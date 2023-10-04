package de.fraunhofer.scai.bio.sam.domain.DTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * A binary Relationship between concepts of different Terminologies. Can be identified in queries from both concepts. As one of parameter of the relation is already given the model only shows the 
 */
@ApiModel(description = "A binary Relationship between concepts of different Terminologies. Can be identified in queries from both concepts. As one of parameter of the relation is already given the model only shows the ")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class MappingRelationDTO  implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * Defines the mapping relationship type.
   */
  public enum TypeEnum {
    EXACTMATCH("exactMatch"),
    
    CLOSEMATCH("closeMatch"),
    
    RELATEDMATCH("relatedMatch");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("type")
  private TypeEnum type = null;

  @JsonProperty("conceptB")
  private ConceptMinDTO conceptB = null;

  public MappingRelationDTO type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Defines the mapping relationship type.
   * @return type
  **/
  @ApiModelProperty(required = true, value = "Defines the mapping relationship type.")
  @NotNull


  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public MappingRelationDTO conceptB(ConceptMinDTO conceptB) {
    this.conceptB = conceptB;
    return this;
  }

  /**
   * The Concept that is is relation with the given type.
   * @return conceptB
  **/
  @ApiModelProperty(required = true, value = "The Concept that is is relation with the given type.")
  @NotNull

  @Valid

  public ConceptMinDTO getConceptB() {
    return conceptB;
  }

  public void setConceptB(ConceptMinDTO conceptB) {
    this.conceptB = conceptB;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MappingRelationDTO mappingRelation = (MappingRelationDTO) o;
    return Objects.equals(this.type, mappingRelation.type) &&
        Objects.equals(this.conceptB, mappingRelation.conceptB);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, conceptB);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MappingRelationDTO {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    conceptB: ").append(toIndentedString(conceptB)).append("\n");
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

