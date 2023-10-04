package de.fraunhofer.scai.bio.sam.service.impl.delegator;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.google.common.collect.Multimap;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.domain.DAO.Mapping;
import de.fraunhofer.scai.bio.sam.domain.DAO.Terminology;
import de.fraunhofer.scai.bio.sam.service.ConceptSearchService;
import de.fraunhofer.scai.bio.sam.service.ConceptService;
import de.fraunhofer.scai.bio.sam.service.ConceptSuggestService;
import de.fraunhofer.scai.bio.sam.service.CurieService;
import de.fraunhofer.scai.bio.sam.service.ServiceRegistry;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;
import de.fraunhofer.scai.bio.sam.service.impl.ols.OLSConceptServiceImpl;

import io.swagger.annotations.ApiParam;

/**
 * ConceptServiceDelegator
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 * @author Marc Jacobs
 **/
@Primary
@Order(1)
@Service("ConceptService")
@DependsOn({"itsProfile", "olsProfile", "loincProfile", "jpmProfile"})
@ComponentScan("de.fraunhofer.scai.bio.sam.config")
public class ConceptServiceDelegator implements ConceptService, ConceptSearchService, ConceptSuggestService {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    CurieService curieService;

    @Autowired
    TerminologyServiceDelegator terminologyService;

    @Autowired
    ServiceRegistry serviceRegistry;

    @Override
    public void saveConceptHierarchyRelations(Multimap<String, String> relationFromTo) {

    }

