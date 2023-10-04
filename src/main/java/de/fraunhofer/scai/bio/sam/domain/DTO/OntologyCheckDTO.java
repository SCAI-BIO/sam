package de.fraunhofer.scai.bio.sam.domain.DTO;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * An Ontology
 */
@ApiModel(description = "Result of checking an ontology")
@Validated

public class OntologyCheckDTO  implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3464621466212638650L;

    @JsonProperty("ontologyID")
    private String ontologyID = null;

    @JsonProperty("iri")
    private String iri = null;

    public String getIri() {
        return iri;
    }

    public void setIri(String iri) {
        this.iri = iri;
    }

    @JsonProperty("consistent")
    private boolean consistent = false;

    @JsonProperty("unsatisfiable")
    private Set<String> unsatisfiable = null;

    @JsonProperty("languages")
    private Set<String> languages = null;

    @JsonProperty("nameSpaces")
    private Map<String, Integer> nameSpaces = null;
    
    @JsonProperty("cycles")
    private Set<String> cycles = null;

    public Set<String> getCycles() {
        return cycles;
    }

    public void setCycles(Set<String> cycles) {
        this.cycles = cycles;
    }

    public Map<String, Integer> getNameSpaces() {
        return nameSpaces;
    }

    public void setNameSpaces(Map<String, Integer> nameSpaces) {
        this.nameSpaces = nameSpaces;
    }

    public Set<String> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<String> languages) {
        this.languages = languages;
    }

    public OntologyCheckDTO ontologyID(String ontologyID) {
        this.ontologyID = ontologyID;
        return this;
    }

    /**
     * A unique ID of the Terminology. Used as a prefix for CURIES, and a Parameter in different API calls.
     * @return terminologyID
     **/
    @ApiModelProperty(example = "GO", required = true, value = "A unique ID of the Ontology. Used as a prefix for " +
            "CURIES, and a Parameter in different API calls.")
    @NotNull

    public String getOntologyID() {
        return ontologyID;
    }

    public void setOntologyID(String ontologyID) {
        this.ontologyID = ontologyID;
    }

    public boolean isConsistent() {
        return consistent;
    }

    public void setConsistent(boolean consistent) {
        this.consistent = consistent;
    }

    public Set<String> getUnsatisfiable() {
        return unsatisfiable;
    }

    public void setUnsatisfiable(Set<String> unsatisfiable) {
        this.unsatisfiable = unsatisfiable;
    }

 }

