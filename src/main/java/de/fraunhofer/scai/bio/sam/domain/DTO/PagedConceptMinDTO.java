package de.fraunhofer.scai.bio.sam.domain.DTO;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;
import io.swagger.annotations.ApiModelProperty;

/**
 * PagedConceptMinDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class PagedConceptMinDTO  implements Serializable {
  private static final long serialVersionUID = 1L;
  private Logger log = LoggerFactory.getLogger(getClass());

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
  private List<ConceptMinDTO> content = new ArrayList<>();

  public PagedConceptMinDTO() {
	  number(0);
	  numberOfElements(0);
	  totalElements(0);
	  totalPages(1);
	  size(0);
  }

  public PagedConceptMinDTO(Page<Concept> concepts) throws NotFoundException, UnsupportedEncodingException {
    this.setSize(concepts.getSize());
    this.setNumber(concepts.getNumber());
    this.setTotalPages(concepts.getTotalPages());
    this.setTotalElements((int) concepts.getTotalElements());
    this.setNumberOfElements(concepts.getNumberOfElements());
        
    for (Concept c1 : concepts.getContent()) {
      if(c1.getPrefixedID() != null) {  // skip concepts without an id
        ConceptMinDTO c2 = new ConceptMinDTO(c1);
        this.addContentItem(c2);
      } else if(c1.getIri() != null) { 
          ConceptMinDTO c2 = new ConceptMinDTO(c1);
          this.addContentItem(c2);    	  
      } else {
    	log.warn("Skipping '{}' ({})", c1.getPrefLabel(), c1.getID());
        this.setSize(this.getSize()-1);
        this.setTotalElements(this.getTotalElements()-1);
        this.setNumberOfElements(this.getNumberOfElements()-1);
      }
    }
    
  }
  
  public void addConcept(Concept c1) throws UnsupportedEncodingException, NotFoundException {
	    size++;
	    totalElements++;
	    numberOfElements++;
	        
	    if(c1.getPrefixedID() != null) {  // skip concepts without an id
	    	addContentItem(new ConceptMinDTO(c1));
	    } else if(c1.getIri() != null) { 
	    	this.addContentItem(new ConceptMinDTO(c1));    	  
	    } else {
	    	log.warn("Skipping '{}' ({})", c1.getPrefLabel(), c1.getID());
	    	size--;
	    	totalElements--;
	    	numberOfElements--;
	    }
  }

  public PagedConceptMinDTO number(Integer number) {
    this.number = number;
    return this;
  }

  
  /**
   *  Number of this page
   * @return number
  **/
  @ApiModelProperty(example = "1", required = true, value = " Number of this page")
  @NotNull


  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public PagedConceptMinDTO numberOfElements(Integer numberOfElements) {
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

  public PagedConceptMinDTO totalElements(Integer totalElements) {
    this.totalElements = totalElements;
    return this;
  }

  /**
   * total Number of elements
   * @return totalElements
  **/
  @ApiModelProperty(example = "14", required = true, value = "total Number of elements")
  @NotNull


  public Integer getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(Integer totalElements) {
    this.totalElements = totalElements;
  }

  public PagedConceptMinDTO totalPages(Integer totalPages) {
    this.totalPages = totalPages;
    return this;
  }

  /**
   * total Number of page
   * @return totalPages
  **/
  @ApiModelProperty(example = "2", required = true, value = "total Number of pages")
  @NotNull


  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public PagedConceptMinDTO size(Integer size) {
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

  public PagedConceptMinDTO content(List<ConceptMinDTO> content) {
    this.content = content;
    return this;
  }

  public PagedConceptMinDTO addContentItem(ConceptMinDTO contentItem) {
    this.content.add(contentItem);
    return this;
  }

  /**
   * Get content
   * @return content
  **/
  @ApiModelProperty(required = true, value = "list of concepts")
  @NotNull

  @Valid

  public List<ConceptMinDTO> getContent() {
    return content;
  }

  public void setContent(List<ConceptMinDTO> content) {
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
    PagedConceptMinDTO pagedConceptMin = (PagedConceptMinDTO) o;
    return Objects.equals(this.number, pagedConceptMin.number) &&
        Objects.equals(this.numberOfElements, pagedConceptMin.numberOfElements) &&
        Objects.equals(this.totalElements, pagedConceptMin.totalElements) &&
        Objects.equals(this.totalPages, pagedConceptMin.totalPages) &&
        Objects.equals(this.size, pagedConceptMin.size) &&
        Objects.equals(this.content, pagedConceptMin.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, numberOfElements, totalElements, totalPages, size, content);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PagedConceptMinDTO {\n");
    
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

