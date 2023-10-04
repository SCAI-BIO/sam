package de.fraunhofer.scai.bio.sam.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.google.common.collect.Multimap;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.domain.DAO.Mapping;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;

public interface ConceptService {
    
    void saveConceptHierarchyRelations(Multimap<String,String> relationFromTo);
    void saveConcepts(String terminologyId, List<Concept> concepts);
    
    /**
     * Returns the Concept defined in @terminology with IRI @iri if present, otherwise throws @{@link NotFoundException}
     *
     * @param terminology
     * @param iri
     * @throws @{@link NotFoundException}
     * @return the retrieved Concept
     */
    <T extends Concept> T getConceptOfTerminology(String terminology, String iri) throws NotFoundException;
    
    // Concept hierarchy
    <T extends Concept>  Page<T> getDirectParents(Concept concept, Pageable request) throws NotFoundException;
    
    <T extends Concept> Page<T> getDirectChildren(Concept concept, Pageable request) throws NotFoundException;
    
    <T extends Concept> Page<T> getTransitiveChildren(Concept concept, Pageable request) throws NotFoundException;
    
    
    <T extends Concept> Page<T> getRelatedConcepts(Concept concept, Pageable request) throws NotFoundException;;
    
   boolean isParent(Concept concept) throws NotFoundException;

    
    /**
     * Returns all mappings of given type for the the given concept.
     * @param concept
     * @param type
     * @param request
     * @return
     */
    Page<Mapping> getMappings(Concept concept, Mapping.MAPPING_TYPE type, Pageable request ) throws NotFoundException;;

    Page<Concept> getTopConceptOfTerminology(String terminology, Pageable request) throws NotFoundException;
    // get all
    <T extends Concept> Page<T> getConceptsOfTerminology(String terminology, Pageable request) throws NotFoundException;
    
    // helper
    <T extends Concept> Page<T> searchConceptExactByIRI(String id, Pageable request);
    <T extends Concept> T getConcept(String iri) throws NotFoundException;
}
