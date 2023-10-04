/**
 * 
 */
package de.fraunhofer.scai.bio.sam.service.impl;

import java.io.File;
import java.nio.charset.Charset;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Multimap;

import de.fraunhofer.scai.bio.sam.config.ItsProperties;
import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.domain.DAO.Mapping;
import de.fraunhofer.scai.bio.sam.domain.DAO.Mapping.MAPPING_TYPE;
import de.fraunhofer.scai.bio.sam.domain.DAO.Terminology;
import de.fraunhofer.scai.bio.sam.domain.DAO.VersionTag;
import de.fraunhofer.scai.bio.sam.service.ConceptService;
import de.fraunhofer.scai.bio.sam.service.TerminologyService;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;

/**
 * @author Marc Jacobs
 * 
 * used for non real terminologies coming from simple taggers
 *
 */
@Order(4)
@Service("InternalTerminologyService")
@Profile("ITS")
public class InternalTerminologyService implements TerminologyService, ConceptService {

    Logger log = LoggerFactory.getLogger(getClass());

    Map<String, Terminology> terminologies;
    Map<String, Concept> concepts;
    Map<String, VersionTag> versions;

    public InternalTerminologyService(ItsProperties itsProperties) {

        terminologies = new TreeMap<String, Terminology>();
        concepts = new TreeMap<String, Concept>();
        versions = new TreeMap<String, VersionTag>();

        log.info(" >> starting internal terminology service");
        loadTerminologies(itsProperties.getTerminologies());
        loadConcepts(itsProperties.getConcepts());
    }

    public String toString() {
        return "InternalTerminologyService";
    }
    
    @Override
    public void save(Terminology t) {
        log.warn(" >> not implemented - won't fix");
    }

    @Override
    public Terminology getTerminology(String id) {
        Terminology t = null;
        int hit = -1;
        
        log.debug(" >> get terminology {}", id);
        for(String key : terminologies.keySet()) {
            if(id.toLowerCase().contains(key.toLowerCase())) {
                if(hit<key.length()) {
                    hit = key.length();
                    t = terminologies.get(key);
                    log.debug(" >>> mapped {}", key);
                }
            }
        }
        return t;
    }

