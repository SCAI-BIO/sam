package de.fraunhofer.scai.bio.sam.domain.DTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * VersionTagDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class VersionTagDTO  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("shortName")
  private String shortName = null;

  @JsonProperty("timestamp")
  private OffsetDateTime timestamp = null;

  @JsonProperty("description")
  private DescriptionDTO description = null;

  public VersionTagDTO shortName(String shortName) {
    this.shortName = shortName;
    return this;
  }

  /**
   * A short meaninfull name of this version. Like a release name.
   * @return shortName
  **/
  @ApiModelProperty(example = "etch", required = true, value = "A short meaninfull name of this version. Like a release name.")
  @NotNull


  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public VersionTagDTO timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * The time at which the version tag was created.
   * @return timestamp
  **/
  @ApiModelProperty(example = "2018-04-15-8:30", required = true, value = "The time at which the version tag was created.")
  @NotNull

  @Valid

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public VersionTagDTO description(DescriptionDTO description) {
    this.description = description;
    return this;
  }

  /**
   * A description of a version.
   * @return description
  **/
  @ApiModelProperty(value = "A description of a version.")

  @Valid

  public DescriptionDTO getDescription() {
    return description;
  }

  public void setDescription(DescriptionDTO description) {
    this.description = description;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VersionTagDTO versionTag = (VersionTagDTO) o;
    return Objects.equals(this.shortName, versionTag.shortName) &&
        Objects.equals(this.timestamp, versionTag.timestamp) &&
        Objects.equals(this.description, versionTag.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shortName, timestamp, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VersionTagDTO {\n");
    
    sb.append("    shortName: ").append(toIndentedString(shortName)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

