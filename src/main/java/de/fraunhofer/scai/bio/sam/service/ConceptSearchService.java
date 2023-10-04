package de.fraunhofer.scai.bio.sam.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;

import java.util.EnumSet;

/**
 * ConceptSearchService
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 **/
public interface ConceptSearchService {
    
    enum SEARCH_FIELDS {
        iri,
        prefLabel,
        altLabels,
        description
    }
    
    Page<Concept> searchConceptInTerminology(String terminology, EnumSet<SEARCH_FIELDS> searchFields, boolean exact,
                                             String query, Pageable request);
    
    //search
    Page<Concept> searchConcept(EnumSet<SEARCH_FIELDS> searchFields, boolean exact, String query,
                                Pageable request);
}
