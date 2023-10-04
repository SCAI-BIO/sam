package de.fraunhofer.scai.bio.sam.domain.DTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

/**
 * A &#x60;description&#x60; in a specific language.
 */
@ApiModel(description = "A `description` in a specific language.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class DescriptionDTO  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("lang")
  private String lang = null;

  public DescriptionDTO description(String description) {
    this.description = description;
    return this;
  }

  /**
   * A description.
   * @return description
  **/
  @ApiModelProperty(example = "This is a fairly long description.", required = true, value = "A description.")
  @NotNull


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public DescriptionDTO lang(String lang) {
    this.lang = lang;
    return this;
  }

  /**
   * The language of this description, as defined by https://tools.ietf.org/html/bcp47.
   * @return lang
  **/
  @ApiModelProperty(example = "en", required = true, value = "The language of this description, as defined by https://tools.ietf.org/html/bcp47.")
  @NotNull


  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DescriptionDTO description = (DescriptionDTO) o;
    return Objects.equals(this.description, description.description) &&
        Objects.equals(this.lang, description.lang);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, lang);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DescriptionDTO {\n");
    
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    lang: ").append(toIndentedString(lang)).append("\n");
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

