package de.fraunhofer.scai.bio.sam.domain.DTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * A Terminology
 */
@ApiModel(description = "A Terminology")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class TerminologyDTO  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("terminologyID")
  private String terminologyID = null;

  @JsonProperty("baseURL")
  private String baseURL = null;

  @JsonProperty("longName")
  private String longName = null;

  @JsonProperty("descriptions")
  private DescriptionDTO descriptions = null;

  @JsonProperty("citation")
  private String citation = null;

  @JsonProperty("versiontags")
  private String versiontags = null;

  public TerminologyDTO terminologyID(String terminologyID) {
    this.terminologyID = terminologyID;
    return this;
  }

  /**
   * A unique ID of the Terminology. Used as a prefix for CURIES, and a Parameter in different API calls.
   * @return terminologyID
  **/
  @ApiModelProperty(example = "GO", required = true, value = "A unique ID of the Terminology. Used as a prefix for " +
          "CURIES, and a Parameter in different API calls.")
  @NotNull


  public String getTerminologyID() {
    return terminologyID;
  }

  public void setTerminologyID(String terminologyID) {
    this.terminologyID = terminologyID;
  }

  public TerminologyDTO baseURL(String baseURL) {
    this.baseURL = baseURL;
    return this;
  }

  /**
   * Base URL of the Terminology can be used to subsitute a CURIE Prefix.
   * @return baseURL
  **/
  @ApiModelProperty(example = "http://purl.obolibrary.org/obo/GO_", required = true, value = "Base URL of the Terminology can be used to subsitute a CURIE Prefix.")
  @NotNull


  public String getBaseURL() {
    return baseURL;
  }

  public void setBaseURL(String baseURL) {
    this.baseURL = baseURL;
  }

  public TerminologyDTO longName(String longName) {
    this.longName = longName;
    return this;
  }

  /**
   * The Name of the Terminology.
   * @return longName
  **/
  @ApiModelProperty(example = "Gene Ontology", value = "The Name of the Terminology.")


  public String getLongName() {
    return longName;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }

  public TerminologyDTO descriptions(DescriptionDTO descriptions) {
    this.descriptions = descriptions;
    return this;
  }

  /**
   * A Description of the Terminology
   * @return descriptions
  **/
  @ApiModelProperty(value = "A Description of the Terminology")

  @Valid

  public DescriptionDTO getDescriptions() {
    return descriptions;
  }

  public void setDescriptions(DescriptionDTO descriptions) {
    this.descriptions = descriptions;
  }

  public TerminologyDTO citation(String citation) {
    this.citation = citation;
    return this;
  }

  /**
   * Article to cite if this Terminology is used.
   * @return citation
  **/
  @ApiModelProperty(example = "https://www.ncbi.nlm.nih.gov/pubmed/10802651", value = "Article to cite if this Terminology is used.")


  public String getCitation() {
    return citation;
  }

  public void setCitation(String citation) {
    this.citation = citation;
  }

  public TerminologyDTO versiontags(String versiontags) {
    this.versiontags = versiontags;
    return this;
  }

  /**
   * A list of versionTags that are present for this Terminology
   * @return versiontags
  **/
  @ApiModelProperty(example = "api.example.org/terminologies/GO/versionTags", value = "A list of versionTags that are present for this Terminology")


  public String getVersiontags() {
    return versiontags;
  }

  public void setVersiontags(String versiontags) {
    this.versiontags = versiontags;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TerminologyDTO terminology = (TerminologyDTO) o;
    return Objects.equals(this.terminologyID, terminology.terminologyID) &&
        Objects.equals(this.baseURL, terminology.baseURL) &&
        Objects.equals(this.longName, terminology.longName) &&
        Objects.equals(this.descriptions, terminology.descriptions) &&
        Objects.equals(this.citation, terminology.citation) &&
        Objects.equals(this.versiontags, terminology.versiontags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(terminologyID, baseURL, longName, descriptions, citation, versiontags);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TerminologyDTO {\n");
    
    sb.append("    terminologyID: ").append(toIndentedString(terminologyID)).append("\n");
    sb.append("    baseURL: ").append(toIndentedString(baseURL)).append("\n");
    sb.append("    longName: ").append(toIndentedString(longName)).append("\n");
    sb.append("    descriptions: ").append(toIndentedString(descriptions)).append("\n");
    sb.append("    citation: ").append(toIndentedString(citation)).append("\n");
    sb.append("    versiontags: ").append(toIndentedString(versiontags)).append("\n");
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

