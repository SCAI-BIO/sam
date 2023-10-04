package de.fraunhofer.scai.bio.sam.domain.DTO;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.fraunhofer.scai.bio.sam.controller.api.terminologies.ConceptAPI;
import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A minimal representation of a Concept. Most information are left out due space constrains.
 */
@ApiModel(description = "A minimal representation of a Concept. Most information are left out due space constrains.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class ConceptMinDTO  implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("conceptID")
	private String conceptID = null;

	@JsonProperty("prefLabel")
	private LabelDTO prefLabel = null;

	@JsonProperty("link")
	private String link = null;

	@JsonProperty("parent")
	private boolean parent = false;


	public ConceptMinDTO conceptID(String conceptID) {
		this.conceptID = conceptID;
		return this;
	}
	
	public ConceptMinDTO(Concept concept) throws UnsupportedEncodingException, NotFoundException {
	    this(concept, "");
	}

	public ConceptMinDTO(Concept concept, String lang) throws NotFoundException, UnsupportedEncodingException {
		LabelDTO labelDTO2 = new LabelDTO();
		labelDTO2.setName(concept.getPrefLabel());
		labelDTO2.setLang(lang);
		this.setPrefLabel(labelDTO2);
		
		this.setParent(concept.isParent());

		if(concept.getLocalID() != null 
				&& concept.getDefiningTerminology() != null) {
			
			String id = concept.getLocalID().contains(":") ?
					concept.getLocalID() :
						concept.getPrefixedID();

			setConceptID(id);
			UriComponents uriComponents = MvcUriComponentsBuilder
					.fromMethodCall(on(ConceptAPI.class).getConcept(concept.getDefiningTerminology(),
							id, null, null, null)).build();
			this.setLink(uriComponents.encode().toUri().toString()+"/");
		} else { 
			this.setConceptID("");
			try {
				String iri = URLEncoder.encode(concept.getIri(), StandardCharsets.UTF_8.toString());
				UriComponents uriComponents = MvcUriComponentsBuilder
						.fromMethodCall(on(ConceptAPI.class).getConcept(concept.getDefiningTerminology(),
								iri, null, null, null)).build();
				this.setLink(uriComponents.encode().toUri().toString()+"/");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

	}

    /**
	 * A unique ID for the given Concept. This is either a CURIE or an IRI.
	 * @return conceptID
	 **/
	@ApiModelProperty(example = "GO:0008289", required = true, value = "A unique ID for the given Concept. This is either a CURIE or an IRI.")
	@NotNull


	public String getConceptID() {
		return conceptID;
	}

	public void setConceptID(String conceptID) {
		this.conceptID = conceptID;
	}

	public ConceptMinDTO prefLabel(LabelDTO prefLabel) {
		this.prefLabel = prefLabel;
		return this;
	}

	/**
	 * The preferred name of a Concept.
	 * @return prefLabel
	 **/
	@ApiModelProperty(value = "The preferred name of a Concept.")

	@Valid

	public LabelDTO getPrefLabel() {
		return prefLabel;
	}

	public void setPrefLabel(LabelDTO prefLabel) {
		this.prefLabel = prefLabel;
	}

	public ConceptMinDTO link(String link) {
		this.link = link;
		return this;
	}

	/**
	 * This Link can be used to obtain a full representation of the Concept.
	 * @return link
	 **/
	@ApiModelProperty(example = "api.example.org//terminologies/GO/concepts/GO:0008289", required = true, value = "This Link can be used to obtain a full representation of the Concept.")
	@NotNull


	public String getLink() {
		return link;
	}

	@ApiModelProperty(example = "false", required = true, value = "Is this node a parent or a leaf?")
	public boolean isParent() {
		return parent;
	}

	public void setParent(boolean parent) {
		this.parent = parent;
	}

	public void setLink(String link) {
		this.link = link;
	}


	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ConceptMinDTO conceptMin = (ConceptMinDTO) o;
		return Objects.equals(this.conceptID, conceptMin.conceptID) &&
				Objects.equals(this.prefLabel, conceptMin.prefLabel) &&
				Objects.equals(this.link, conceptMin.link);
	}

	@Override
	public int hashCode() {
		return Objects.hash(conceptID, prefLabel, link);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ConceptMinDTO {\n");

		sb.append("    conceptID: ").append(toIndentedString(conceptID)).append("\n");
		sb.append("    prefLabel: ").append(toIndentedString(prefLabel)).append("\n");
		sb.append("    link: ").append(toIndentedString(link)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

