package de.fraunhofer.scai.bio.sam.domain.DTO;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * A Concept
 */
@ApiModel(description = "A Concept")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class ConceptDTO  implements Serializable {
  private static final long serialVersionUID = 1L;

  Logger log = LoggerFactory.getLogger(getClass());
  
  @JsonProperty("conceptID")
  private String conceptID = null;

  @JsonProperty("prefLabel")
  private LabelDTO prefLabel = null;

  @JsonProperty("description")
  private DescriptionDTO description = null;

  @JsonProperty("parent")
  private boolean parent = false;
  
  @JsonProperty("altLabels")
  private String altLabels = null;

  @JsonProperty("mappings")
  private String mappings = null;

  @JsonProperty("directBroader")
  private String directBroader = null;

  @JsonProperty("allBroader")
  private String allBroader = null;

  @JsonProperty("directNarrower")
  private String directNarrower = null;

  @JsonProperty("allNarrower")
  private String allNarrower = null;

  @JsonProperty("related")
  private String related = null;

  @JsonProperty("excerptNarrower")
  @Valid
  private List<ConceptMinDTO> excerptNarrower = null;

  @JsonProperty("excerptBroader")
  @Valid
  private List<ConceptMinDTO> excerptBroader = null;

  @JsonProperty("excerptAltLabels")
  @Valid
  private List<LabelDTO> excerptAltLabels = null;
	  
  public ConceptDTO(Concept concept) throws NotFoundException, UnsupportedEncodingException {
      log.trace("concept " + concept.getLocalID());
      init(concept, concept.getLocalID(), "");
  }
    
  public ConceptDTO(Concept concept, String conceptID) throws NotFoundException, UnsupportedEncodingException {
      log.trace("conceptID " + conceptID);
      init(concept, conceptID, "");
  }

public ConceptDTO(Concept concept, String localID, String lang) throws UnsupportedEncodingException, NotFoundException {
    log.trace("localID " + localID);
    init(concept, localID, lang);
}

private void init(Concept concept, String conceptID, String lang) throws NotFoundException, UnsupportedEncodingException {
	this.setConceptID(concept.getIri());
    DescriptionDTO descriptionDTO= new DescriptionDTO();
    descriptionDTO.setDescription(concept.getDescription());
    descriptionDTO.setLang(lang);
    this.setDescription(descriptionDTO);
    LabelDTO labelDTO = new LabelDTO();
    labelDTO.setName(concept.getPrefLabel());
    labelDTO.setLang(lang);
    this.setPrefLabel(labelDTO);
    this.setParent(concept.isParent());
    
    log.trace(conceptID + " _ " + concept.toString());
    
    List<String> a =concept.getAltLabels();
    for (String lable : a) {
      LabelDTO labelDTO2 = new LabelDTO();
      labelDTO2.setName(lable);
      labelDTO2.setLang(lang);
      this.addExcerptAltLabelsItem(labelDTO2);
      
      if( this.getExcerptAltLabels().size() >= 5) {
          break;
      }
    }
    
  
    UriComponents uriComponents = MvcUriComponentsBuilder
            .fromMethodCall(on(ConceptAPI.class).getLabelsOfConcept(concept.getDefiningTerminology(),
            		conceptID,null,null,null,null)).build();
  
    log.trace(uriComponents.toUriString());
    
    URI uri = uriComponents.encode().toUri();
    this.setAltLabels(uri.toString());
    uriComponents = MvcUriComponentsBuilder
            .fromMethodCall(on(ConceptAPI.class).getMappingsOfConcept(concept.getDefiningTerminology(),
            		conceptID,null,null,null,null)).build();
  
    uri = uriComponents.encode().toUri();
    this.setMappings(uri.toString());
    uriComponents = MvcUriComponentsBuilder
            .fromMethodCall(on(ConceptAPI.class).getMappingsOfConcept(concept.getDefiningTerminology(),
            		conceptID,null,null,null,"")).build();
  
    uri = uriComponents.encode().toUri();
    this.setDirectBroader(uri.toString());
    uriComponents = MvcUriComponentsBuilder
            .fromMethodCall(on(ConceptAPI.class).getBroaderConcepts(concept.getDefiningTerminology(),
            		conceptID,null,null,null)).build();
  
    uri = uriComponents.encode().toUri();
    this.setAllBroader(uri.toString());
    uriComponents = MvcUriComponentsBuilder
            .fromMethodCall(on(ConceptAPI.class).getNarrowerConcepts(concept.getDefiningTerminology(),
            		conceptID,null,null,null,false)).build();
  
    uri = uriComponents.encode().toUri();
    this.setDirectNarrower(uri.toString());
    uriComponents = MvcUriComponentsBuilder
            .fromMethodCall(on(ConceptAPI.class).getNarrowerConcepts(concept.getDefiningTerminology(),
            		conceptID,null,null,null,true)).build();
  
    uri = uriComponents.encode().toUri();
    this.setAllNarrower(uri.toString());
    uriComponents = MvcUriComponentsBuilder
            .fromMethodCall(on(ConceptAPI.class).getRelatedConcepts(concept.getDefiningTerminology(),
            		conceptID,null,null,null)).build();
    this.setRelated(uri.toString());
}
  
  public ConceptDTO conceptID(String conceptID) {
    this.conceptID = conceptID;
    return this;
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

  public ConceptDTO prefLabel(LabelDTO prefLabel) {
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

  public ConceptDTO description(DescriptionDTO description) {
    this.description = description;
    return this;
  }

  /**
   * A description of a Concept.
   * @return description
  **/
  @ApiModelProperty(value = "A description of a Concept.")

  @Valid

  public DescriptionDTO getDescription() {
    return description;
  }

  public void setDescription(DescriptionDTO description) {
    this.description = description;
  }

  public ConceptDTO altLabels(String altLabels) {
    this.altLabels = altLabels;
    return this;
  }

  /**
   * Use this Link to retrieve altLabels.
   * @return altLabels
  **/
  @ApiModelProperty(example = "api.example.org/terminologies/GO/concepts/GO:0008289/labels", value = "Use this Link to retrieve altLabels.")


  public String getAltLabels() {
    return altLabels;
  }

  public void setAltLabels(String altLabels) {
    this.altLabels = altLabels;
  }

  
  @ApiModelProperty(example = "false", required = true, value = "Is this node a parent or a leaf?")
  public boolean isParent() {
	  return parent;
  }

  public void setParent(boolean parent) {
	  this.parent = parent;
  }

  public ConceptDTO mappings(String mappings) {
    this.mappings = mappings;
    return this;
  }

  /**
   * Use this Link to retrieve mappings.
   * @return mappings
  **/
  @ApiModelProperty(example = "api.example.org/terminologies/GO/concepts/GO:0008289/mappings", value = "Use this Link to retrieve mappings.")


  public String getMappings() {
    return mappings;
  }

  public void setMappings(String mappings) {
    this.mappings = mappings;
  }

  public ConceptDTO directBroader(String directBroader) {
    this.directBroader = directBroader;
    return this;
  }

  /**
   * Use this Link to retrieve all concepts that are directly broader in the concept hierarchy.
   * @return directBroader
  **/
  @ApiModelProperty(example = "api.example.org/terminologies/GO/concepts/GO:0008289/broader", value = "Use this Link to retrieve all concepts that are directly broader in the concept hierarchy.")


  public String getDirectBroader() {
    return directBroader;
  }

  public void setDirectBroader(String directBroader) {
    this.directBroader = directBroader;
  }

  public ConceptDTO allBroader(String allBroader) {
    this.allBroader = allBroader;
    return this;
  }

  /**
   * Use this Link to retrieve all concepts that are directly and transitive broader in the concept hierarchy.
   * @return allBroader
  **/
  @ApiModelProperty(example = "api.example.org/terminologies/GO/concepts/GO:0008289/broader?transitive=true", value = "Use this Link to retrieve all concepts that are directly and transitive broader in the concept hierarchy.")


  public String getAllBroader() {
    return allBroader;
  }

  public void setAllBroader(String allBroader) {
    this.allBroader = allBroader;
  }

  public ConceptDTO directNarrower(String directNarrower) {
    this.directNarrower = directNarrower;
    return this;
  }

  /**
   * Use this Link to retrieve all concepts that are directly narrower in the concept hierarchy.
   * @return directNarrower
  **/
  @ApiModelProperty(example = "api.example.org/terminologies/GO/concepts/GO:0008289/narrower", value = "Use this Link to retrieve all concepts that are directly narrower in the concept hierarchy.")


  public String getDirectNarrower() {
    return directNarrower;
  }

  public void setDirectNarrower(String directNarrower) {
    this.directNarrower = directNarrower;
  }

  public ConceptDTO allNarrower(String allNarrower) {
    this.allNarrower = allNarrower;
    return this;
  }

  /**
   * Use this Link to retrieve all concepts The levels of concepts that are transitively narrower in the concept hierarchy.
   * @return allNarrower
  **/
  @ApiModelProperty(example = "api.example.org/terminologies/GO/concepts/GO:0008289/narrower?transitive=true", value = "Use this Link to retrieve all concepts The levels of concepts that are transitively narrower in the concept hierarchy.")


  public String getAllNarrower() {
    return allNarrower;
  }

  public void setAllNarrower(String allNarrower) {
    this.allNarrower = allNarrower;
  }

  public ConceptDTO related(String related) {
    this.related = related;
    return this;
  }

  /**
   * Use this Link to retrieve all related concepts
   * @return related
  **/
  @ApiModelProperty(example = "api.example.org/terminologies/GO/concepts/GO:0008289/related", value = "Use this Link to retrieve all related concepts")


  public String getRelated() {
    return related;
  }

  public void setRelated(String related) {
    this.related = related;
  }

  public ConceptDTO excerptNarrower(List<ConceptMinDTO> excerptNarrower) {
    this.excerptNarrower = excerptNarrower;
    return this;
  }

  public ConceptDTO addExcerptNarrowerItem(ConceptMinDTO excerptNarrowerItem) {
    if (this.excerptNarrower == null) {
      this.excerptNarrower = new ArrayList<>();
    }
    this.excerptNarrower.add(excerptNarrowerItem);
    return this;
  }

  /**
   * An excerpt of the concept's narrower concepts.
   * @return excerptNarrower
  **/
  @ApiModelProperty(value = "An excerpt of the concept's narrower concepts.")

  @Valid

  public List<ConceptMinDTO> getExcerptNarrower() {
    return excerptNarrower;
  }

  public void setExcerptNarrower(List<ConceptMinDTO> excerptNarrower) {
    this.excerptNarrower = excerptNarrower;
  }

  public ConceptDTO excerptBroader(List<ConceptMinDTO> excerptBroader) {
    this.excerptBroader = excerptBroader;
    return this;
  }

  public ConceptDTO addExcerptBroaderItem(ConceptMinDTO excerptBroaderItem) {
    if (this.excerptBroader == null) {
      this.excerptBroader = new ArrayList<>();
    }
    this.excerptBroader.add(excerptBroaderItem);
    return this;
  }

  /**
   * An excerpt of the concept's broader concepts.
   * @return excerptBroader
  **/
  @ApiModelProperty(value = "An excerpt of the concept's broader concepts.")

  @Valid

  public List<ConceptMinDTO> getExcerptBroader() {
    return excerptBroader;
  }

  public void setExcerptBroader(List<ConceptMinDTO> excerptBroader) {
    this.excerptBroader = excerptBroader;
  }

  public ConceptDTO excerptAltLabels(List<LabelDTO> excerptAltLabels) {
    this.excerptAltLabels = excerptAltLabels;
    return this;
  }

  public ConceptDTO addExcerptAltLabelsItem(LabelDTO excerptAltLabelsItem) {
    if (this.excerptAltLabels == null) {
      this.excerptAltLabels = new ArrayList<>();
    }
    this.excerptAltLabels.add(excerptAltLabelsItem);
    return this;
  }

  /**
   * An excerpt of concept's altLabels.
   * @return excerptAltLabels
  **/
  @SuppressWarnings("unchecked")
  @ApiModelProperty(value = "An excerpt of concept's altLabels.")

  @Valid

  public List<LabelDTO> getExcerptAltLabels() {
    if(excerptAltLabels==null){
      return Collections.EMPTY_LIST;
    }
    return excerptAltLabels;
  }

  public void setExcerptAltLabels(List<LabelDTO> excerptAltLabels) {
    this.excerptAltLabels = excerptAltLabels;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConceptDTO concept = (ConceptDTO) o;
    return Objects.equals(this.conceptID, concept.conceptID) &&
        Objects.equals(this.prefLabel, concept.prefLabel) &&
        Objects.equals(this.description, concept.description) &&
        Objects.equals(this.altLabels, concept.altLabels) &&
        Objects.equals(this.mappings, concept.mappings) &&
        Objects.equals(this.directBroader, concept.directBroader) &&
        Objects.equals(this.allBroader, concept.allBroader) &&
        Objects.equals(this.directNarrower, concept.directNarrower) &&
        Objects.equals(this.allNarrower, concept.allNarrower) &&
        Objects.equals(this.related, concept.related) &&
        Objects.equals(this.excerptNarrower, concept.excerptNarrower) &&
        Objects.equals(this.excerptBroader, concept.excerptBroader) &&
        Objects.equals(this.excerptAltLabels, concept.excerptAltLabels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(conceptID, prefLabel, description, altLabels, mappings, directBroader, allBroader, directNarrower, allNarrower, related, excerptNarrower, excerptBroader, excerptAltLabels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConceptDTO {\n");
    
    sb.append("    conceptID: ").append(toIndentedString(conceptID)).append("\n");
    sb.append("    prefLabel: ").append(toIndentedString(prefLabel)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    altLabels: ").append(toIndentedString(altLabels)).append("\n");
    sb.append("    mappings: ").append(toIndentedString(mappings)).append("\n");
    sb.append("    directBroader: ").append(toIndentedString(directBroader)).append("\n");
    sb.append("    allBroader: ").append(toIndentedString(allBroader)).append("\n");
    sb.append("    directNarrower: ").append(toIndentedString(directNarrower)).append("\n");
    sb.append("    allNarrower: ").append(toIndentedString(allNarrower)).append("\n");
    sb.append("    related: ").append(toIndentedString(related)).append("\n");
    sb.append("    excerptNarrower: ").append(toIndentedString(excerptNarrower)).append("\n");
    sb.append("    excerptBroader: ").append(toIndentedString(excerptBroader)).append("\n");
    sb.append("    excerptAltLabels: ").append(toIndentedString(excerptAltLabels)).append("\n");
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

