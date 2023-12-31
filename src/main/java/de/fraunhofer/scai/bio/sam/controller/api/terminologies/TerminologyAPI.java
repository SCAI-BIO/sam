/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package de.fraunhofer.scai.bio.sam.controller.api.terminologies;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import java.io.File;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TreeSet;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import de.fraunhofer.scai.bio.sam.domain.DAO.Terminology;
import de.fraunhofer.scai.bio.sam.domain.DTO.DescriptionDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.ErrorDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.LabelDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.PagedLabelDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.PagedPrefixDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.PagedTerminologyDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.PagedVersionDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.PrefixDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.TerminologyDTO;
import de.fraunhofer.scai.bio.sam.domain.DTO.VersionTagDTO;
import de.fraunhofer.scai.bio.sam.service.ConceptService;
import de.fraunhofer.scai.bio.sam.service.CurieService;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;
import de.fraunhofer.scai.bio.sam.service.impl.ProminerExportService;
import de.fraunhofer.scai.bio.sam.service.impl.delegator.TerminologyServiceDelegator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

/**
 * TerminologyAPI
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 **/
@CrossOrigin(origins = "*")
@Controller
@Api(tags = {"Terminology"}, description = "Retrieve properties from a Terminology.")
public class TerminologyAPI {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	TerminologyServiceDelegator terminologyService;

	@Autowired
	ProminerExportService prominerExportService;

	@Autowired
	ConceptService conceptService;

	@Autowired
	CurieService curieService;
	