    @Override
    public Page<Terminology> getAllTerminologies(Pageable request) {

        log.debug(" >> TMS: {}", terminologies.keySet());
        return new PageImpl<Terminology>(new ArrayList<Terminology>(terminologies.values()));
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

    private void loadTerminologies(String ressource) {
        log.info(" >>> parsing terminologies... {}", ressource);

        //InputStream in = new ClassPathResource(ressource).getInputStream();

        File terminologyFile = new File(ressource);

        try {
            CSVParser parser = CSVParser.parse(terminologyFile, Charset.forName("UTF-8"), CSVFormat.TDF.withFirstRecordAsHeader());
            parser.forEach(record -> {
                if(record.isConsistent()) {
                    Terminology t = new Terminology();
                    t.setOlsID(record.get("olsid"));
                    t.setDescription(record.get("description"));
                    t.setIri(record.get("iri"));
                    t.setLongName(record.get("longname"));
                    t.setShortName(record.get("shortname"));

                    terminologies.put(record.get("olsid"), t);
                    versions.put(record.get("olsid"), new VersionTag(record.get("version"), OffsetDateTime.now()));

                } else {
                    log.error(" >>>> invalid record: {}", record.toString());
                }
            });
            parser.close();

        } catch (Exception e) {
            log.error(" >> wrong terminology file '{}'", ressource);
            log.debug(e.getLocalizedMessage(), e);
        }

        log.info(" >>> Got {} terminologies. {}", terminologies.size(), terminologies.keySet());

        if(terminologies.size() > 0) {
            log.info(" >>> example terminology: {}", terminologies.entrySet().iterator().next());
        }

        log.trace(" {}", terminologies);

    }

    // ConceptService

    @Override
    public void saveConceptHierarchyRelations(Multimap<String, String> relationFromTo) {
        log.warn(" >> not implemented: saveConceptHierarchyRelations");

    }

    @Override
    public void saveConcepts(String terminologyId, List<Concept> concepts) {
        log.warn(" >> not implemented: saveConcepts");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Concept getConceptOfTerminology(String terminology, String iri) throws NotFoundException {

        log.debug(" >> Searching for {}:{} (getConceptOfTerminology)", terminology, iri);

        if(concepts.containsKey(terminology+":"+iri)) {
            return concepts.get(terminology+":"+iri);

            // searching for unnormalized concepts coming from ML models
        } else if(iri.split(":").length == 2) {
            String term = iri.split(":")[1];
            String searchterm = terminology+":"+iri.split(":")[0]+":*";

            log.trace(" >>> search for {}", searchterm );
            // replace placeholder with actual String
            if(concepts.containsKey(searchterm)) {
                Concept c1 = concepts.get(searchterm);

                Concept c2 = new Concept();
                c2.setAltLabels(c1.getAltLabels());
                c2.setAnnotations(c1.getAnnotations());
                c2.setDefiningTerminology(c1.getDefiningTerminology());
                c2.setDescription(c1.getDescription().replace("*", term));
                c2.setEncoding(c1.getEncoding());
                c2.setIri(c1.getIri().replace("*", term).replaceAll("\\s", "_"));
                c2.setLocalID(c1.getID().replace("*", term).replaceAll("\\s", "_"));
                c2.setParent(c1.isParent());
                c2.setPartofNameSpace(c1.isPartOFNameSpace());
                if(terminology.contains("Gold")) { c2.setPrefLabel(term+" [Au]"); }
                else { c2.setPrefLabel(term); }

                return c2;
            }

        }

        log.error(" >> ITS: Unsuccessfully searched for {} with {} (getConceptOfTerminology)", terminology, iri);
        throw new NotFoundException();
    }

    @Override
    public Page<Concept> getDirectParents(Concept concept, Pageable request) throws NotFoundException {
        log.warn(" >> ITS: not implemented: getDirectParents");
        return null;
    }

    @Override
    public Page<Concept> getDirectChildren(Concept concept, Pageable request) throws NotFoundException {
        log.warn(" >> ITS: not implemented: getDirectChildren");
        return null;
    }

    @Override
    public Page<Concept> getTransitiveChildren(Concept concept, Pageable request)
            throws NotFoundException {
        log.warn(" >> ITS: not implemented: getTransitiveChildren");
        return null;
    }

    @Override
    public Page<Concept> getRelatedConcepts(Concept concept, Pageable request) throws NotFoundException {
        log.warn(" >> ITS: not implemented: getRelatedConcepts");
        return null;
    }

    @Override
    public boolean isParent(Concept concept) throws NotFoundException {
        return concept.isParent();
    }

    @Override
    public Page<Mapping> getMappings(Concept concept, MAPPING_TYPE type, Pageable request) throws NotFoundException {
        log.warn(" >> ITS: not implemented: getMappings");
        return null;
    }

    @Override
    public Page<Concept> getTopConceptOfTerminology(String terminology, Pageable request) throws NotFoundException {
        log.warn(" >> ITS: not implemented: getTopConceptOfTerminology");
        return null;
    }

    @Override
    public Page<Concept> getConceptsOfTerminology(String terminology, Pageable request)
            throws NotFoundException {
        log.warn(" >> ITS: not implemented: getConceptsOfTerminology");
        return null;
    }

    @Override
    public Page<Concept> searchConceptExactByIRI(String id, Pageable request) {
        log.warn(" >> ITS: not implemented: searchConceptExactByIRI");
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Concept getConcept(String iri) throws NotFoundException {
        log.debug(" >> Searching for {} (getConcept)", iri);

        if(concepts.containsKey(iri)) {
            return concepts.get(iri);
        }

        log.error(" >> Unsuccessfully searched for {} (getConcept)", iri);
        throw new NotFoundException();
    }

    private void loadConcepts(String ressource) {
        log.info(" >>> parsing concepts... {}", ressource);

        //InputStream in = new ClassPathResource(ressource).getInputStream();
        File conceptFile = new File(ressource);

        try {
            CSVParser parser = CSVParser.parse(conceptFile, Charset.forName("UTF-8"), CSVFormat.TDF.withFirstRecordAsHeader());
            parser.forEach(record -> {
                if(record.isConsistent()) {
                    Concept c = new Concept();
                    c.setDefiningTerminology(record.get("definingTerminology"));
                    c.setDescription(record.get("description"));
                    c.setIri(record.get("iri"));
                    c.setPrefLabel(record.get("prefLable"));
                    c.setLocalID(record.get("id"));

                    concepts.put(record.get("definingTerminology") + ":" + record.get("id"), c);

                } else {
                    log.error(" >>>> invalid record: {}", record.toString());
                }
            });
            parser.close();

        } catch (Exception e) {
            log.error(" >>> wrong concept file '{}'", ressource);
            log.debug(e.getLocalizedMessage(), e);
        }

        //in.close();
        log.info(" >>> Got {} concepts.", concepts.size());
        if(concepts.size() > 0) {
            log.info(" >>> example concept: {}", concepts.entrySet().iterator().next());
        }
    }

}
