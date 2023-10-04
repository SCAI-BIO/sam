package de.fraunhofer.scai.bio.sam.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.domain.task.ExportTask;
import de.fraunhofer.scai.bio.sam.service.CurieService;
import de.fraunhofer.scai.bio.sam.service.TerminologyExportService;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;
import de.fraunhofer.scai.bio.sam.service.impl.delegator.ConceptServiceDelegator;
import de.fraunhofer.scai.bio.sam.service.impl.delegator.TerminologyServiceDelegator;

/**
 * ProminerExportService
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 **/
@Service
@EnableAsync
@EnableCaching
public class ProminerExportService extends TerminologyExportService {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	TerminologyServiceDelegator terminologyService;

	@Autowired
	ConceptServiceDelegator conceptService;
	
	@Autowired
	CurieService curieService;
	
	@Async
	@Override
	public CompletableFuture<Long> export(ExportTask task) throws IOException, NotFoundException {
		
		log.info(" >> preparing export: {} {}", task.getTaskId(), task.getFilename());
		File export = File.createTempFile(task.getFilename(),".tgz");
				
		String officialTerminologyId = terminologyService.searchTerminology(task.getTerminologyId(), "");
        log.debug(" >>> exporting {}", officialTerminologyId);

        if(officialTerminologyId==null || officialTerminologyId.trim().isEmpty() ){
			throw new IllegalArgumentException("passed terminologyId is either null, empty or not existent!");
		}
		if( !terminologyService.existsByID(officialTerminologyId)){
			throw new NotFoundException("terminologyId not present!");
		}

		try (OutputStream fOut = Files.newOutputStream(Paths.get(export.getAbsolutePath()));
			     BufferedOutputStream buffOut = new BufferedOutputStream(fOut);
			     GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(buffOut);
			     TarArchiveOutputStream tOut = new TarArchiveOutputStream(gzOut)) {

			if(!task.isSplitted()) {	
			    log.debug(" >>> looking into {}", officialTerminologyId);

				Page<Concept> conceptPage = conceptService.getConceptsOfTerminology(officialTerminologyId, PageRequest.of(0, 500));
				File f = writeConceptsOfTerminologyToFile(task, conceptPage, task.getFilename());
				
				TarArchiveEntry tarEntry = new TarArchiveEntry(f, f.getName());
				tOut.putArchiveEntry(tarEntry);

				// copy file to TarArchiveOutputStream
				Files.copy(Paths.get(f.getAbsolutePath()), tOut);

				tOut.closeArchiveEntry();
				tOut.flush();
				tOut.finish();
				tOut.close();
				
				gzOut.flush();
				gzOut.finish();
				gzOut.close();

				buffOut.flush();
				buffOut.close();
				
			} else {
			    log.debug(" >>> looking into {}", officialTerminologyId);
			    
			    Page<Concept> top = conceptService.getTopConceptOfTerminology(officialTerminologyId, PageRequest.of(0, 500));
			    
				for(Concept topConcept: top) {
					curieService.setCurieAndId(topConcept, topConcept.getID(), officialTerminologyId);
					
					Page<Concept> conceptPage = conceptService.getTransitiveChildren(topConcept, PageRequest.of(0, 500));
					
					log.debug(" >>> root: {} - {} concepts found.", topConcept.getLocalID(), conceptPage.getTotalElements());
					
					String fname = task.getFilename().replaceAll(topConcept.getLocalID().split(":")[0], topConcept.getLocalID().replaceAll(":", "_"));
					File f = writeTransitiveChildrenToFile(task, conceptPage, fname, topConcept); 
					
					TarArchiveEntry tarEntry = new TarArchiveEntry(f, f.getName());
					tOut.putArchiveEntry(tarEntry);
					Files.copy(Paths.get(f.getAbsolutePath()), tOut);
					tOut.closeArchiveEntry();					
				}
				tOut.flush();
				tOut.finish();
				tOut.close();
			}
			
			gzOut.flush();
			gzOut.finish();
			gzOut.close();

			buffOut.flush();
			buffOut.close();
			
			log.info(" >>> written {}", export.getAbsoluteFile());
		}

		getFiles().put(task.getTaskId(), export);
		
		return CompletableFuture.completedFuture(export.length());
	}