    @Override
    public void saveConcepts(String terminologyId, List<Concept> concepts) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Concept> T getConceptOfTerminology(String terminology, String iri) throws NotFoundException {
        if (serviceRegistry.getConceptServiceList() != null && serviceRegistry.getConceptServiceList().size() > 0) {
            log.debug(" >> ConceptServiceDelegator.getConcept: {} - ({},{})", serviceRegistry.getConceptServiceList(), terminology, iri);

            Concept service = (Concept) serviceRegistry.getConceptServiceList()
                    .parallelStream()
                    .map(s -> {
                        // test for classes in ontology
                        try {
                            Concept c1 = s.getConceptOfTerminology(terminology, iri);
                            if (c1 != null) {
                                c1.setDefiningTerminology(terminology);
                                c1.setParent(s.isParent(c1));
                            }
                            return c1;
                        } catch (NotFoundException e) {
                            // test for individuals in ontology
                            if (s instanceof OLSConceptServiceImpl) {
                                try {
                                    Concept c1 = ((OLSConceptServiceImpl) s).getIndividualOfTerminology(terminology, iri);
                                    if (c1 != null) {
                                        c1.setDefiningTerminology(terminology);
                                        c1.setParent(s.isParent(c1));
                                    }
                                    return c1;
                                } catch (NotFoundException e1) {
                                    return null;
                                }
                            }

                            return null;
                        }
                    }).filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(""));
            return (T) service;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Concept> Page<T> getDirectParents(Concept concept, Pageable request) throws NotFoundException {
        if (serviceRegistry.getConceptServiceList() != null && serviceRegistry.getConceptServiceList().size() > 0) {
            Page<T> service = (Page<T>) serviceRegistry.getConceptServiceList()
                    .parallelStream()
                    .map(s -> {
                        try {
                            return s.getDirectParents(concept, request);
                        } catch (NotFoundException e) {
                            return null;
                        }
                    }).filter(Objects::nonNull).filter(s -> s.getNumberOfElements() != 0)
                    .findFirst()
                    .orElse(Page.empty(request));

            return service;
        }

        return null;
    }

    @Override
    public <T extends Concept> Page<T> getDirectChildren(Concept concept, Pageable request) throws NotFoundException {
        log.debug("{}", serviceRegistry.getConceptServiceList());

        if (serviceRegistry.getConceptServiceList() != null && serviceRegistry.getConceptServiceList().size() > 0) {
            @SuppressWarnings("unchecked")
            Page<T> service = (Page<T>) serviceRegistry.getConceptServiceList()
                    .parallelStream()
                    .map(s -> {
                        try {
                            return s.getDirectChildren(concept, request);
                        } catch (NotFoundException e) {
                            return null;
                        }
                    }).filter(Objects::nonNull).filter(s -> s.getNumberOfElements() != 0)
                    .findFirst()
                    .orElse(Page.empty(request));

            log.debug(" >>> got {} concepts", service.getNumberOfElements());

            return service;
        }
        return null;
    }

    @Override
    public boolean isParent(Concept concept) throws NotFoundException {
        if (serviceRegistry.getConceptServiceList() != null && serviceRegistry.getConceptServiceList().size() > 0) {
            return serviceRegistry.getConceptServiceList()
                    .parallelStream()
                    .map(s -> {
                        try {
                            return s.isParent(concept);
                        } catch (NotFoundException e) {
                            return false;
                        }
                    }).filter(Objects::nonNull)
                    .findFirst()
                    .orElse(false);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Concept> Page<T> getTransitiveChildren(Concept concept, Pageable request) throws NotFoundException {
        if (serviceRegistry.getConceptServiceList() != null && serviceRegistry.getConceptServiceList().size() > 0) {
            Page<T> service = (Page<T>) serviceRegistry.getConceptServiceList()
                    .parallelStream()
                    .map(s -> {
                        try {
                            return s.getTransitiveChildren(concept, request);
                        } catch (NotFoundException e) {
                            return null;
                        }
                    }).filter(Objects::nonNull).filter(s -> s.getNumberOfElements() != 0)
                    .findFirst()
                    .orElse(Page.empty(request));

            return service;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Concept> Page<T> getRelatedConcepts(Concept concept, Pageable request) throws NotFoundException {
        if (serviceRegistry.getConceptServiceList() != null && serviceRegistry.getConceptServiceList().size() > 0) {
            Page<T> service = (Page<T>) serviceRegistry.getConceptServiceList()
                    .parallelStream()
                    .map(s -> {
                        try {
                            return s.getRelatedConcepts(concept, request);
                        } catch (NotFoundException e) {
                            return null;
                        }
                    }).filter(Objects::nonNull).filter(s -> s.getNumberOfElements() != 0)
                    .findFirst()
                    .orElse(Page.empty(request));

            return service;
        }
        return null;
    }

    @Override
    public Page<Mapping> getMappings(Concept concept, Mapping.MAPPING_TYPE type, Pageable request) throws NotFoundException {
        return null;
    }

    @Override
    public Page<Concept> getTopConceptOfTerminology(String terminology, Pageable request) throws NotFoundException {
        log.debug(" >>> getTop of {} from {}", terminology, serviceRegistry.getConceptServiceList());

        if (serviceRegistry.getConceptServiceList() != null && serviceRegistry.getConceptServiceList().size() > 0) {
            Page<Concept> service = (Page<Concept>) serviceRegistry.getConceptServiceList()
                    .parallelStream()
                    .map(s -> {
                        try {
                            return s.getTopConceptOfTerminology(terminology, request);
                        } catch (NotFoundException e) {
                            return null;
                        }
                    }).filter(Objects::nonNull).filter(s -> s.getNumberOfElements() != 0)
                    .findFirst()
                    .orElse(Page.empty(request));

            log.debug(" >>> got {} ", service.getNumberOfElements());
            return service;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Concept> Page<T> getConceptsOfTerminology(String terminology, Pageable request) throws NotFoundException {
        log.debug(" >>> concepts of {}, {}", terminology, serviceRegistry.getConceptServiceList());
        if (serviceRegistry.getConceptServiceList() != null && serviceRegistry.getConceptServiceList().size() > 0) {
            Page<T> service = (Page<T>) serviceRegistry.getConceptServiceList()
                    .parallelStream()
                    .map(s -> {
                        try {
                            return s.getConceptsOfTerminology(terminology, request);
                        } catch (NotFoundException e) {
                            return null;
                        }
                    }).filter(Objects::nonNull).filter(s -> s.getNumberOfElements() != 0)
                    .findFirst()
                    .orElse(Page.empty(request));
            return service;
        }
        return null;
    }

    @Override
    public <T extends Concept> Page<T> searchConceptExactByIRI(String id, Pageable request) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Concept> T getConcept(String iri) throws NotFoundException {
        if (serviceRegistry.getConceptServiceList() != null && serviceRegistry.getConceptServiceList().size() > 0) {
            Concept service = (Concept) serviceRegistry.getConceptServiceList()
                    .parallelStream()
                    .map(s -> {
                        try {
                            return s.getConcept(iri);
                        } catch (NotFoundException e) {
                            return null;
                        }
                    }).filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(""));
            return (T) service;
        }
        return null;
    }


    @Override
    public Page<Concept> suggestConceptInTerminology(String terminologyId, @NotNull @Valid String query, Pageable request) {
        Pageable fullRequest = PageRequest.of(0, 500);    // maximum number allowed

        long sum = 0l;
        List<Concept> req = new ArrayList<>();

        if (serviceRegistry.getConceptServiceList() != null && serviceRegistry.getConceptServiceList().size() > 0) {

            List<Page<Concept>> service = serviceRegistry.getConceptServiceList()
                    .stream().filter(s -> s instanceof ConceptSuggestService)
                    .map(s -> ((ConceptSuggestService) s).suggestConceptInTerminology(terminologyId, query, fullRequest))
                    .filter(Objects::nonNull).filter(s -> s.getNumberOfElements() != 0).collect(Collectors.toList());

            sum = service.parallelStream().mapToLong(s -> s.getTotalElements()).sum();

            List<Concept> allMatchedConcepts = service.parallelStream().map(Page::getContent)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            allMatchedConcepts.forEach(c1 -> {
                curieService.setCurieAndId(c1, c1.getLocalID(), terminologyId);
            });

            req = allMatchedConcepts.parallelStream()
                    .distinct()
                    .sequential()
                    .skip(request.getOffset())
                    .limit(request.getPageSize())
                    .collect(Collectors.toList());
        }

        return new PageImpl<Concept>(req, request, sum);
    }

    @Override
    public Page<Concept> suggestConcepts(String query, Pageable request) {

        Pageable fullRequest = PageRequest.of(0, 500);    // maximum number allowed in OLS

        long sum = 0l;
        List<Concept> req = new ArrayList<>();

        if (serviceRegistry.getConceptServiceList() != null && serviceRegistry.getConceptServiceList().size() > 0) {
            List<Page<Concept>> service = serviceRegistry.getConceptServiceList()
                    .stream().filter(s -> s instanceof ConceptSuggestService)
                    .map(s -> ((ConceptSuggestService) s).suggestConcepts(query, fullRequest))
                    .filter(Objects::nonNull).filter(s -> s.getNumberOfElements() != 0).collect(Collectors.toList());

            sum = service.parallelStream().mapToLong(s -> s.getTotalElements()).sum();

            List<Concept> allMatchedConcepts = service.parallelStream().map(Page::getContent)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            allMatchedConcepts.forEach(c1 -> {
                curieService.setCurieAndId(c1, c1.getLocalID(), c1.getDefiningTerminology());
            });

            // filter to correct page
            req = allMatchedConcepts.parallelStream()
                    .distinct()
                    .sequential()
                    .skip(request.getOffset())
                    .limit(request.getPageSize())
                    .collect(Collectors.toList());
        }

        return new PageImpl<Concept>(req, request, sum);
    }

    @Override
    public Page<Concept> searchConceptInTerminology(String terminology, EnumSet<SEARCH_FIELDS> searchFields, boolean exact, String query, Pageable request) {

        Pageable fullRequest = PageRequest.of(0, 500);    // maximum number allowed

        long sum = 0l;
        List<Concept> req = new ArrayList<>();

        if (serviceRegistry.getConceptServiceList() != null && serviceRegistry.getConceptServiceList().size() > 0) {
            List<Page<Concept>> service = serviceRegistry.getConceptServiceList()
                    .stream().filter(s -> s instanceof ConceptSearchService)
                    .map(s -> ((ConceptSearchService) s).searchConceptInTerminology(terminology, searchFields, exact, query, fullRequest))
                    .filter(Objects::nonNull).filter(s -> s.getNumberOfElements() != 0).collect(Collectors.toList());

            sum = service.parallelStream().mapToLong(s -> s.getTotalElements()).sum();

            List<Concept> allMatchedConcepts = service.parallelStream().map(Page::getContent)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            allMatchedConcepts.forEach(c1 -> {
                curieService.setCurieAndId(c1, c1.getLocalID(), terminology);
            });

            // filter to correct page
            req = allMatchedConcepts.parallelStream()
                    .distinct()
                    .sequential()
                    .skip(request.getOffset())
                    .limit(request.getPageSize())
                    .collect(Collectors.toList());
        }

        return new PageImpl<Concept>(req, request, sum);
    }

    @Override
    public Page<Concept> searchConcept(EnumSet<SEARCH_FIELDS> searchFields, boolean exact, String query, Pageable request) {

        Pageable fullRequest = PageRequest.of(0, 500);    // maximum number allowed
        long sum = 0l;
        List<Concept> req = new ArrayList<>();

        if (serviceRegistry.getConceptServiceList() != null && serviceRegistry.getConceptServiceList().size() > 0) {

            List<Page<Concept>> service = serviceRegistry.getConceptServiceList()
                    .stream().filter(s -> s instanceof ConceptSearchService)
                    .map(s -> ((ConceptSearchService) s).searchConcept(searchFields, exact, query, fullRequest))
                    .filter(Objects::nonNull).filter(s -> s.getNumberOfElements() != 0).collect(Collectors.toList());

            sum = service.parallelStream().mapToLong(s -> s.getTotalElements()).sum();

            List<Concept> allMatchedConcepts = service.parallelStream().map(Page::getContent)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            allMatchedConcepts.forEach(c1 -> { // TODO check if definingTerm is set correctly
                curieService.setCurieAndId(c1, c1.getLocalID(), c1.getDefiningTerminology());
            });

            req = allMatchedConcepts.parallelStream().
                    distinct().
                    sequential().
                    skip(request.getOffset()).
                    limit(request.getPageSize()).
                    collect(Collectors.toList());

        }

        return new PageImpl<Concept>(req, request, sum);
    }

    public Concept searchConcept(String terminologyId, Concept concept) throws NotFoundException, UnsupportedEncodingException {
        curieService.setCurieAndId(concept, concept.getLocalID(), terminologyId);
        return searchConcept(terminologyId, concept.getLocalID());
    }

    public Concept searchConcept(String terminologyId, String conceptId) throws NotFoundException, UnsupportedEncodingException {
        Concept concept = null;

        log.debug(" >>> search concept {} {}", terminologyId, conceptId);

        if (conceptId.startsWith("http")) {
            conceptId = URLDecoder.decode(conceptId, StandardCharsets.UTF_8.name());
        }

        // query specific ontology
        concept = getConcept(terminologyId, conceptId);

        // query as OBO ontology
        if (concept == null) {
            concept = getAsOboConcept(terminologyId, conceptId);
        }

        // query all ontologies as OBO ontology
        if (concept == null) {
            Page<Terminology> page = terminologyService.getAllTerminologies(PageRequest.of(0, (int) terminologyService.getCountOfTerminologies()));
            for (Terminology terminology : page.getContent()) {
                log.trace("trying {} ontology", terminology.getOlsID());
                concept = getAsOboConcept(terminology.getOlsID(), conceptId);
                if (concept != null) break;
            }
        }
        return concept;
    }

    private Concept getConcept(@ApiParam(value = "Identifier of the terminology.", required = true) @PathVariable("terminologyId") String terminologyId, @ApiParam(value = "Identifier of the concept.", required = true) @PathVariable("conceptId") String conceptId) throws NotFoundException {
        log.debug(" >> getConcept: Passed terminologyId {}, conceptId {}", terminologyId, conceptId);

        Concept concept = null;
        String iri = null;

        if (conceptId.startsWith("http://") || conceptId.startsWith("https://")) {
            concept = getConceptOfTerminology(terminologyId, conceptId);

        } else if (conceptId.contains(":")) {
            String[] parts = conceptId.split(":");
            if (parts.length > 2) {
                log.error("invalid CURIE {}", conceptId);
            } else {
                if (curieService.getPrefix(parts[0]) != null) {
                    iri = curieService.getPrefix(parts[0]) + parts[1];
                } else {
                    iri = conceptId;
                }
                concept = getConceptOfTerminology(terminologyId, iri);
            }
        }

        curieService.setCurieAndId(concept, conceptId, terminologyId);
        return concept;
    }

    public Concept getAsOboConcept(String terminologyId, String conceptId) {
        Concept concept = null;

        if (!conceptId.contains(":")) return concept;

        try {
            String iri = "http://purl.obolibrary.org/obo/" + conceptId.replaceFirst(":", "_");
            concept = getConcept(terminologyId, iri);
            if (concept != null) {
                concept.setIri(iri);
                concept.setDefiningTerminology(terminologyId);
            }
        } catch (NotFoundException ex) {
        }
        return concept;
    }

    /**
     * searches every concept in the page and calls <code>setCurieAndId</code>
     *
     * @param concepts
     * @param terminologyId
     */
    public void searchConcepts(Page<Concept> concepts, String terminologyId) {
        concepts.forEach(c1 -> {
            try {
                Concept c2 = searchConcept(terminologyId, c1);
                c1.setParent(c2.isParent());
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
                log.debug(e.getLocalizedMessage(), e);
            }
        });
    }

}
