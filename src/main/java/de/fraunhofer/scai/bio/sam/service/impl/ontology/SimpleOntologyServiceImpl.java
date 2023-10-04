package de.fraunhofer.scai.bio.sam.service.impl.ontology;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.fraunhofer.scai.bio.owltooling.OntologyReasoner;
import de.fraunhofer.scai.bio.owltooling.TMOntology;
import de.fraunhofer.scai.bio.sam.domain.DTO.DescriptionDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.OntologyCheckDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.OntologyDTO;
import de.fraunhofer.scai.bio.sam.service.CurieService;
import de.fraunhofer.scai.bio.sam.service.OntologyService;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;

@Order(1)
@Service("OntologyService")
public class SimpleOntologyServiceImpl implements OntologyService {

    @Autowired
    CurieService curies;
    
    Logger logger = LoggerFactory.getLogger(getClass());

    Map<String, OWLOntology> ontologies;
    Map<String, OntologyDTO> metadata;


    SimpleOntologyServiceImpl() {
        ontologies = new HashMap<String, OWLOntology>();
        metadata = new HashMap<String, OntologyDTO>();
    }

    @Override
    public OWLOntology getOntology(String id) throws NotFoundException {
        if(ontologies.containsKey(id)) {
            return ontologies.get(id);
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public Page<OWLOntology> getAllOntologies(Pageable request) {
        List<OWLOntology> olist = new ArrayList<OWLOntology>(ontologies.values());

        Page<OWLOntology> page = new PageImpl<OWLOntology>(olist);
        return page;
    }

    @Override
    public long getCountOfOntologies() {
        return ontologies.size();
    }

    @Override
    public boolean existsByID(String id) {
        return ontologies.containsKey(id);
    }

    @Override
    public void loadOntology(String id, String iri, String lang) throws OWLOntologyCreationException {
        logger.info(" >> loading {} from {}", id, iri);
        OWLOntology o;
        IRI documentIRI =  IRI.create(iri);
        o = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(documentIRI);
        ontologies.put(id, o);

        metadata.put(id, 
                extractMetaData(id, iri, lang, o)
                );

        logger.info("done.");
    }

    public File saveOntology(String ontologyId, String format) throws IOException, NotFoundException, OWLOntologyStorageException {
        OWLOntology ontology = getOntology(ontologyId);
        File file;
        OutputStream fOut;

        if(format.toLowerCase().equals("rdf")) {
            file = new File(ontologyId+".rdf");
            fOut = Files.newOutputStream(Paths.get(file.getAbsolutePath()));
            ontology.saveOntology(new RDFXMLDocumentFormat(), fOut);

        } else if (format.toLowerCase().equals("ttl")) {
            file = new File(ontologyId+".ttl");
            TurtleDocumentFormat ttlFormat = new TurtleDocumentFormat();
            if (ontology.getNonnullFormat().isPrefixOWLDocumentFormat()) {
                ttlFormat.copyPrefixesFrom(ontology.getNonnullFormat().asPrefixOWLDocumentFormat());
            }
            fOut = Files.newOutputStream(Paths.get(file.getAbsolutePath()));
            ontology.saveOntology(ttlFormat, fOut);

            // default if (format.toLowerCase().equals("owl"))
        } else {
            file = new File(ontologyId+".owl");
            OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
            if (ontology.getNonnullFormat().isPrefixOWLDocumentFormat()) {
                owlxmlFormat.copyPrefixesFrom(ontology.getNonnullFormat().asPrefixOWLDocumentFormat());
            }
            fOut = Files.newOutputStream(Paths.get(file.getAbsolutePath()));
            ontology.saveOntology(owlxmlFormat, fOut);
        }

        fOut.flush();
        fOut.close();

        logger.info("Written " + file.getName());

        return file;
    }

    private OntologyDTO extractMetaData(String id, String iri, String lang, OWLOntology o) {
        OntologyDTO meta = new OntologyDTO();
        meta.setLongName(lang);
        meta.setOntologyID(id);
        meta.setSize(o.getAxiomCount());
        o.getOntologyID();
        meta.setFormat(o.getFormat().toString());
        meta.setBaseURL(o.getOntologyID().toString());

        DescriptionDTO desc = new DescriptionDTO();
        desc.setDescription(iri);
        desc.setLang(lang);
        meta.setDescriptions(desc);
        return meta;
    }

    @Override
    public OntologyDTO getOntologyInfo(String id) throws NotFoundException {
        if(!ontologies.containsKey(id)) {
            throw new NotFoundException();
        }

        return metadata.get(id);
    }

    @Override
    public Page<String> getAllOntologyIDs(Pageable request) {
        List<String> olist = new ArrayList<String>(ontologies.keySet());

        Page<String> page = new PageImpl<String>(olist);
        return page;
    }

    public List<String> generateLanguageVersions(String id) throws OWLOntologyCreationException, NotFoundException {

        if(!ontologies.containsKey(id)) {
            throw new NotFoundException();
        }

        OWLOntology o = ontologies.get(id);
        List<String> generated = new ArrayList<String>();
        String iri = metadata.get(id).getDescriptions().getDescription();

        logger.info(" >> Creating language versions...");

        for(TMOntology tmo : OntologyReasoner.getLanguageOntologies(curies.getPrefixManager(), o, true, id)) {
            ontologies.put(tmo.getName(), tmo.getOntology());
            generated.add(tmo.getName());

            String lang = tmo.getName().split("_lang-")[1];
            metadata.put(tmo.getName(), 
                    extractMetaData(tmo.getName(), "derived from " + iri, lang, tmo.getOntology())
                    );
            logger.info("     Created " + tmo.getName());                
        }

        return generated;
    }

    public List<String> generateTextMiningBins(String id, String reasoning, boolean skipBFO) throws OWLOntologyCreationException, NotFoundException {

        if(!ontologies.containsKey(id)) {
            throw new NotFoundException();
        }

        OWLOntology o = ontologies.get(id);
        List<String> generated = new ArrayList<String>();
        String iri = metadata.get(id).getDescriptions().getDescription();
        
        OWLReasoner reasoner = OntologyReasoner.createReasoner(o, reasoning);

//        logger.info(" >> Creating text mining bins...");
//        IRI skipNode = null;
//        if(skipBFO) {
//            skipNode = IRI.create("http://purl.obolibrary.org/obo/BFO_0000001");
//        }
//        
//        Map<String, List<String>> bins =  OntologyReasoner.getTextMiningBins(reasoner, pm, o, skipNode, true);      
//        bins.keySet().forEach(bin -> {
//            logger.info(bin + ": " + bins.get(bin).size());    
//        });

        OntologyReasoner.getTextMiningOntologies(reasoner, curies.getPrefixManager(), o, skipBFO, true, id).forEach(tmo -> {
            ontologies.put(tmo.getName(), tmo.getOntology());
            generated.add(tmo.getName());
            
            metadata.put(tmo.getName(), 
                    extractMetaData(tmo.getName(), "derived from " + iri, "", tmo.getOntology())
                    );
            logger.info("     Created " + tmo.getName());                
        });
        
        return generated;
    }

    public OntologyCheckDTO check(String id, String algorithm) throws NotFoundException {
        
        if(!ontologies.containsKey(id)) {
            throw new NotFoundException();
        }

        OntologyCheckDTO results = new OntologyCheckDTO();
        
        OWLOntology o = ontologies.get(id);
        OWLReasoner reasoner;
        
        results.setOntologyID(id);
        results.setIri(o.getOntologyID().getOntologyIRI().get().getIRIString());

        if(!algorithm.isEmpty()) {
            reasoner = OntologyReasoner.createReasoner(o, algorithm);
        } else {
            logger.warn("no resoner provided - talking degfault.");
            reasoner = OntologyReasoner.createReasoner(o, "STRUCTURAL");
        }           
        
        Set<String> unsatClasses = new TreeSet<String>();
        for(OWLClass clazz : OntologyReasoner.checkUnsatisfiable(reasoner)) {
            unsatClasses.add(clazz.getIRI().getIRIString());
        }
        results.setUnsatisfiable(unsatClasses);        
        results.setConsistent(OntologyReasoner.checkConsistent(reasoner));
        results.setLanguages(OntologyReasoner.checkLanguages(o, curies.getPrefixManager(), true)); 
        results.setNameSpaces(OntologyReasoner.checkNameSpaces(o, curies.getPrefixManager()));

        results.setCycles(OntologyReasoner.checkHierarchy(reasoner, curies.getPrefixManager(), o, -1));
//        OntologyReasoner.checkMappings(o, pm,  o.getOntologyID().getOntologyIRI().toString(), id,  metadata.get(id).getBaseURL(), true);
//        OntologyReasoner.checkMappings(cti.getOntology(), cti.getPrefixManager(), cti.getOntologyIRI().toString(), "cti", cti.getSource(), true);

        return results;
    }
}
