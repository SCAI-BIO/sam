package de.fraunhofer.scai.bio.sam.domain.DTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

/**
 * PageDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class PageDTO  implements Serializable {
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

  public PageDTO number(Integer number) {
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

  public PageDTO numberOfElements(Integer numberOfElements) {
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

  public PageDTO totalElements(Integer totalElements) {
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

  public PageDTO totalPages(Integer totalPages) {
    this.totalPages = totalPages;
    return this;
  }

  /**
   * total Number of page
   * @return totalPages
  **/
  @ApiModelProperty(example = "1", required = true, value = "total Number of page")
  @NotNull


  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public PageDTO size(Integer size) {
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


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PageDTO page = (PageDTO) o;
    return Objects.equals(this.number, page.number) &&
        Objects.equals(this.numberOfElements, page.numberOfElements) &&
        Objects.equals(this.totalElements, page.totalElements) &&
        Objects.equals(this.totalPages, page.totalPages) &&
        Objects.equals(this.size, page.size);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, numberOfElements, totalElements, totalPages, size);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PageDTO {\n");
    
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    numberOfElements: ").append(toIndentedString(numberOfElements)).append("\n");
    sb.append("    totalElements: ").append(toIndentedString(totalElements)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
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

