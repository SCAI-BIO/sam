package de.fraunhofer.scai.bio.sam.controller.api.terminologies;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.domain.DTO.ErrorDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.PagedConceptMinDTO;
import de.fraunhofer.scai.bio.sam.service.ConceptSearchService;
import de.fraunhofer.scai.bio.sam.service.ConceptSearchService.SEARCH_FIELDS;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;
import de.fraunhofer.scai.bio.sam.service.impl.delegator.ConceptServiceDelegator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * ConceptSearchAndSuggestAPI
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 * @author Marc Jacobs <marc.jacobs@scai.fraunhofer.de>
 **/
@CrossOrigin(origins = "*")
@Controller
@Api(tags = {"retrieval"}, description = "Retrieve concepts from a Terminology.")
public  class ConceptSearchAndSuggestAPI {
    private Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ConceptServiceDelegator conceptSearchService;
        
    
    @ApiOperation(value = "Search for concepts", nickname = "terminologySearch", notes = "Returns all search results for your term/REGEX, paginated using page and size. Concept's IDs, names, labels and references are considered. Set exact=true to only recieve perfect matches.", response = PagedConceptMinDTO.class, tags={ "Terminology", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = PagedConceptMinDTO.class),
            @ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class) })
    @RequestMapping(value = "/api/v1/terminologies/{terminologyId}/concepts/search",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public  ResponseEntity<PagedConceptMinDTO> terminologySearch(
            @ApiParam(value = "Identifier of the terminology.",required=true) @PathVariable("terminologyId") String terminologyId,
            @NotNull @ApiParam(value = "Search term.", required = true) @Valid @RequestParam(value = "q", required = true) String q,
            @ApiParam(value = "Terminology version defined by tag-name") @Valid @RequestParam(value = "versionTag", required = false) String versionTag,
            @ApiParam(value = "This allows to filter the response to only contain descriptions in a specific language. If no description in the given language is present non is returned. The API expect a language to be encoded as defined in https://tools.ietf.org/html/bcp47. Further an extension 'all' is allowed. If that is given properties in all language are returned. The default language is 'en'.", defaultValue = "en") @Valid @RequestParam(value = "lang", required = false, defaultValue="en") String lang,
            @ApiParam(value = "The requested page", type="long", defaultValue = "1", example = "1") @Valid @RequestParam(value = "page", required = false, defaultValue="0") Long page,
            @ApiParam(value = "Size of pages", type="long", defaultValue = "10", example = "10") @Valid @RequestParam(value = "size", required = false, defaultValue="10") Long size,
            @ApiParam(value = "Only find exact matches?.",defaultValue="false") @Valid @RequestParam(value = "exact", required = false) Boolean exact,
            @ApiParam(value = "To limit results to a property, set field to ID/prefLabel/altLabel/description.", allowableValues = "ID, prefLabel, altLabel, description") @Valid @RequestParam(value= "field", required = false) String field) throws NotFoundException, UnsupportedEncodingException{
        
        EnumSet<ConceptSearchService.SEARCH_FIELDS> fields2 = null;
        if(fields2 == null || fields2.isEmpty()) {
            Collection<SEARCH_FIELDS> col = new ArrayList<SEARCH_FIELDS> ();        
            col.add(SEARCH_FIELDS.prefLabel);
            col.add(SEARCH_FIELDS.altLabels);
            fields2 = EnumSet.copyOf(col);
//            fields2 = EnumSet.copyOf(Arrays.asList(ConceptSearchService.SEARCH_FIELDS.values()));
        }
        
        log.info(" >> searching for {} in {}", q, terminologyId);
        Page<Concept> concepts = conceptSearchService.searchConceptInTerminology(terminologyId,fields2,exact,q, PageRequest.of(page.intValue(),size.intValue()));
        PagedConceptMinDTO pagedConceptMinDTO = new PagedConceptMinDTO(concepts);
    
        return new ResponseEntity<PagedConceptMinDTO>(pagedConceptMinDTO, HttpStatus.OK);
    }
        
    @ApiOperation(value = "Autocomplete concepts", nickname = "conceptAutocompletion", notes = "Returns all possible completions for your term, paginated using page and size. Concept's IDs, names, labels and references are considered.", response = PagedConceptMinDTO.class, tags={ "Terminology", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = PagedConceptMinDTO.class),
            @ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class) })
    @RequestMapping(value = "/api/v1/terminologies/concepts/autocomplete",
            produces = { "application/json" },
            method = RequestMethod.GET)
     public ResponseEntity<PagedConceptMinDTO> conceptAutocompletion(
    		 @NotNull @ApiParam(value = "Beginning of a term to be completed.", required = true) @Valid @RequestParam(value = "q", required = true) String q,
    		 @ApiParam(value = "Terminology version defined by tag-name") @Valid @RequestParam(value = "versionTag", required = false) String versionTag,
    		 @ApiParam(value = "This allows to filter the response to only contain descriptions in a specific language. If no description in the given language is present non is returned. The API expect a language to be encoded as defined in https://tools.ietf.org/html/bcp47. Further an extension 'all' is allowed. If that is given properties in all language are returned. The default language is 'en'.", defaultValue = "en") @Valid @RequestParam(value = "lang", required = false, defaultValue="en") String lang,
    		 @ApiParam(value = "The requested page", defaultValue = "0", example = "1") @Valid @RequestParam(value = "page", required = false, defaultValue="0") Long page,
    		 @ApiParam(value = "Size of pages", defaultValue = "25", example = "10") @Valid @RequestParam(value = "size", required = false, defaultValue="25") Long size) throws NotFoundException, UnsupportedEncodingException{

    	EnumSet<ConceptSearchService.SEARCH_FIELDS> fields2 = null;
        if(fields2 == null || fields2.isEmpty()) {
            Collection<SEARCH_FIELDS> col = new ArrayList<SEARCH_FIELDS> ();        
            col.add(SEARCH_FIELDS.prefLabel);
            col.add(SEARCH_FIELDS.altLabels);
            fields2 = EnumSet.copyOf(col);
//            fields2 = EnumSet.copyOf(Arrays.asList(ConceptSearchService.SEARCH_FIELDS.values()));
        }
        
        log.info(" >> autocomplete for {}", q);

        Page<Concept> concepts = conceptSearchService.suggestConcepts(q, PageRequest.of
                (page.intValue(),size.intValue()));
        log.debug("#concepts = " + concepts.getSize());
        PagedConceptMinDTO pagedConceptMinDTO = new PagedConceptMinDTO(concepts);
        return new ResponseEntity<PagedConceptMinDTO>(pagedConceptMinDTO,HttpStatus.OK);
    }

    @ApiOperation(value = "Autocomplete concepts in terminology", nickname = "terminologyAutocompletion", notes = "Returns all possible completions for your term in a terminology, paginated using page and size. Concept's IDs, names, labels and references are considered.", response = PagedConceptMinDTO.class, tags={ "Terminology", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = PagedConceptMinDTO.class),
            @ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class) })
    @RequestMapping(value = "/api/v1/terminologies/{terminologyId}/concepts/autocomplete",
            produces = { "application/json" },
            method = RequestMethod.GET)
     public ResponseEntity<PagedConceptMinDTO> terminologyAutocompletion(
    		 @ApiParam(value = "Identifier of the terminology.",required=true) @PathVariable("terminologyId") String terminologyId,
    		 @NotNull @ApiParam(value = "Beginning of a term to be completed.", required = true) @Valid @RequestParam(value = "q", required = true) String q,
    		 @ApiParam(value = "Terminology version defined by tag-name") @Valid @RequestParam(value = "versionTag", required = false) String versionTag,
    		 @ApiParam(value = "This allows to filter the response to only contain descriptions in a specific language. If no description in the given language is present non is returned. The API expect a language to be encoded as defined in https://tools.ietf.org/html/bcp47. Further an extension 'all' is allowed. If that is given properties in all language are returned. The default language is 'en'.", defaultValue = "en") @Valid @RequestParam(value = "lang", required = false, defaultValue="en") String lang,
    		 @ApiParam(value = "The requested page", defaultValue = "0", example = "1") @Valid @RequestParam(value = "page", required = false, defaultValue="0") Long page,
    		 @ApiParam(value = "Size of pages", defaultValue = "25", example = "10") @Valid @RequestParam(value = "size", required = false, defaultValue="25") Long size) throws NotFoundException, UnsupportedEncodingException{

    	EnumSet<ConceptSearchService.SEARCH_FIELDS> fields2 = null;
        if(fields2 == null || fields2.isEmpty()) {
            Collection<SEARCH_FIELDS> col = new ArrayList<SEARCH_FIELDS> ();        
            col.add(SEARCH_FIELDS.prefLabel);
            col.add(SEARCH_FIELDS.altLabels);
            fields2 = EnumSet.copyOf(col);
//            fields2 = EnumSet.copyOf(Arrays.asList(ConceptSearchService.SEARCH_FIELDS.values()));
        }
        
        log.info(" >> autocomplete for {} in {}", q, terminologyId);

        Page<Concept> concepts = conceptSearchService.suggestConceptInTerminology(terminologyId,q, PageRequest.of
                (page.intValue(),size.intValue()));
        log.debug("#concepts = " + concepts.getSize());
        PagedConceptMinDTO pagedConceptMinDTO = new PagedConceptMinDTO(concepts);
        return new ResponseEntity<PagedConceptMinDTO>(pagedConceptMinDTO,HttpStatus.OK);
    }
}
