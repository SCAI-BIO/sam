package de.fraunhofer.scai.bio.sam.domain.DTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * An Ontology
 */
@ApiModel(description = "An Ontology")
@Validated

public class OntologyDTO  implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("ontologyID")
    private String ontologyID = null;

    @JsonProperty("baseURL")
    private String baseURL = null;

    @JsonProperty("language")
    private String language = null;

    @JsonProperty("descriptions")
    private DescriptionDTO descriptions = null;

    @JsonProperty("size")
    private int size;

    @JsonProperty("format")
    private String format;

    public OntologyDTO ontologyID(String ontologyID) {
        this.ontologyID = ontologyID;
        return this;
    }

    /**
     * A unique ID of the Terminology. Used as a prefix for CURIES, and a Parameter in different API calls.
     * @return terminologyID
     **/
    @ApiModelProperty(example = "GO", required = true, value = "A unique ID of the Terminology. Used as a prefix for " +
            "CURIES, and a Parameter in different API calls.")
    @NotNull


    public String getOntologyID() {
        return ontologyID;
    }

    public void setOntologyID(String ontologyID) {
        this.ontologyID = ontologyID;
    }

    public OntologyDTO baseURL(String baseURL) {
        this.baseURL = baseURL;
        return this;
    }

    /**
     * Base URL of the Terminology can be used to substitute a CURIE Prefix.
     * @return baseURL
     **/
    @ApiModelProperty(example = "http://purl.obolibrary.org/obo/GO_", required = true, value = "Base URL of the Terminology can be used to subsitute a CURIE Prefix.")
    @NotNull


    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }


    /**
     * The Name of the Terminology.
     * @return longName
     **/
    @ApiModelProperty(example = "Gene Ontology", value = "The Name of the Terminology.")


    public String getLanguage() {
        return language;
    }

    public void setLongName(String language) {
        this.language = language;
    }

    public OntologyDTO descriptions(DescriptionDTO descriptions) {
        this.descriptions = descriptions;
        return this;
    }

    /**
     * A Description of the Terminology
     * @return descriptions
     **/
    @ApiModelProperty(value = "A Description of the Terminology")

    @Valid

    public DescriptionDTO getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(DescriptionDTO descriptions) {
        this.descriptions = descriptions;
    }


    /**
     * Article to cite if this Terminology is used.
     * @return citation
     **/
    @ApiModelProperty(example = "https://www.ncbi.nlm.nih.gov/pubmed/10802651", value = "Article to cite if this Terminology is used.")


    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
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
        OntologyDTO ontology = (OntologyDTO) o;
        return Objects.equals(this.ontologyID, ontology.ontologyID) &&
                Objects.equals(this.baseURL, ontology.baseURL) &&
                Objects.equals(this.language, ontology.language) &&
                Objects.equals(this.descriptions, ontology.descriptions) &&
                Objects.equals(this.size, ontology.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ontologyID, baseURL, language, descriptions, size);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OntologyDTO {\n");

        sb.append("    ontologyID: ").append(toIndentedString(ontologyID)).append("\n");
        sb.append("    baseURL: ").append(toIndentedString(baseURL)).append("\n");
        sb.append("    longName: ").append(toIndentedString(language)).append("\n");
        sb.append("    descriptions: ").append(toIndentedString(descriptions)).append("\n");
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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }


}

