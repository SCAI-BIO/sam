package de.fraunhofer.scai.bio.sam.controller.api.terminologies;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.domain.DAO.Mapping;
import de.fraunhofer.scai.bio.sam.domain.DTO.ConceptDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.ConceptMinDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.ErrorDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.LabelDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.PagedConceptMinDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.PagedLabelDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.PagedMappingDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.PagedStringListDTO;
import de.fraunhofer.scai.bio.sam.service.CurieService;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;
import de.fraunhofer.scai.bio.sam.service.impl.delegator.ConceptServiceDelegator;
import de.fraunhofer.scai.bio.sam.service.impl.delegator.TerminologyServiceDelegator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * ConceptAPI
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 * @author Marc Jacobs
 **/
@CrossOrigin(origins = "*")
@Controller
@Api(tags = {"Concept"}, description = "Retrieve properties from a Concept.")
public class ConceptAPI {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ConceptServiceDelegator conceptService;
    
    @Autowired
    TerminologyServiceDelegator terminologyService;

    @Autowired
    CurieService curieService;

    @ApiOperation(value = "Get a list of all concepts", nickname = "conceptsList",
            notes =
            "Returns all concepts of a terminology. ",
            response =
            PagedConceptMinDTO.class, tags={ "retrieval", "Concept", "Terminology"})
    @GetMapping("/api/v1/terminologies/{terminologyId}/concepts/")
    public ResponseEntity<PagedConceptMinDTO> getConceptList(
            @ApiParam(value = "Identifier of the terminology.",required=true)
            @PathVariable("terminologyId") String terminologyId,
            @ApiParam(value = "Terminology version defined by tag-name")
            @Valid @RequestParam(value = "versionTag", required = false) String versionTag,
            @ApiParam(value = "The requested page", type="long", defaultValue = "0", example = "1")
            @Valid @RequestParam(value = "page", required = false, defaultValue="0") Long page,
            @ApiParam(value = "Size of pages", type="long", defaultValue = "10")
            @Valid @RequestParam(value = "size", required = false, defaultValue="10") Long size
            ) throws NotFoundException, UnsupportedEncodingException {

        terminologyId = terminologyService.searchTerminology(terminologyId, "");

        Page<Concept> concepts= conceptService.getConceptsOfTerminology(terminologyId, PageRequest.of(page.intValue(), size.intValue()));
        conceptService.searchConcepts(concepts, terminologyId);
        
        PagedConceptMinDTO pagedConceptMinDTO = new PagedConceptMinDTO(concepts);
        return new ResponseEntity<PagedConceptMinDTO>(pagedConceptMinDTO,HttpStatus.OK);
    }



    @ApiOperation(value = "Get a list of concepts without a sole broader concept", nickname = "conceptGetTop",
            notes =
            "Returns all concepts without a broader Concept. These are the top level of the hierarchy. ",
            response =
            PagedConceptMinDTO.class, tags={ "retrieval", "Concept", "Terminology"})
    @GetMapping("/api/v1/terminologies/{terminologyId}/concepts/top")
    public ResponseEntity<PagedConceptMinDTO> getTopConcepts(
            @ApiParam(value = "Identifier of the terminology.",required=true)
            @PathVariable("terminologyId") String terminologyId,
            @ApiParam(value = "Terminology version defined by tag-name")
            @Valid @RequestParam(value = "versionTag", required = false) String versionTag,
            @ApiParam(value = "The requested page", type="long", defaultValue = "0", example = "1")
            @Valid @RequestParam(value = "page", required = false, defaultValue="0") Long page,
            @ApiParam(value = "Size of pages", type="long", defaultValue = "10")
            @Valid @RequestParam(value = "size", required = false, defaultValue="10") Long size
            ) throws NotFoundException, UnsupportedEncodingException {

        terminologyId = terminologyService.searchTerminology(terminologyId, "");

        Page<Concept> concepts= conceptService.getTopConceptOfTerminology(terminologyId, PageRequest.of(page.intValue(), size.intValue()));
        conceptService.searchConcepts(concepts, terminologyId);
        PagedConceptMinDTO pagedConceptMinDTO = new PagedConceptMinDTO(concepts);
        return new ResponseEntity<PagedConceptMinDTO>(pagedConceptMinDTO,HttpStatus.OK);
    }

