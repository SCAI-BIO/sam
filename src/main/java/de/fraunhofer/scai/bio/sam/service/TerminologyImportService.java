package de.fraunhofer.scai.bio.sam.service;

import java.io.File;
import java.io.IOException;

import de.fraunhofer.scai.bio.sam.service.exceptions.InvalidDataException;

/**
 * TerminologyImportServiceImpl
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 **/
public interface TerminologyImportService {
    
    /**
     *
     * @param terminologyId     curie prefix
     * @param versionTag        version
     * @param fileToImport      TTL File with a SKOS Model
     * @throws IOException
     * @throws InvalidDataException
     */
    void importTerminology(String terminologyId, String versionTag, File fileToImport) throws IOException,
            InvalidDataException;
}