	@Deprecated
	@ApiOperation(value = "Retrieve all versions of a Terminology.", nickname = "getVersionList", notes = "Returns all version tags of the specified terminology, paginated using page and size.", response = PagedVersionDTO.class, tags = {"retrieval",})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful response", response = PagedVersionDTO.class),
			@ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class)})
	@RequestMapping(value = "/api/v1/terminologies/{terminologyId}/versionTags",
	produces = {"application/json"},
	method = RequestMethod.GET)
	ResponseEntity<PagedVersionDTO> getVersionList(@ApiParam(value = "Identifier of the terminology.", required = true) @PathVariable("terminologyId") String terminologyId, @ApiParam(value = "The requested page", defaultValue = "1") @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Long page, @ApiParam(value = "Size of pages", defaultValue = "10") @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
		PagedVersionDTO pagedVersionDTO = new PagedVersionDTO();
		pagedVersionDTO.setSize(size.intValue());
		pagedVersionDTO.setNumber(0);
		pagedVersionDTO.setTotalPages(1);
		pagedVersionDTO.setTotalElements(1);
		pagedVersionDTO.setNumberOfElements(1);
		VersionTagDTO versionTagDTO = new VersionTagDTO();
		versionTagDTO.setShortName("LATEST");
		DescriptionDTO descriptionDTO = new DescriptionDTO();
		descriptionDTO.setDescription("LATEST VERSION");
		descriptionDTO.setLang("");
		versionTagDTO.setDescription(descriptionDTO);
		versionTagDTO.setTimestamp(OffsetDateTime.now());
		pagedVersionDTO.addContentItem(versionTagDTO);
		return new ResponseEntity<PagedVersionDTO>(pagedVersionDTO, HttpStatus.OK);

	}

	@ApiOperation(value = "Retrieve all indexed ontology prefixes.", nickname = "getPrefixList", notes = "Returns all prefixes that are used in CURIEs, paginated using page and size.", response = PagedVersionDTO.class, tags = {"retrieval",})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful response", response = PagedPrefixDTO.class),
			@ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
			@ApiResponse(code = 404, message = "List not found.", response = ErrorDTO.class)})
	@RequestMapping(value = "/api/v1/terminologies/prefixes",
	produces = {"application/json"},
	method = RequestMethod.GET)
	ResponseEntity<PagedPrefixDTO> getPrefixList(
			@ApiParam(value = "The requested page", defaultValue = "0", example = "1") @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Long page, 
			@ApiParam(value = "Size of pages", defaultValue = "10", example = "10") @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Long size) {
		TreeSet<String> sorted = new TreeSet<String>(curieService.getPrefixes().keySet());

		PagedPrefixDTO pagedVersionDTO = new PagedPrefixDTO();
		pagedVersionDTO.setSize(size.intValue());
		pagedVersionDTO.setNumber(page.intValue());
		pagedVersionDTO.setTotalPages(sorted.size()/size.intValue()+1);
		pagedVersionDTO.setTotalElements(sorted.size());
		pagedVersionDTO.setNumberOfElements(1);

		int i=0, j=0;
		for(String prefix : sorted) {
			if(i>=page*size && i<page*size+size) {
				PrefixDTO prefixDTO = new PrefixDTO();
				prefixDTO.setIri(curieService.getPrefixes().get(prefix));
				prefixDTO.setPrefix(prefix);
				pagedVersionDTO.addContentItem(prefixDTO);
				j++;
			}
			i++;
			if(i==page*size+size) break;
		}
		pagedVersionDTO.setNumberOfElements(j);


		return new ResponseEntity<PagedPrefixDTO>(pagedVersionDTO, HttpStatus.OK);

	}


	@ApiOperation(value = "Add new prefixes to the aplication.", nickname = "addPrefixList", notes = "Add new prefixes to the aplication in memory. These will not be persisted. Returns the number of newly added prefixes.")
	@PostMapping(value = "/api/v1/terminologies/prefixes")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Prefix created.",response = Integer.class),
			@ApiResponse(code = 200, message = "Successful response", response = Integer.class),
			@ApiResponse(code = 400, message = "Bad Client Request", response = Integer.class),
			@ApiResponse(code = 503, message = "CURIE service unavailable.", response = Integer.class)
	})
	public ResponseEntity<Integer> addPrefixToIndex(@RequestBody List<PrefixDTO> prefixes)  {

		if(curieService == null) {
			return new ResponseEntity<Integer>(0, HttpStatus.SERVICE_UNAVAILABLE);
		}

		int nofPrefixes = curieService.getPrefixes().size();

		prefixes.forEach(prefix -> {
			if(prefix != null 
					&& prefix.getPrefix() != null
					&& prefix.getIri() != null) {
				curieService.getPrefixes().put(prefix.getPrefix(), prefix.getIri());
			}
		});

		return new ResponseEntity<Integer>(curieService.getPrefixes().size()-nofPrefixes, HttpStatus.CREATED);
	}

	@ApiOperation(value = "Export a terminology.",
			notes = "Set format to specify export format. A file will be generated in the selected format and stored to be later fetched (since this may take some while)",
					tags = {"export",})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok.",response = String.class)
	})
	@GetMapping(value = "/api/v1/terminologies/{terminologyId}/export/{format}", produces = MediaType.TEXT_PLAIN_VALUE )
	ResponseEntity<String> export(
			@ApiParam(value = "Identifier of the terminology.", required = true) @PathVariable("terminologyId") String terminologyId,
			@ApiParam(value = "Options are jpm-syn file.", required = true, allowableValues = "syn") @PathVariable("format") String format,
			@ApiParam(value = "Terminology version defined by tag-name") @Valid @RequestParam(value = "versionTag", required = false) String versionTag,
			@ApiParam(value = "Split in sub ontologies?", required = false, defaultValue = "false") @Valid @RequestParam(value = "split", required = false, defaultValue = "false") Boolean split)
					throws Exception {
				
		String version= (versionTag==null || versionTag.trim().isEmpty()) ? "LATEST" : versionTag;
		String filename= "export_"+terminologyId+"_"+version+"_"+ OffsetDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);

		String id = null;
		
		if(format!=null && !format.trim().isEmpty() && format.equals("syn")){
			id = prominerExportService.schedule(terminologyId,version,filename, split);
		} else {
			return (new ResponseEntity<String>("invalid", HttpStatus.BAD_REQUEST));
		}

		return (new ResponseEntity<String>(id, HttpStatus.ACCEPTED));
	}

	@ApiOperation(value = "Fetch an exported terminology.",
			notes = "A generated file will be returned in the selected format.",
					tags = {"export",})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok.",response = File.class)
	})
	@GetMapping(value = "/api/v1/terminologies/fetch/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
	HttpEntity<FileSystemResource> fetch(
			@ApiParam(value = "Identifier of the file.", required = true) @PathVariable("id") String fileId)
					throws Exception {

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		header.setContentLength(-1);

		File export= prominerExportService.fetch(fileId);
		if(export != null) {
			header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + export.getName());
			header.setContentLength(export.length());
		}
						
		return new HttpEntity<FileSystemResource>(new FileSystemResource(export), header);
	}


	@ApiOperation(value = "Get all terminologies", nickname = "list", notes = "Returns a terminology's meta information, including a link to the original source and a link to the local refined version, if present. Also returns links to changes and versionTags.", response = PagedTerminologyDTO.class, tags = {"retrieval",})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful response", response = PagedTerminologyDTO.class),
			@ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class)})
	@RequestMapping(value = "/api/v1/terminologies/",
	produces = {"application/json"},
	method = RequestMethod.GET)
	ResponseEntity<PagedTerminologyDTO> list(
			@ApiParam(value = "Terminology version defined by tag-name") @Valid @RequestParam(value = "versionTag", required = false) String versionTag, 
			@ApiParam(value = "This allows to filter the response to only contain descriptions in a specific language. If no description in the given language is present non is returned. The API expect a language to be encoded as defined in https://tools.ietf.org/html/bcp47. Further an extension 'all' is allowed. If that is given properties in all language are returned. The default language is empty.", defaultValue = "") @Valid @RequestParam(value = "lang", required = false, defaultValue = "") String lang, 
			@ApiParam(value = "The requested page", defaultValue = "0", example = "1") @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Long page, 
			@ApiParam(value = "Size of pages", defaultValue = "10", example = "10") @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Long size
			) {

		Page<Terminology> terminologies = terminologyService.getAllTerminologies(PageRequest.of(page.intValue(), size.intValue()));
		PagedTerminologyDTO pagedTerminologyDTO = new PagedTerminologyDTO();
		pagedTerminologyDTO.setSize(terminologies.getSize());
		pagedTerminologyDTO.setNumber(terminologies.getNumber());
		pagedTerminologyDTO.setTotalPages(terminologies.getTotalPages());
		pagedTerminologyDTO.setTotalElements((int) terminologies.getTotalElements());
		pagedTerminologyDTO.setNumberOfElements(terminologies.getNumberOfElements());

		for (Terminology terminology : terminologies.getContent()) {
			TerminologyDTO terminologyDTO = new TerminologyDTO();
			terminologyDTO.setBaseURL(terminology.getIri());
			terminologyDTO.setTerminologyID(terminology.getOlsID());
			terminologyDTO.setLongName(terminology.getLongName());
			DescriptionDTO descriptionDTO = new DescriptionDTO();
			descriptionDTO.setDescription(terminology.getDescription());
			descriptionDTO.setLang(lang);
			terminologyDTO.setDescriptions(descriptionDTO);
			UriComponents uriComponents = MvcUriComponentsBuilder
					.fromMethodCall(on(TerminologyAPI.class).getVersionList(terminology.getOlsID(), (long) 0, (long) 10)).build();

			URI uri = uriComponents.encode().toUri();
			terminologyDTO.setVersiontags(uri.toString());
			pagedTerminologyDTO.addContentItem(terminologyDTO);
		}

		return new ResponseEntity<PagedTerminologyDTO>(pagedTerminologyDTO, HttpStatus.OK);
	}


	@ApiOperation(value = "Get a terminology's meta information", nickname = "get", notes = "Returns a terminology's meta information, including a link to the original source and a link to the local refined version, if present. Also returns links to changes and versionTags.", response = TerminologyDTO.class, tags = {"retrieval",})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful response", response = TerminologyDTO.class),
			@ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class)})
	@RequestMapping(value = "/api/v1/terminologies/{terminologyId}/",
	produces = {"application/json"},
	method = RequestMethod.GET)
	ResponseEntity<TerminologyDTO> get(
			@ApiParam(value = "Identifier of the terminology.", required = true) @PathVariable("terminologyId") String terminologyId, 
			@ApiParam(value = "Terminology version defined by tag-name") @Valid @RequestParam(value = "versionTag", required = false) String versionTag, 
			@ApiParam(value = "This allows to filter the response to only contain descriptions in a specific language. If no description in the given language is present non is returned. The API expect a language to be encoded as defined in https://tools.ietf.org/html/bcp47. Further an extension 'all' is allowed. If that is given properties in all language are returned. The default language is empty.", defaultValue = "") @Valid @RequestParam(value = "lang", required = false, defaultValue = "") String lang) {

		terminologyId = terminologyService.searchTerminology(terminologyId, lang);

		if(terminologyId.equals("DocumentFiller")) {
			TerminologyDTO terminologyDTO = new TerminologyDTO();
			terminologyDTO.setBaseURL("");
			terminologyDTO.setTerminologyID("DocumentFiller");
			terminologyDTO.setLongName("All imported documents");
			DescriptionDTO descriptionDTO = new DescriptionDTO();
			descriptionDTO.setDescription("All documents which have been imported by a dedicated parser.");
			descriptionDTO.setLang(lang);
			terminologyDTO.setDescriptions(descriptionDTO);
			terminologyDTO.setVersiontags("");

			return new ResponseEntity<TerminologyDTO>(terminologyDTO, HttpStatus.OK);
		
		}
		
		try {
			Terminology terminology = terminologyService.getTerminology(terminologyId);
			TerminologyDTO terminologyDTO = new TerminologyDTO();
			terminologyDTO.setBaseURL(terminology.getIri());
			terminologyDTO.setTerminologyID(terminology.getOlsID());
			terminologyDTO.setLongName(terminology.getLongName());
			DescriptionDTO descriptionDTO = new DescriptionDTO();
			descriptionDTO.setDescription(terminology.getDescription());
			descriptionDTO.setLang(lang);
			terminologyDTO.setDescriptions(descriptionDTO);
			UriComponents uriComponents = MvcUriComponentsBuilder
					.fromMethodCall(on(TerminologyAPI.class).getVersionList(terminology.getOlsID(), (long) 0, (long) 10)).build();

			URI uri = uriComponents.encode().toUri();
			terminologyDTO.setVersiontags(uri.toString());

			return new ResponseEntity<TerminologyDTO>(terminologyDTO, HttpStatus.OK);

		} catch (javax.ws.rs.NotFoundException ex) {
			return new ResponseEntity<TerminologyDTO>(HttpStatus.NOT_FOUND);
		}

	}

	@ApiOperation(value = "Get a terminology's label",  notes = "Returns all labels, paginated using page and size. Set lang to filter by language, returns all languages by default.", response = PagedLabelDTO.class, tags={ "retrieval", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful response", response = PagedLabelDTO.class),
			@ApiResponse(code = 400, message = "Bad Client Request", response = ErrorDTO.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = ErrorDTO.class) })
	@RequestMapping(value = "/api/v1/terminologies/{terminologyId}/label",
	produces = { "application/json" },
	method = RequestMethod.GET)
	public ResponseEntity<PagedLabelDTO> getLabelOfTerminology(
			@ApiParam(value = "Identifier of the terminology.", required=true) @PathVariable("terminologyId") String terminologyId,
			@ApiParam(value = "Terminology version defined by tag-name") @Valid @RequestParam(value = "versionTag", required = false) String versionTag,
			@ApiParam(value = "This allows to filter the response to only contain descriptions in a specific language. If no description in the given language is present non is returned. The API expect a language to be encoded as defined in https://tools.ietf.org/html/bcp47. Further an extension 'all' is allowed. If that is given properties in all language are returned. The default language is empty.", defaultValue = "") @Valid @RequestParam(value = "lang", required = false, defaultValue="") String lang,
			@ApiParam(value = "The requested page", type="long", defaultValue = "0", example = "1") @Valid @RequestParam(value = "page", required = false, defaultValue="0") Long page,
			@ApiParam(value = "Size of pages", type="long", defaultValue = "10", example = "10") @Valid @RequestParam(value = "size", required = false, defaultValue="10") Long size
			) throws NotFoundException {

		terminologyId = terminologyService.searchTerminology(terminologyId, lang);

		String label = null;

		try {
			Terminology terminology = terminologyService.getTerminology(terminologyId);   
			label = terminology.getLongName();
		} catch (javax.ws.rs.NotFoundException ex) {
			return new ResponseEntity<PagedLabelDTO>(HttpStatus.NOT_FOUND);
		}

		PagedLabelDTO pagedLabelDTO= new PagedLabelDTO();
		pagedLabelDTO.setSize(1);
		pagedLabelDTO.setNumber(0);
		pagedLabelDTO.setTotalPages(1);
		pagedLabelDTO.setTotalElements(1);
		pagedLabelDTO.setNumberOfElements(1);

		LabelDTO labelDTO= new LabelDTO();
		labelDTO.setName(label);
		labelDTO.setLang(lang);
		pagedLabelDTO.addContentItem(labelDTO);

		return new ResponseEntity<PagedLabelDTO>(pagedLabelDTO,HttpStatus.OK);
	}
}
