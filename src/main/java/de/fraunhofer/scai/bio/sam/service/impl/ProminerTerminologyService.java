/**
 * 
 */
package de.fraunhofer.scai.bio.sam.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Multimap;

import de.fraunhofer.scai.bio.sam.config.JpmProperties;
import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.domain.DAO.Mapping;
import de.fraunhofer.scai.bio.sam.domain.DAO.Mapping.MAPPING_TYPE;
import de.fraunhofer.scai.bio.sam.domain.DAO.Terminology;
import de.fraunhofer.scai.bio.sam.domain.DAO.VersionTag;
import de.fraunhofer.scai.bio.sam.service.ConceptSearchService;
import de.fraunhofer.scai.bio.sam.service.ConceptService;
import de.fraunhofer.scai.bio.sam.service.ConceptSuggestService;
import de.fraunhofer.scai.bio.sam.service.CurieService;
import de.fraunhofer.scai.bio.sam.service.TerminologyService;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;

/**
 * @author Marc Jacobs
 * 
 * used for terminologies coming from ProMiner-syn-files
 * allows to list, search and auto-complete
 *
 */
@Order(4)
@Service("ProminerTerminologyService")
@Profile("JPM")
public class ProminerTerminologyService implements TerminologyService, ConceptService, ConceptSearchService, ConceptSuggestService {

    Logger log = LoggerFactory.getLogger(getClass());

    Map<String, Terminology> terminologies;                     // all terminologies
    Map<String, Map<String, Concept>> concepts;                 // all concepts in a terminology
    Map<String, VersionTag> versions;
    Map<String, Map<String, Collection<Concept>>> names;        // concept search
    EnumSet<ConceptSearchService.SEARCH_FIELDS> searchFields;   // TODO which fields to search
    PatriciaTrie<String> prefixes = new PatriciaTrie<String>(); // concept suggestion
    
    public ProminerTerminologyService(JpmProperties jpmProperties, CurieService curies) {

        Collection<SEARCH_FIELDS> col = new ArrayList<SEARCH_FIELDS> ();        
        col.add(SEARCH_FIELDS.prefLabel);
        col.add(SEARCH_FIELDS.altLabels);
        searchFields = EnumSet.copyOf(col);
        
        terminologies = new TreeMap<String, Terminology>();
        concepts = new TreeMap<String, Map<String,Concept>>();
        versions = new TreeMap<String, VersionTag>();
        names = new TreeMap<String, Map<String, Collection<Concept>>>();
        
        log.info(" >> starting Prominer terminology service");
        loadTerminologies(jpmProperties.getSynFileFolder(), jpmProperties.getTerminologies(), curies);
    }

    @Override
    public void save(Terminology t) {
        log.warn(" >> not implemented - won't fix");
    }

    @Override
    public Terminology getTerminology(String id) {
        for(String key : terminologies.keySet()) {
            if(id.toLowerCase().contains(key.toLowerCase())) {
                return terminologies.get(key);
            }
        }
        return null;
    }

    @Override
    public Page<Terminology> getAllTerminologies(Pageable request) {

        log.debug(" >> TMS: {}", terminologies.keySet());
        return new PageImpl<Terminology>(new ArrayList<Terminology>(terminologies.values()));
    }

    public String toString() {
        return "ProminerTerminologyService";
    }
    
    @Override
    public long getCountOfTerminologies() {
        return terminologies.size();
    }

    @Override
    public boolean existsByID(String id) {
        return terminologies.containsKey(id);
    }

    @Override
    public VersionTag getLastVersionTag(String terminologyId) {

        Terminology t = getTerminology(terminologyId);

        if(t != null) return versions.get(t.getOlsID());
        return null;
    }

