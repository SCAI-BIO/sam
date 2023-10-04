package de.fraunhofer.scai.bio.sam.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.fraunhofer.scai.bio.sam.domain.DAO.Terminology;
import de.fraunhofer.scai.bio.sam.domain.DAO.VersionTag;


public interface TerminologyService {
    
    void save(Terminology t);


    Terminology getTerminology(String id);
    
    Page<Terminology> getAllTerminologies( Pageable request);
    long getCountOfTerminologies();
    boolean existsByID(String id);
    
    /**
     * Returns the most recent versionTag.  of the most recently created version.
     * If versions yet have to be created, returns null!
     */
    VersionTag getLastVersionTag(String terminologyId);
}