    @ApiOperation(value = "Get a concepts broader concepts", notes = "Returns all " +
            "direct broader concepts, being one hierarchical level above, paginated using page and size.", response = PagedConceptMinDTO.class, tags={ "retrieval", "Concept", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = PagedConceptMinDTO.class),
            @ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class) })
    @RequestMapping(value = "/api/v1/terminologies/{terminologyId}/concepts/{conceptId}/broader",
    produces = { "application/json" },
    method = RequestMethod.GET)
    public ResponseEntity<PagedConceptMinDTO> getBroaderConcepts(
            @ApiParam(value = "Identifier of the terminology.",required=true)
            @PathVariable("terminologyId") String terminologyId,
            @ApiParam(value = "Identifier of the concept.",required=true)
            @PathVariable("conceptId") String conceptId,
            @ApiParam(value = "Terminology version defined by tag-name")
            @Valid @RequestParam(value = "versionTag", required = false) String versionTag,
            @ApiParam(value = "The requested page", type="long", defaultValue = "0", example = "1" )
            @Valid @RequestParam(value = "page", required = false, defaultValue="0") Long page,
            @ApiParam(value = "Size of pages", type="long", defaultValue = "10")
            @Valid @RequestParam(value = "size", required = false, defaultValue="10") Long size) throws NotFoundException, UnsupportedEncodingException {

        terminologyId = terminologyService.searchTerminology(terminologyId, "");

        Concept concept = conceptService.searchConcept(terminologyId, conceptId);
        if(concept==null){
            return new ResponseEntity<PagedConceptMinDTO>(HttpStatus.NOT_FOUND);
        }
        Page<Concept> concepts= conceptService.getDirectParents(concept, PageRequest.of(page.intValue(), size.intValue()));
        conceptService.searchConcepts(concepts, terminologyId);
        PagedConceptMinDTO pagedConceptMinDTO = new PagedConceptMinDTO(concepts);
        return new ResponseEntity<PagedConceptMinDTO>(pagedConceptMinDTO,HttpStatus.OK);
    }

    @ApiOperation(value = "Get a concepts parents", notes = "Returns all " +
            "direct broader concepts in an ordered list, up to the root of the ontology (owl:thing), paginated using page and size.", response = PagedConceptMinDTO.class, tags={ "retrieval", "Concept", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = PagedConceptMinDTO.class),
            @ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class) })
    @RequestMapping(value = "/api/v1/terminologies/{terminologyId}/concepts/{conceptId}/parents",
    produces = { "application/json" },
    method = RequestMethod.GET)
    public ResponseEntity<PagedConceptMinDTO> getParentConcepts(
            @ApiParam(value = "Identifier of the terminology.",required=true)
            @PathVariable("terminologyId") String terminologyId,
            @ApiParam(value = "Identifier of the concept.",required=true)
            @PathVariable("conceptId") String conceptId,
            @ApiParam(value = "Terminology version defined by tag-name")
            @Valid @RequestParam(value = "versionTag", required = false) String versionTag,
            @ApiParam(value = "The requested page", type="long", defaultValue = "0", example = "1" )
            @Valid @RequestParam(value = "page", required = false, defaultValue="0") Long page,
            @ApiParam(value = "Size of pages", type="long", defaultValue = "10")
            @Valid @RequestParam(value = "size", required = false, defaultValue="10") Long size) throws NotFoundException, UnsupportedEncodingException {

        terminologyId = terminologyService.searchTerminology(terminologyId, "");

        Concept concept = conceptService.searchConcept(terminologyId, conceptId);
        if(concept==null){
            return new ResponseEntity<PagedConceptMinDTO>(HttpStatus.NOT_FOUND);
        }

        PagedConceptMinDTO pagedConceptMinDTO = new PagedConceptMinDTO();

        Page<Concept> concepts=null;
        int number = 0;
        int total = 0;

        do {
            // follow up on the first element only
            concepts= conceptService.getDirectParents(concept, PageRequest.of(0, 1));

            if(concepts.getTotalElements()>0) {
                Concept parent = concepts.getContent().get(0);	
                curieService.setCurieAndId(parent, parent.getLocalID(), terminologyId);
                parent.setParent(true);
                total++;
                if(total > (number+1)*size) number++;

                if(page*size < total && total <= (page+1)*size) {
                    pagedConceptMinDTO.addConcept(parent);
                }

                concept=parent;
            }
        } while (concepts.getTotalElements() > 0);

        pagedConceptMinDTO.number(page.intValue());
        pagedConceptMinDTO.totalElements(total);
        pagedConceptMinDTO.totalPages(number+1);

        return new ResponseEntity<PagedConceptMinDTO>(pagedConceptMinDTO,HttpStatus.OK);
    }

    @ApiOperation(value = "Get a concept's labels",  notes = "Returns all labels, paginated using page and size. Set lang to filter by language, returns all languages by default.", response = PagedLabelDTO.class, tags={ "retrieval", "Concept", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = PagedLabelDTO.class),
            @ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class) })
    @RequestMapping(value = "/api/v1/terminologies/{terminologyId}/concepts/{conceptId}/labels",
    produces = { "application/json" },
    method = RequestMethod.GET)
    public ResponseEntity<PagedLabelDTO> getLabelsOfConcept(
            @ApiParam(value = "Identifier of the terminology.", required=true)
            @PathVariable("terminologyId") String terminologyId,
            @ApiParam(value = "Identifier of the concept.",required=true)
            @PathVariable("conceptId") String conceptId,
            @ApiParam(value = "Terminology version defined by tag-name")
            @Valid @RequestParam(value = "versionTag", required = false) String versionTag,
            @ApiParam(value = "This allows to filter the response to only contain descriptions in a specific language. If no description in the given language is present non is returned. The API expect a language to be encoded as defined in https://tools.ietf.org/html/bcp47. Further an extension 'all' is allowed. If that is given properties in all language are returned. The default language is 'en'.", defaultValue = "en")
            @Valid @RequestParam(value = "lang", required = false, defaultValue="") String lang,
            @ApiParam(value = "The requested page", type="long", defaultValue = "0", example = "1")
            @Valid @RequestParam(value = "page", required = false, defaultValue="0") Long page,
            @ApiParam(value = "Size of pages", type="long", defaultValue = "10")
            @Valid @RequestParam(value = "size", required = false, defaultValue="10") Long size)
                    throws NotFoundException, UnsupportedEncodingException {

        terminologyId = terminologyService.searchTerminology(terminologyId, lang);

        Concept concept = conceptService.searchConcept(terminologyId, conceptId);
        if(concept==null){
            return new ResponseEntity<PagedLabelDTO>(HttpStatus.NOT_FOUND);
        }

        PagedLabelDTO pagedLabelDTO= new PagedLabelDTO();
        pagedLabelDTO.setSize((int)size.intValue());
        pagedLabelDTO.setNumber(0);
        pagedLabelDTO.setTotalPages(1);
        //@TODO size correct!
        pagedLabelDTO.setTotalElements((int) concept.getAltLabels().size()+1);

        int i=0;
        for( String lable : concept.getAltLabels()){
            i++;
            if(i>size-1){
                break;
            }
            LabelDTO labelDTO= new LabelDTO();
            labelDTO.setName(lable);
            labelDTO.setLang(lang);
            pagedLabelDTO.addContentItem(labelDTO);
        }
        LabelDTO labelDTO= new LabelDTO();
        labelDTO.setName(concept.getPrefLabel());
        labelDTO.setLang(lang);
        pagedLabelDTO.addContentItem(labelDTO);
        pagedLabelDTO.setNumberOfElements(pagedLabelDTO.getContent().size());
        return new ResponseEntity<PagedLabelDTO>(pagedLabelDTO,HttpStatus.OK);
    }


    @ApiOperation(value = "Get a concept's mappings",  notes = "Returns all mappings, paginated using page and size. Set type to filter by mapping type. Set sort to sort by referenced concepts.", response = PagedMappingDTO.class, tags={ "retrieval", "Concept", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = PagedMappingDTO.class),
            @ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class) })
    @RequestMapping(value = "/api/v1/terminologies/{terminologyId}/concepts/{conceptId}/mappings",
    produces = { "application/json" },
    method = RequestMethod.GET)
    public ResponseEntity<PagedMappingDTO> getMappingsOfConcept(
            @ApiParam(value = "Identifier of the terminology.",required=true)
            @PathVariable("terminologyId") String terminologyId,
            @ApiParam(value = "Identifier of the concept.",required=true)
            @PathVariable("conceptId") String conceptId,
            @ApiParam(value = "Terminology version defined by tag-name")
            @Valid @RequestParam(value = "versionTag", required = false) String versionTag,
            @ApiParam(value = "The requested page", type="long", defaultValue = "0", example = "1")
            @Valid @RequestParam(value = "page", required = false, defaultValue="0") Long page,
            @ApiParam(value = "Size of pages", type="long", defaultValue = "10")
            @Valid @RequestParam(value = "size", required = false, defaultValue="10") Long size,
            @ApiParam(value = "", allowableValues = "exactMatch, closeMatch, relatedMatch")
            @Valid @RequestParam(value = "type", required = false) String type) throws NotFoundException, UnsupportedEncodingException {

        terminologyId = terminologyService.searchTerminology(terminologyId, "");

        Concept concept = conceptService.searchConcept(terminologyId, conceptId);
        if(concept==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Page<Mapping> mappings= conceptService.getMappings(concept, Mapping.MAPPING_TYPE.CLOSE,PageRequest.of(page.intValue(), size.intValue
                ()));

        if(mappings ==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            PagedMappingDTO pagedConceptMinDTO = new PagedMappingDTO(concept,mappings);
            return new ResponseEntity<>(pagedConceptMinDTO,HttpStatus.OK);
        }
    }


    @ApiOperation(value = "Get a concept's meta information",  notes = "Returns a concept's meta information and a selection of its altLabels and direct broader/narrower concepts. Set examples to specify the amount returned. Also includes links to its related concepts, its direct broader and narrower, as well as all of its transitive broader and narrower concepts.", response = ConceptDTO.class, tags={ "retrieval", "Concept",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = ConceptDTO.class),
            @ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class) })
    @RequestMapping(value = "/api/v1/terminologies/{terminologyId}/concepts/{conceptId}",
    produces = { "application/json" },
    method = RequestMethod.GET)
    public ResponseEntity<ConceptDTO> getConcept(
            @ApiParam(value = "Identifier of the terminology.",required=true)
            @PathVariable("terminologyId") String terminologyId,
            @ApiParam(value = "Identifier of the concept.",required=true)
            @PathVariable("conceptId") String conceptId,
            @ApiParam(value = "Terminology version defined by tag-name")
            @Valid @RequestParam(value = "versionTag", required = false) String versionTag,
            @ApiParam(value = "This allows to filter the response to only contain descriptions in a specific language. If no description in the given language is present non is returned. The API expect a language to be encoded as defined in https://tools.ietf.org/html/bcp47. Further an extension 'all' is allowed. If that is given properties in all language are returned. The default language is 'en'.", defaultValue = "en")
            @Valid @RequestParam(value = "lang", required = false, defaultValue="") String lang,
            @ApiParam(value = "ID mode of the response", allowableValues = "BEL, PURL, CURIE")
            @Valid @RequestParam(value = "idMode", required = false) String idMode) throws NotFoundException, UnsupportedEncodingException {

        log.info(" >> searching for {} in {}", conceptId, terminologyId);
        terminologyId = terminologyService.searchTerminology(terminologyId, lang);
        
        Concept concept = conceptService.searchConcept(terminologyId, conceptId);

        if(concept == null) {
            return new ResponseEntity<ConceptDTO>(HttpStatus.NOT_FOUND);
        }

        ConceptDTO conceptDTO = new ConceptDTO(concept, concept.getLocalID(), lang);
        for (Concept c1 :conceptService.getDirectChildren(concept, PageRequest.of(0, 5))) {
            ConceptMinDTO c2 = new ConceptMinDTO(
                    conceptService.searchConcept(terminologyId, c1), lang
                    );
            conceptDTO.addExcerptNarrowerItem(c2);
        }
        for (Concept c1 :conceptService.getDirectParents(concept, PageRequest.of(0, 5))){
            ConceptMinDTO c2 = new ConceptMinDTO(
                    conceptService.searchConcept(terminologyId, c1), lang
                    );
            conceptDTO.addExcerptBroaderItem(c2);
        }
        return new ResponseEntity<ConceptDTO>(conceptDTO,HttpStatus.OK);

    }

    @ApiOperation(value = "Get a concept's narrower concepts", notes = "Returns all direct narrower concepts, being one hierarchical level below, paginated using page and size. Set transitive=true to also get all indirectly narrower concepts of even lower hierarchical levels.", response = PagedConceptMinDTO.class, tags={ "retrieval", "Concept",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = PagedConceptMinDTO.class),
            @ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class) })
    @RequestMapping(value = "/api/v1/terminologies/{terminologyId}/concepts/{conceptId}/narrower",
    produces = { "application/json" },
    method = RequestMethod.GET)
    public ResponseEntity<PagedConceptMinDTO> getNarrowerConcepts(
            @ApiParam(value = "Identifier of the terminology.",required=true)
            @PathVariable("terminologyId") String terminologyId,
            @ApiParam(value = "Identifier of the concept.",required=true)
            @PathVariable("conceptId") String conceptId,
            @ApiParam(value = "Terminology version defined by tag-name") @Valid
            @RequestParam(value = "versionTag", required = false) String versionTag,
            @ApiParam(value = "The requested page", type="long", defaultValue = "0", example = "1") @Valid
            @RequestParam(value = "page", required = false, defaultValue="0") Long page,
            @ApiParam(value = "Size of pages", type="long", defaultValue = "10") @Valid
            @RequestParam(value = "size", required = false, defaultValue="10") Long size,
            @ApiParam(value = "Include transitive narrower concepts?", defaultValue = "false")
            @Valid @RequestParam(value = "transitive", required = false, defaultValue="false") Boolean transitive)
                    throws NotFoundException, UnsupportedEncodingException {

        terminologyId = terminologyService.searchTerminology(terminologyId, "");

        Concept concept = conceptService.searchConcept(terminologyId, conceptId);
        if(concept==null){
            return new ResponseEntity<PagedConceptMinDTO>(HttpStatus.NOT_FOUND);
        }

        Page<Concept> concepts;

        if(transitive) {
            concepts = conceptService.getTransitiveChildren(concept,PageRequest.of(page.intValue(), size.intValue()));        	
        } else {
            concepts = conceptService.getDirectChildren(concept,PageRequest.of(page.intValue(), size.intValue()));
        }

        conceptService.searchConcepts(concepts, terminologyId);
        PagedConceptMinDTO pagedConceptMinDTO = new PagedConceptMinDTO(concepts);
        return new ResponseEntity<PagedConceptMinDTO>(pagedConceptMinDTO,HttpStatus.OK);
    }


    @ApiOperation(value = "Get a concept's related concepts", notes = "Returns all direct related concepts, paginated using page and size.", response = PagedConceptMinDTO.class, tags={ "retrieval", "Concept",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = PagedConceptMinDTO.class),
            @ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class) })
    @RequestMapping(value = "/api/v1/terminologies/{terminologyId}/concepts/{conceptId}/relateds",
    produces = { "application/json" },
    method = RequestMethod.GET)
    public ResponseEntity<PagedConceptMinDTO> getRelatedConcepts(
            @ApiParam(value = "Identifier of the terminology.",required=true)
            @PathVariable("terminologyId") String terminologyId,
            @ApiParam(value = "Identifier of the concept.",required=true)
            @PathVariable("conceptId") String conceptId,
            @ApiParam(value = "Terminology version defined by tag-name")
            @Valid @RequestParam(value = "versionTag", required = false) String versionTag,
            @ApiParam(value = "The requested page", type="long", defaultValue = "0", example = "1")
            @Valid @RequestParam(value = "page", required = false, defaultValue="0") Long page,
            @ApiParam(value = "Size of pages", type="long", defaultValue = "10")
            @Valid @RequestParam(value = "size", required = false, defaultValue="10") Long size) throws NotFoundException, UnsupportedEncodingException {

        terminologyId = terminologyService.searchTerminology(terminologyId, "");

        Concept concept = conceptService.searchConcept(terminologyId, conceptId);
        if(concept==null){
            return new ResponseEntity<PagedConceptMinDTO>(HttpStatus.NOT_FOUND);
        }

        Page<Concept> concepts= conceptService.getRelatedConcepts(concept,PageRequest.of(page.intValue(), size.intValue()));
        conceptService.searchConcepts(concepts, terminologyId);
        PagedConceptMinDTO pagedConceptMinDTO = new PagedConceptMinDTO(concepts);
        return new ResponseEntity<PagedConceptMinDTO>(pagedConceptMinDTO,HttpStatus.OK);
    }    

    @ApiOperation(value = "Get a concept's narrower descriptors", notes = "Returns all descriptors of narrower concepts, being in a hierarchical level below, paginated using page and size. ", response = PagedConceptMinDTO.class, tags={ "retrieval", "Concept",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = PagedConceptMinDTO.class),
            @ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class) })
    @RequestMapping(value = "/api/v1/terminologies/{terminologyId}/concepts/{conceptId}/descriptors",
    produces = { "application/json" },
    method = RequestMethod.GET)
    public ResponseEntity<PagedStringListDTO> getNarrowerDescriptors(
            @ApiParam(value = "Identifier of the terminology.",required=true)
            @PathVariable("terminologyId") String terminologyId,
            @ApiParam(value = "Identifier of the concept.",required=true)
            @PathVariable("conceptId") String conceptId,
            @ApiParam(value = "Terminology version defined by tag-name") @Valid
            @RequestParam(value = "versionTag", required = false) String versionTag,
            @ApiParam(value = "The requested page", type="long", defaultValue = "0", example = "1") @Valid
            @RequestParam(value = "page", required = false, defaultValue="0") Long page,
            @ApiParam(value = "Size of pages", type="long", defaultValue = "10") @Valid
            @RequestParam(value = "size", required = false, defaultValue="10") Long size
            )
                    throws NotFoundException, UnsupportedEncodingException {

        terminologyId = terminologyService.searchTerminology(terminologyId, "");

        Concept concept = conceptService.searchConcept(terminologyId, conceptId);
        if(concept==null) {
            return new ResponseEntity<PagedStringListDTO>(HttpStatus.NOT_FOUND);
        }

        List<String> descriptors = new ArrayList<String>();
        int totalElements = 0;
        int totalPages = 1;

        Page<Concept> concepts = conceptService.getTransitiveChildren(concept,PageRequest.of(0, 500));

        if(concepts != null) {
            for(int current = 0; current<concepts.getTotalPages(); current++) {
                for(Concept c1 : concepts) {
                    Map<String, List<String>> annotations = c1.getAnnotations();
                    if(annotations != null && annotations.containsKey("descriptor")) {
                        List<String> descList = annotations.get("descriptor");

                        while(!descList.isEmpty()) {
                            String desc = descList.remove(0);
                            if(totalElements >= page*size && totalElements < (page+1)*size) {
                                descriptors.add(desc);
                            }
                            totalElements++;
                        }

                        if(totalElements>totalPages*size) {
                            totalPages++;
                        }
                    }
                }
                concepts = conceptService.getTransitiveChildren(concept,PageRequest.of(current+1, 500));
            }
        }

        PagedStringListDTO descriptorPage =  new PagedStringListDTO();
        descriptorPage.setNumber(page.intValue());
        descriptorPage.setNumberOfElements(descriptors.size());
        descriptorPage.setTotalPages(totalPages);
        descriptorPage.setTotalElements(totalElements);
        descriptorPage.setSize(size.intValue());
        descriptorPage.setContent(descriptors);


        return new ResponseEntity<PagedStringListDTO>(descriptorPage,HttpStatus.OK);
    }

    @ApiOperation(value = "Get a concept's narrower leaves", notes = "Returns all leaf narrower concepts, being in the hierarchy below, paginated using page and size. ", response = PagedConceptMinDTO.class, tags={ "retrieval", "Concept",  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", response = PagedConceptMinDTO.class),
            @ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class) })
    @RequestMapping(value = "/api/v1/terminologies/{terminologyId}/concepts/{conceptId}/bottom",
    produces = { "application/json" },
    method = RequestMethod.GET)
    public ResponseEntity<PagedConceptMinDTO> getBottomConcepts(
            @ApiParam(value = "Identifier of the terminology.",required=true)
            @PathVariable("terminologyId") String terminologyId,
            @ApiParam(value = "Identifier of the concept.",required=true)
            @PathVariable("conceptId") String conceptId,
            @ApiParam(value = "Terminology version defined by tag-name") @Valid
            @RequestParam(value = "versionTag", required = false) String versionTag,
            @ApiParam(value = "The requested page", type="long", defaultValue = "0", example = "1") @Valid
            @RequestParam(value = "page", required = false, defaultValue="0") Long page,
            @ApiParam(value = "Size of pages", type="long", defaultValue = "10", example = "25") @Valid
            @RequestParam(value = "size", required = false, defaultValue="10") Long size
            )
                    throws NotFoundException, UnsupportedEncodingException {

        terminologyId = terminologyService.searchTerminology(terminologyId, "");

        Concept concept = conceptService.searchConcept(terminologyId, conceptId);
        if(concept==null) {
            return new ResponseEntity<PagedConceptMinDTO>(HttpStatus.NOT_FOUND);
        }

        PagedConceptMinDTO pagedConceptMinDTO = new PagedConceptMinDTO();

        Page<Concept> concepts = conceptService.getTransitiveChildren(concept,PageRequest.of(0, 500));
        int totalElements = 0;
        int totalPages = 1;

        if(concepts != null) {
            for(int current = 0; current<concepts.getTotalPages(); current++) {
                for(Concept c1 : concepts) {

                    curieService.setCurieAndId(c1, c1.getLocalID(), terminologyId);

                    if(!c1.isParent()) {
                        if(totalElements >= page*size && totalElements < (page+1)*size) {
                            pagedConceptMinDTO.addConcept(c1);
                        }
                        totalElements++;
                    }

                    if(totalElements>totalPages*size) {
                        totalPages++;
                    }
                }

                // fetch next page
                concepts = conceptService.getTransitiveChildren(concept,PageRequest.of(current+1, 500));
            }
        }

        pagedConceptMinDTO.setTotalElements(totalElements);
        pagedConceptMinDTO.setTotalPages(totalPages);

        return new ResponseEntity<PagedConceptMinDTO>(pagedConceptMinDTO,HttpStatus.OK);
    }


}
