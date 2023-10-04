package de.fraunhofer.scai.bio.sam.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * ConceptSuggestService
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 **/
public interface ConceptSuggestService {
    
    Page<Concept> suggestConceptInTerminology(String terminologyId, @NotNull @Valid String query, Pageable request);
    
    /**
     * Suggest Concepts bases on the given query.
     * This
     * @param query
     * @param request
     * @return
     */
    Page<Concept> suggestConcepts(@NotNull @Valid String query, Pageable request);
}
