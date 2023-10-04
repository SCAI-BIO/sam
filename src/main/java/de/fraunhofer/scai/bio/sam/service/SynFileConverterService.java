package de.fraunhofer.scai.bio.sam.service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;

public interface SynFileConverterService {

    File convertResourceToSynFile(File resource, String storeDestination, String terminology,
                                  Optional<String> terminologyPrefixShortForm,
                                  String version,
                                  String idSourceColName,
                                  String prefLabelSourceColName,
                                  String descriptionSourceColName)
            throws IOException, NotFoundException;
}
