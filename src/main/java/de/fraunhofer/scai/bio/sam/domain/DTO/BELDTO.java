package de.fraunhofer.scai.bio.sam.domain.DTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;

/**
 * BELDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class BELDTO  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("encoding")
  private String encoding = null;

  @JsonProperty("markedNS")
  private Boolean markedNS = null;

  @JsonProperty("markedAnno")
  private Boolean markedAnno = null;

  public BELDTO encoding(String encoding) {
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

  public BELDTO markedNS(Boolean markedNS) {
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

  public BELDTO markedAnno(Boolean markedAnno) {
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
    BELDTO BEL = (BELDTO) o;
    return Objects.equals(this.encoding, BEL.encoding) &&
        Objects.equals(this.markedNS, BEL.markedNS) &&
        Objects.equals(this.markedAnno, BEL.markedAnno);
  }

  @Override
  public int hashCode() {
    return Objects.hash(encoding, markedNS, markedAnno);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BELDTO {\n");
    
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

