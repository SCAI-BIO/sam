package de.fraunhofer.scai.bio.sam.domain.DTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

/**
 * A &#x60;name&#x60; that describes a Concept.
 */
@ApiModel(description = "A `name` that describes a Concept.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class LabelDTO  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("lang")
  private String lang = null;

  public LabelDTO name(String name) {
    this.name = name;
    return this;
  }

  /**
   * A name that describes a Concept.
   * @return name
  **/
  @ApiModelProperty(example = "lipid binding", required = true, value = "A name that describes a Concept.")
  @NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LabelDTO lang(String lang) {
    this.lang = lang;
    return this;
  }

  /**
   * The language of this Label, as defined by https://tools.ietf.org/html/bcp47.
   * @return lang
  **/
  @ApiModelProperty(example = "en", required = true, value = "The language of this Label, as defined by https://tools.ietf.org/html/bcp47.")
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
    LabelDTO label = (LabelDTO) o;
    return Objects.equals(this.name, label.name) &&
        Objects.equals(this.lang, label.lang);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, lang);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LabelDTO {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