    private void loadTerminologies(String folder, List<String> list, CurieService curies) {
        log.info(" >>> parsing terminologies from '{}'", folder);

        for(String entry : list) {
            
            long nofSyn = 0l;

            String filename = entry;    //.split("@")[0];
            String terminologyName = FileNameUtils.getBaseName(filename).replace("Raw", "");

            File terminologyFile = FileUtils.getFile(folder, filename);

            Terminology t = new Terminology();
            t.setDescription("import from JProminer syn-file " + terminologyFile.getName());

            // prefix in synfile used needs to be mentioned first
            String shortName = null;
            
            if(terminologyName.contains("_")) {
                shortName= terminologyName.split("_")[0];
            } else {
                shortName = terminologyName;
            }

            if(curies.getPrefix(shortName) != null) {
                t.setIri(curies.getPrefix(shortName));
            } else {
                t.setIri("");
            }

            // nice long name
            t.setLongName(terminologyName.replace("_", " "));          
            t.setShortName(shortName);
            t.setOlsID(shortName);

            Map<String, Collection<Concept>> namesMap;
            Map<String,Concept> conceptMap;
           
            if(!terminologies.containsKey(t.getOlsID())) {
                terminologies.put(t.getOlsID(), t);
                versions.put(t.getOlsID(), new VersionTag("", OffsetDateTime.now()));
                namesMap = new TreeMap<String, Collection<Concept>>();                
                names.put(t.getOlsID(), namesMap);
                conceptMap = new TreeMap<String,Concept>();
            } else {
                log.warn(t.getOlsID() + " is already loaded; appending");
                
                namesMap = names.get(t.getOlsID());
                conceptMap = concepts.get(t.getOlsID());
            }

            try {
                // DB00001@DRUGBANK|Lepirudin:BIOD00024|BTD00024|Hirudin variant-1|Lepirudin|Lepirudin recombinant

                log.info(" >>> Scanning concepts from {}...", terminologyFile.getName());

                Scanner scanner;
                try {
                    scanner = new Scanner(terminologyFile, "UTF-8");
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();

                        // check for comments
                        if(line.startsWith("#")) {
                            continue;
                        }
                        
                        String[] token = line.split("\\|");
                        if(token.length<=1) {
                            log.error(" >>>> invalid record: {}", line);
                        } else {
                            Concept concept = new Concept();
                            concept.setEncoding("UTF-8");
                            concept.setAltLabels(new ArrayList<String>());
                            concept.setPrefLabel(token[1].split(":")[0]);
                            concept.getAltLabels().add(token[1].split(":")[1]);
                            concept.setDefiningTerminology(token[0].split("@")[1]);
                            concept.setParent(false);
                            concept.setLocalID(concept.getDefiningTerminology()+":"+token[0].split("@")[0]);

                            for(int i=2; i<token.length; i++) {
                                concept.getAltLabels().add(token[i]);
                                nofSyn++;
                            }

                            concept.setDescription(String.format("%s (%s) is a concept from '%s'", concept.getPrefLabel(), concept.getLocalID(), concept.getDefiningTerminology()));
                            concept.setIri(t.getIri() + token[0].split("@")[0]);

                            log.trace(" >>>> adding {}", t.getShortName()+"@"+concept.getIri());
                            conceptMap.put(concept.getIri(), concept);
                            
                            String label = concept.getPrefLabel();
                            if(!namesMap.containsKey(label)) {
                                namesMap.put(label, new HashSet<Concept>());
                            }
                                
                            prefixes.put(label, label);

                            namesMap.get(concept.getPrefLabel()).add(concept);
                            for(String label1 : concept.getAltLabels()) {
                                if(!namesMap.containsKey(label1)) {
                                    namesMap.put(label1, new HashSet<Concept>());
                                    prefixes.put(label1, label);
                                }
                                namesMap.get(label1).add(concept);                                
                            }
                        }

                    }
                    scanner.close();
                    log.info(" >>> Got {} concepts, {} synonyms from {}.", conceptMap.size(), nofSyn, t.getShortName());

                } catch (FileNotFoundException e) {
                    log.error(e.getLocalizedMessage());
                    log.debug(e.getLocalizedMessage(), e);
                }

                concepts.put(t.getOlsID(), conceptMap);

            } catch (Exception e) {
                log.error(" >> wrong terminology file '{}'", folder);
                log.debug(e.getLocalizedMessage(), e);
            }
        }

