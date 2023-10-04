package de.fraunhofer.scai.bio.sam.domain.DTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.domain.DAO.Mapping;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PagedMappingDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class PagedMappingDTO  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("number")
  private Integer number = null;

  @JsonProperty("numberOfElements")
  private Integer numberOfElements = null;

  @JsonProperty("totalElements")
  private Integer totalElements = null;

  @JsonProperty("totalPages")
  private Integer totalPages = null;

  @JsonProperty("size")
  private Integer size = null;

  @JsonProperty("content")
  @Valid
  private List<MappingDTO> content = new ArrayList<>();
  
  public PagedMappingDTO(Concept concept,Page<Mapping> mappings) throws NotFoundException, UnsupportedEncodingException {
    this.setSize(mappings.getSize());
    this.setNumber(mappings.getNumber());
    this.setTotalPages(mappings.getTotalPages());
    this.setTotalElements((int) mappings.getTotalElements());
    this.setNumberOfElements(mappings.getNumberOfElements());
    
    MappingDTO mappingDTO = new MappingDTO();
    
    if(!mappings.getContent().isEmpty()){
      for (Mapping mapping : mappings.getContent()) {
        MappingRelationDTO mappingRelationDTO = new MappingRelationDTO();
        if(mapping.getA().equals(concept)) {
          mappingRelationDTO.setConceptB(new ConceptMinDTO(mapping.getB()));
        }else{
          mappingRelationDTO.setConceptB(new ConceptMinDTO(mapping.getA()));
        }
        switch (mapping.getType()){
          case CLOSE:
            mappingRelationDTO.setType(MappingRelationDTO.TypeEnum.CLOSEMATCH);
          case EXACT:
            mappingRelationDTO.setType(MappingRelationDTO.TypeEnum.EXACTMATCH);
          case RELATED:
            mappingRelationDTO.setType(MappingRelationDTO.TypeEnum.RELATEDMATCH);
            mappingRelationDTO.setType(MappingRelationDTO.TypeEnum.RELATEDMATCH);
        }
        mappingDTO.addMappedConceptsItem(mappingRelationDTO);
      }
      mappingDTO.setConceptA(new ConceptMinDTO(concept));
      this.addContentItem(mappingDTO);
    }
    
  }
  
  public PagedMappingDTO number(Integer number) {
    this.number = number;
    return this;
  }

  /**
   *  Number of this page
   * @return number
  **/
  @ApiModelProperty(example = "0", required = true, value = " Number of this page")
  @NotNull


  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public PagedMappingDTO numberOfElements(Integer numberOfElements) {
    this.numberOfElements = numberOfElements;
    return this;
  }

  /**
   *  Number of elements in this page
   * @return numberOfElements
  **/
  @ApiModelProperty(example = "4", required = true, value = " Number of elements in this page")
  @NotNull


  public Integer getNumberOfElements() {
    return numberOfElements;
  }

  public void setNumberOfElements(Integer numberOfElements) {
    this.numberOfElements = numberOfElements;
  }

  public PagedMappingDTO totalElements(Integer totalElements) {
    this.totalElements = totalElements;
    return this;
  }

  /**
   * total Number of elements
   * @return totalElements
  **/
  @ApiModelProperty(example = "4", required = true, value = "total Number of elements")
  @NotNull


  public Integer getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(Integer totalElements) {
    this.totalElements = totalElements;
  }

  public PagedMappingDTO totalPages(Integer totalPages) {
    this.totalPages = totalPages;
    return this;
  }

  /**
   * total Number of page
   * @return totalPages
  **/
  @ApiModelProperty(example = "1", required = true, value = "total Number of pages")
  @NotNull


  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public PagedMappingDTO size(Integer size) {
    this.size = size;
    return this;
  }

  /**
   * size of a page
   * @return size
  **/
  @ApiModelProperty(example = "10", required = true, value = "size of a page")
  @NotNull


  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public PagedMappingDTO content(List<MappingDTO> content) {
    this.content = content;
    return this;
  }

  public PagedMappingDTO addContentItem(MappingDTO contentItem) {
    this.content.add(contentItem);
    return this;
  }

  /**
   * Get content
   * @return content
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public List<MappingDTO> getContent() {
    return content;
  }

  public void setContent(List<MappingDTO> content) {
    this.content = content;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PagedMappingDTO pagedMapping = (PagedMappingDTO) o;
    return Objects.equals(this.number, pagedMapping.number) &&
        Objects.equals(this.numberOfElements, pagedMapping.numberOfElements) &&
        Objects.equals(this.totalElements, pagedMapping.totalElements) &&
        Objects.equals(this.totalPages, pagedMapping.totalPages) &&
        Objects.equals(this.size, pagedMapping.size) &&
        Objects.equals(this.content, pagedMapping.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, numberOfElements, totalElements, totalPages, size, content);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagedMappingDTO {\n");
    
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    numberOfElements: ").append(toIndentedString(numberOfElements)).append("\n");
    sb.append("    totalElements: ").append(toIndentedString(totalElements)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
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

