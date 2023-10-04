package de.fraunhofer.scai.bio.sam.domain.DTO;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * VersionTagDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-27T08:40:59.375+02:00")

public class PrefixDTO  implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonProperty("prefix")
	private String prefix = null;

	@JsonProperty("iri")
	private String iri = null;

	public PrefixDTO prefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	public PrefixDTO iri(String iri) {
		this.iri = iri;
		return this;
	}


	/**
	 * A short meaningfull name of iri.
	 * @return prefix
	 **/
	@ApiModelProperty(example = "BFO", required = true, value = "A short meaninfull name of this iri.")
	@NotNull


	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * The iri prefix of a concept.
	 * @return iri
	 **/
	@ApiModelProperty(example = "http://purl.obolibrary.org/obo/BFO_", required = true, value = "The iri prefix of a concept.")
	@NotNull

	@Valid

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PrefixDTO versionTag = (PrefixDTO) o;
		return Objects.equals(this.prefix, versionTag.prefix) &&
				Objects.equals(this.iri, versionTag.iri);
	}

	@Override
	public int hashCode() {
		return Objects.hash(prefix, iri);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class PrefixDTO {\n");
		sb.append("    prefix: ").append(toIndentedString(prefix)).append("\n");
		sb.append("    iri: ").append(toIndentedString(iri)).append("\n");
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