        log.info(" >>> Got {} terminologies. {}", terminologies.size(), terminologies.keySet());
    }

    // ConceptService

    @Override
    public void saveConceptHierarchyRelations(Multimap<String, String> relationFromTo) {
        log.warn(" >> JPM: not implemented: saveConceptHierarchyRelations");

    }

    @Override
    public void saveConcepts(String terminologyId, List<Concept> concepts) {
        log.warn(" >> JPM: not implemented: saveConcepts");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Concept getConceptOfTerminology(String terminology, String iri) throws NotFoundException {

        log.debug(" >> Searching for {} - {} (getConceptOfTerminology)", terminology, iri);
        log.trace(concepts.keySet().toString());
        
        if(concepts.containsKey(terminology)) {
            log.trace(">"+ concepts.get(terminology).size() + "<");
            
            if(concepts.get(terminology).containsKey(iri)) {
                log.trace(concepts.get(terminology).get(iri).toString());
                
                return concepts.get(terminology).get(iri);                
            }
        }

        log.error(" >> JPM: Unsuccessfully searched for {} with {} (getConceptOfTerminology)", terminology, iri);
        throw new NotFoundException();
    }

    @Override
    public Page<Concept> getDirectParents(Concept concept, Pageable request) throws NotFoundException {
        return null;
    }

    @Override
    public Page<Concept> getDirectChildren(Concept concept, Pageable request) throws NotFoundException {
        return null;
    }

    @Override
    public Page<Concept> getTransitiveChildren(Concept concept, Pageable request)
            throws NotFoundException {
        return null;
    }

    @Override
    public Page<Concept> getRelatedConcepts(Concept concept, Pageable request) throws NotFoundException {
        return null;
    }

    @Override
    public boolean isParent(Concept concept) throws NotFoundException {
        return concept.isParent();
    }

    @Override
    public Page<Mapping> getMappings(Concept concept, MAPPING_TYPE type, Pageable request) throws NotFoundException {
        log.warn(" >> JPM: not implemented: getMappings");
        return Page.empty();
    }

    @Override
    public Page<Concept> getTopConceptOfTerminology(String terminology, Pageable request) throws NotFoundException {
        log.warn(" >> JPM: not implemented: getTopConceptOfTerminology");
        return Page.empty();
    }

    @Override
    public Page<Concept> getConceptsOfTerminology(String terminology, Pageable request)
            throws NotFoundException {

        if(!concepts.containsKey(terminology)) {
            throw new NotFoundException(terminology + " not indexed.");
        }

        List<Concept> content = new ArrayList<Concept>();
        long total = concepts.get(terminology).size();

        int i = -1;
        if(concepts.containsKey(terminology)) {
            Map<String, Concept> tConcepts = concepts.get(terminology);
            for(String key : tConcepts.keySet()) {
                i++;
                if(i < request.getPageNumber()*request.getPageSize()) { continue; }
                if(i >= (request.getPageNumber()+1)*request.getPageSize()) { continue; }

                content.add(tConcepts.get(key));
            }
        }

        return new PageImpl<Concept>(content, request, total);
    }

    @Override
    public Page<Concept> searchConceptExactByIRI(String iri, Pageable request) {

        List<Concept> content = new ArrayList<Concept>();
        long total = 0l;
        int i = -1;

        for(String terminology : concepts.keySet()) {
            i++;
            if(concepts.get(terminology).containsKey(iri)) {
                total++;
                if(i < request.getPageNumber()*request.getPageSize()) { continue; }
                if(i >= (request.getPageNumber()+1)*request.getPageSize()) { continue; }

                content.add( concepts.get(terminology).get(iri) );
            }
        }

        return new PageImpl<Concept>(content, request, total) ;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Concept getConcept(String iri) throws NotFoundException {
        log.debug(" >> Searching for {} (getConcept)", iri);

        // search all terminologies
        for(String terminology : concepts.keySet()) {
            if(concepts.get(terminology).containsKey(iri)) {
                return concepts.get(terminology).get(iri);
            }
        }

        log.error(" >> Unsuccessfully searched for {} (getConcept)", iri);
        throw new NotFoundException();
    }

    @Override
    public Page<Concept> searchConceptInTerminology(String terminology, EnumSet<SEARCH_FIELDS> searchFields,
            boolean exact, String query, Pageable request) {

        List<Concept> content = null;

        log.debug(" >>> searchConcept {} in [names] for {}", query, terminology);
        if(names.containsKey(terminology)) {
            log.trace("{}",names.get(terminology).size());
            if(names.get(terminology).containsKey(query)) {
                if(content == null) {
                    content = new ArrayList<>();
                }
                log.trace("terminology {}, query {} = {}", terminology, query, names.get(terminology).get(query));
                content.addAll(names.get(terminology).get(query));
            }
        }
        
        if(content == null) {
            return Page.empty();
        } else {
            return new PageImpl<Concept>(content, request, content.size());
        }
        
    }

    @Override
    public Page<Concept> searchConcept(EnumSet<SEARCH_FIELDS> searchFields, boolean exact, String query,
            Pageable request) {

        List<Concept> content = collectConcepts(query);
        
        if(content == null) {
            return Page.empty();
        } else {
            return new PageImpl<Concept>(content, request, content.size());
        }
    }

    private List<Concept> collectConcepts(String query) {
        List<Concept> content = null;

        log.debug(" >>> searchConcept {} in [names] for {}", query, names.keySet());
        for(String terminology : names.keySet()) {
            if(names.containsKey(terminology)) {
                if(names.get(terminology).containsKey(query)) {
                    if(content == null) {
                        content = new ArrayList<>();
                    }
                    log.trace("terminology {}, query {} = {}", terminology, query, names.get(terminology).get(query));
                    content.addAll(names.get(terminology).get(query));
                }
            }
        }
        return content;
    }

    @Override
    public Page<Concept> suggestConceptInTerminology(String terminologyId, @NotNull @Valid String query, Pageable request) {
        
        return searchConceptInTerminology(terminologyId, searchFields, true, query, request);
    }

    @Override
    public Page<Concept> suggestConcepts(@NotNull @Valid String query, Pageable request) {
        
        Set<Concept> content = new HashSet<Concept>();
                
        // TODO
        request.getOffset();
        request.getPageNumber();
        request.getPageSize();
        
        log.debug("{}", request);
         
        for(Entry<String, String> concept : prefixes.prefixMap(query).entrySet()) {
            log.debug("{} -> {}", concept.getKey(), concept.getValue());
            content.addAll(collectConcepts(concept.getValue()));
        }
        
        if(content == null || content.isEmpty()) {
            return Page.empty();
        } else {
            return new PageImpl<Concept>(new ArrayList<>(content), request, content.size());
        }
    }
}
