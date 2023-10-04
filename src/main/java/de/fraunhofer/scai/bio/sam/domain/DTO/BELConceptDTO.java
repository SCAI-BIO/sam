package de.fraunhofer.scai.bio.sam.domain.DTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BELConceptDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class BELConceptDTO  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("conceptID")
  private String conceptID = null;

  @JsonProperty("prefLabel")
  private LabelDTO prefLabel = null;

  @JsonProperty("description")
  private DescriptionDTO description = null;

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

  @JsonProperty("encoding")
  private String encoding = null;

  @JsonProperty("markedNS")
  private Boolean markedNS = null;

  @JsonProperty("markedAnno")
  private Boolean markedAnno = null;

  public BELConceptDTO conceptID(String conceptID) {
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

  public BELConceptDTO prefLabel(LabelDTO prefLabel) {
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

  public BELConceptDTO description(DescriptionDTO description) {
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

  public BELConceptDTO altLabels(String altLabels) {
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

  public BELConceptDTO mappings(String mappings) {
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

  public BELConceptDTO directBroader(String directBroader) {
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

  public BELConceptDTO allBroader(String allBroader) {
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

  public BELConceptDTO directNarrower(String directNarrower) {
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

  public BELConceptDTO allNarrower(String allNarrower) {
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

  public BELConceptDTO related(String related) {
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

  public BELConceptDTO excerptNarrower(List<ConceptMinDTO> excerptNarrower) {
    this.excerptNarrower = excerptNarrower;
    return this;
  }

  public BELConceptDTO addExcerptNarrowerItem(ConceptMinDTO excerptNarrowerItem) {
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

  public BELConceptDTO excerptBroader(List<ConceptMinDTO> excerptBroader) {
    this.excerptBroader = excerptBroader;
    return this;
  }

  public BELConceptDTO addExcerptBroaderItem(ConceptMinDTO excerptBroaderItem) {
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

  public BELConceptDTO excerptAltLabels(List<LabelDTO> excerptAltLabels) {
    this.excerptAltLabels = excerptAltLabels;
    return this;
  }

  public BELConceptDTO addExcerptAltLabelsItem(LabelDTO excerptAltLabelsItem) {
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
  @ApiModelProperty(value = "An excerpt of concept's altLabels.")

  @Valid

  public List<LabelDTO> getExcerptAltLabels() {
    return excerptAltLabels;
  }

  public void setExcerptAltLabels(List<LabelDTO> excerptAltLabels) {
    this.excerptAltLabels = excerptAltLabels;
  }

  public BELConceptDTO encoding(String encoding) {
    this.encoding = encoding;
    return this;
  }

  /**
   * Encoding of the Concept if present in namespaceFile, otherwise empty.
   * @return encoding
  **/
  @ApiModelProperty(example = "E", value = "Encoding of the Concept if present in namespaceFile, otherwise empty.")


  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public BELConceptDTO markedNS(Boolean markedNS) {
    this.markedNS = markedNS;
    return this;
  }

  /**
   * Whether this concept is marked to appear in the namespaceFile.
   * @return markedNS
  **/
  @ApiModelProperty(example = "true", value = "Whether this concept is marked to appear in the namespaceFile.")


  public Boolean isMarkedNS() {
    return markedNS;
  }

  public void setMarkedNS(Boolean markedNS) {
    this.markedNS = markedNS;
  }

  public BELConceptDTO markedAnno(Boolean markedAnno) {
    this.markedAnno = markedAnno;
    return this;
  }

  /**
   * Whether this concept is marked to appear in the annotationFile.
   * @return markedAnno
  **/
  @ApiModelProperty(example = "false", value = "Whether this concept is marked to appear in the annotationFile.")


  public Boolean isMarkedAnno() {
    return markedAnno;
  }

  public void setMarkedAnno(Boolean markedAnno) {
    this.markedAnno = markedAnno;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BELConceptDTO beLConcept = (BELConceptDTO) o;
    return Objects.equals(this.conceptID, beLConcept.conceptID) &&
        Objects.equals(this.prefLabel, beLConcept.prefLabel) &&
        Objects.equals(this.description, beLConcept.description) &&
        Objects.equals(this.altLabels, beLConcept.altLabels) &&
        Objects.equals(this.mappings, beLConcept.mappings) &&
        Objects.equals(this.directBroader, beLConcept.directBroader) &&
        Objects.equals(this.allBroader, beLConcept.allBroader) &&
        Objects.equals(this.directNarrower, beLConcept.directNarrower) &&
        Objects.equals(this.allNarrower, beLConcept.allNarrower) &&
        Objects.equals(this.related, beLConcept.related) &&
        Objects.equals(this.excerptNarrower, beLConcept.excerptNarrower) &&
        Objects.equals(this.excerptBroader, beLConcept.excerptBroader) &&
        Objects.equals(this.excerptAltLabels, beLConcept.excerptAltLabels) &&
        Objects.equals(this.encoding, beLConcept.encoding) &&
        Objects.equals(this.markedNS, beLConcept.markedNS) &&
        Objects.equals(this.markedAnno, beLConcept.markedAnno);
  }

  @Override
  public int hashCode() {
    return Objects.hash(conceptID, prefLabel, description, altLabels, mappings, directBroader, allBroader, directNarrower, allNarrower, related, excerptNarrower, excerptBroader, excerptAltLabels, encoding, markedNS, markedAnno);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BELConceptDTO {\n");
    
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
    sb.append("    encoding: ").append(toIndentedString(encoding)).append("\n");
    sb.append("    markedNS: ").append(toIndentedString(markedNS)).append("\n");
    sb.append("    markedAnno: ").append(toIndentedString(markedAnno)).append("\n");
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