	File writeConceptsOfTerminologyToFile(ExportTask task, Page<Concept> conceptPage, String filename)
			throws IOException, NotFoundException, FileNotFoundException {
		File export = File.createTempFile(filename,".syn");
		int nofConcepts = 0;
		
		if(export==null || !export.isFile() || !export.canWrite()){
			throw new IllegalArgumentException("cannot write to passed FD!");
		}
		try(PrintWriter out = new PrintWriter(export)) {
			
			nofConcepts = writeConceptsOfPageToFile(task, nofConcepts, out, conceptPage);
			log.debug(" >>> {} - gone through first page of {} of {}.", task.getTerminologyId(), conceptPage.getNumber()+1, conceptPage.getTotalPages());

			while (conceptPage.hasNext()) {
				conceptPage = conceptService.getConceptsOfTerminology(task.getTerminologyId(), conceptPage.nextPageable());
				nofConcepts = writeConceptsOfPageToFile(task, nofConcepts, out, conceptPage);
				log.debug(" >>> {} - gone through {} page of {}.", task.getTerminologyId(), conceptPage.getNumber()+1, conceptPage.getTotalPages());
			}
			
			out.close();
		}
		
		log.info(" >>> {} - {} concepts exported.", task.getTerminologyId(), nofConcepts);
		return export;
	}


	private File writeTransitiveChildrenToFile(ExportTask task, Page<Concept> conceptPage, String filename, Concept topConcept)
			throws IOException, NotFoundException, FileNotFoundException {
		
		File export = File.createTempFile(filename,".syn");
		int nofConcepts = 0;
		
		try(PrintWriter out = new PrintWriter(export)) {
			
			nofConcepts = writeConceptsOfPageToFile(task, nofConcepts, out, conceptPage);
			log.debug("{} - gone through first page of {} of {}.", task.getTerminologyId(), conceptPage.getNumber()+1, conceptPage.getTotalPages());

			while (conceptPage.hasNext()) {
				conceptPage = conceptService.getTransitiveChildren(topConcept, conceptPage.nextPageable());						
				nofConcepts = writeConceptsOfPageToFile(task, nofConcepts, out, conceptPage);
				log.debug("{} - gone through {} page of {}.", task.getTerminologyId(), conceptPage.getNumber()+1, conceptPage.getTotalPages());
			}
			
			out.close();
		}
		
		log.info(" >>> {} - {} concepts exported.", task.getTerminologyId(), nofConcepts);
		return export;
	}

	private int writeConceptsOfPageToFile(ExportTask task, int nofConcepts, PrintWriter out, Page<Concept> conceptPage) {
		log.debug(" >>> writing concepts {} / {} ", nofConcepts, conceptPage.getNumberOfElements());
	    for (Concept concept : conceptPage.getContent()) {
			
            log.debug(" >>> {}, {}", concept.getID(), concept.getLocalID());

            curieService.setCurieAndId(concept, concept.getLocalID(), task.getTerminologyId());
			
			if(concept.getPrefLabel().equals("Thing")) continue;
			if(concept.getLocalID() == null) continue;

			nofConcepts++;
			String synonyms = prepareSynonyms(concept);
			String references = prepareReferences(concept);
			if(references != null && !references.isEmpty()) {
				if(synonyms != null && !synonyms.isEmpty()) {
					out.println(references+":"+synonyms);
				} else  {
					out.println(references);
				}
			}
		}
		
		out.flush();
		return nofConcepts;
	}

	protected String prepareSynonyms(Concept concept) {
		String synonyms =concept.getAltLabels().stream().map(s -> s.replaceAll("[\r\n\t:]", " ").replaceAll("\\s+", " ")).collect(Collectors.joining("|"));

		if(concept.getPrefLabel()!=null &&  !concept.getPrefLabel().trim().isEmpty()) {
			if (synonyms!=null && !synonyms.trim().isEmpty()){
				return concept.getPrefLabel().replaceAll("[\r\n\t:]", " ").replaceAll("\\s+", " ") + "|"+synonyms;
			}else{
				return concept.getPrefLabel().replaceAll("[\r\n\t:]", " ").replaceAll("\\s+", " ");
			}
		}else {
			return synonyms;
		}
	}

	protected String prepareReferences(Concept concept) {
		if(concept.getPrefLabel()!=null &&  !concept.getPrefLabel().trim().isEmpty()) {
			return concept.getJProMinerFormatedID() + "|" + concept.getPrefLabel().replaceAll("[\r\n\t: ]", " ").replaceAll("\\s+", " ");
		}
		else{
			return concept.getJProMinerFormatedID();
		}
	}

}
