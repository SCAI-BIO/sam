package de.fraunhofer.scai.bio.sam.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.fraunhofer.scai.bio.sam.config.ItsProperties;
import de.fraunhofer.scai.bio.sam.config.JpmProperties;
import de.fraunhofer.scai.bio.sam.config.LoincProperties;
import de.fraunhofer.scai.bio.sam.config.OlsProperties;
import de.fraunhofer.scai.bio.sam.domain.DTO.ErrorDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.PagedOntologyDTO;
import de.fraunhofer.scai.bio.sam.service.ServiceRegistry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * ConfigAPI
 * <p>
 * TODO: Add javadoc
 *
 * @author Marc Jacobs
 **/
@CrossOrigin(origins = "*")
@Controller

@Api(tags = {"Config"}, description = "Retrieve properties from the configuration.")
public class ConfigAPI {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    OlsProperties olsProperties;
    
    @Autowired
    LoincProperties loincProperties;

    @Autowired
    ItsProperties itsProperties;
    
    @Autowired
    JpmProperties jpmProperties;
    
    @Autowired 
    ServiceRegistry serviceRegistry;

    @ApiOperation(value = "List the configuration", nickname = "listConfig", notes = "Returns the infomartion on the current configuration", response = String.class, 
            tags = {"Configuration"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = PagedOntologyDTO.class),
            @ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
            })
    @RequestMapping(value = "/api/v1/configuration/",
    produces = {"application/json"},
    method = RequestMethod.GET)
    
    public ResponseEntity<String> listConfig(
            ) {

        StringBuilder configInfo = new StringBuilder();
        
        try {
            configInfo.append(" - Terminology services: ");
            configInfo.append(serviceRegistry.getTerminologyServiceList());
            configInfo.append("\n");
            
            configInfo.append(" - Concept services: ");
            configInfo.append(serviceRegistry.getConceptServiceList()); 
            configInfo.append("\n");
            
            configInfo.append(" - OLS: ");
            if(olsProperties.isEnabled()) {
                configInfo.append("on\n");
                configInfo.append("  - URLs: ");
                configInfo.append(olsProperties.getUrl());
                configInfo.append("\n");
                
                configInfo.append("  - Max page size: ");
                configInfo.append(olsProperties.getMaxPageSize());
                configInfo.append("\n");
                                
            } else {
                configInfo.append("off\n");
            }
            
            configInfo.append(" - LOINC: ");
            if(loincProperties.isEnabled()) {
                configInfo.append("on\n");
                configInfo.append("  - URL: ");
                configInfo.append(loincProperties.getUrl());
                configInfo.append("\n");
                                
            } else {
                configInfo.append("off\n");
            }

            configInfo.append(" - Internal Terminologies: ");
            if(itsProperties.isEnabled()) {
                configInfo.append("on\n");
                configInfo.append("  - Terminologies: ");
                configInfo.append(itsProperties.getTerminologies());
                configInfo.append("\n");
                                
            } else {
                configInfo.append("off\n");
            }

        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(configInfo.toString(), HttpStatus.OK);
    }

}

