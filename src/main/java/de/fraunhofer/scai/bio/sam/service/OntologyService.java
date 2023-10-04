package de.fraunhofer.scai.bio.sam.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.fraunhofer.scai.bio.sam.domain.DTO.OntologyDTO;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;


public interface OntologyService {
    
    OWLOntology getOntology(String id) throws NotFoundException;
    OntologyDTO getOntologyInfo(String id) throws NotFoundException;
    
    Page<OWLOntology> getAllOntologies(Pageable request);
    Page<String> getAllOntologyIDs(Pageable request);
    
    long getCountOfOntologies();
    boolean existsByID(String id);
    
    void loadOntology(String id, String iri, String lang) throws OWLOntologyCreationException;
    File saveOntology(String ontologyId, String format) throws IOException, NotFoundException, OWLOntologyStorageException;
    List<String> generateLanguageVersions(String id) throws OWLOntologyCreationException, NotFoundException;
    
}
