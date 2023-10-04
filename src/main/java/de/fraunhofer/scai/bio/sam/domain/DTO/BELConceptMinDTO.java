package de.fraunhofer.scai.bio.sam.domain.DTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BELConceptMinDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class BELConceptMinDTO  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("conceptID")
  private String conceptID = null;

  @JsonProperty("prefLabel")
  private LabelDTO prefLabel = null;

  @JsonProperty("link")
  private String link = null;

  @JsonProperty("encoding")
  private String encoding = null;

  @JsonProperty("markedNS")
  private Boolean markedNS = null;

  @JsonProperty("markedAnno")
  private Boolean markedAnno = null;

  public BELConceptMinDTO conceptID(String conceptID) {
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

  public BELConceptMinDTO prefLabel(LabelDTO prefLabel) {
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

  public BELConceptMinDTO link(String link) {
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

  public void setLink(String link) {
    this.link = link;
  }

  public BELConceptMinDTO encoding(String encoding) {
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

  public BELConceptMinDTO markedNS(Boolean markedNS) {
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

  public BELConceptMinDTO markedAnno(Boolean markedAnno) {
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
    BELConceptMinDTO beLConceptMin = (BELConceptMinDTO) o;
    return Objects.equals(this.conceptID, beLConceptMin.conceptID) &&
        Objects.equals(this.prefLabel, beLConceptMin.prefLabel) &&
        Objects.equals(this.link, beLConceptMin.link) &&
        Objects.equals(this.encoding, beLConceptMin.encoding) &&
        Objects.equals(this.markedNS, beLConceptMin.markedNS) &&
        Objects.equals(this.markedAnno, beLConceptMin.markedAnno);
  }

  @Override
  public int hashCode() {
    return Objects.hash(conceptID, prefLabel, link, encoding, markedNS, markedAnno);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BELConceptMinDTO {\n");
    
    sb.append("    conceptID: ").append(toIndentedString(conceptID)).append("\n");
    sb.append("    prefLabel: ").append(toIndentedString(prefLabel)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
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

