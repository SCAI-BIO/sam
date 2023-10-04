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
 * MappingDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class MappingDTO  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("conceptA")
  private ConceptMinDTO conceptA = null;

  @JsonProperty("mappedConcepts")
  @Valid
  private List<MappingRelationDTO> mappedConcepts = null;

  public MappingDTO conceptA(ConceptMinDTO conceptA) {
    this.conceptA = conceptA;
    return this;
  }

  /**
   * Get conceptA
   * @return conceptA
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public ConceptMinDTO getConceptA() {
    return conceptA;
  }

  public void setConceptA(ConceptMinDTO conceptA) {
    this.conceptA = conceptA;
  }

  public MappingDTO mappedConcepts(List<MappingRelationDTO> mappedConcepts) {
    this.mappedConcepts = mappedConcepts;
    return this;
  }

  public MappingDTO addMappedConceptsItem(MappingRelationDTO mappedConceptsItem) {
    if (this.mappedConcepts == null) {
      this.mappedConcepts = new ArrayList<>();
    }
    this.mappedConcepts.add(mappedConceptsItem);
    return this;
  }

  /**
   * Get mappedConcepts
   * @return mappedConcepts
  **/
  @ApiModelProperty(value = "")

  @Valid

  public List<MappingRelationDTO> getMappedConcepts() {
    return mappedConcepts;
  }

  public void setMappedConcepts(List<MappingRelationDTO> mappedConcepts) {
    this.mappedConcepts = mappedConcepts;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MappingDTO mapping = (MappingDTO) o;
    return Objects.equals(this.conceptA, mapping.conceptA) &&
        Objects.equals(this.mappedConcepts, mapping.mappedConcepts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(conceptA, mappedConcepts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MappingDTO {\n");
    
    sb.append("    conceptA: ").append(toIndentedString(conceptA)).append("\n");
    sb.append("    mappedConcepts: ").append(toIndentedString(mappedConcepts)).append("\n");
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

