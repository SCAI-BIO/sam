package de.fraunhofer.scai.bio.sam.domain.DAO;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Domain Object.
 * <p>
 * TODO:
 * * TreeStructure
 * * Mappings
 * ** used in Terminologies **
 * <p>
 * <p>
 * Missing and of interest:
 * https://owlcollab.github.io/oboformat/doc/GO.format.obo-1_4.html
 * **is_obsolete
 * Cardinality: zero or one. Whether or not this term is obsolete. Allowable values are "true" and "false" (false is assumed if this tag is not present). Obsolete terms must have no relationships, and no defined is_a, inverse_of, disjoint_from, union_of, or intersection_of tags.
 * **replaced_by
 * Cardinality: any. Gives a term which replaces an obsolete term. The value is the id of the replacement term. The value of this tag can safely be used to automatically reassign instances whose instance_of property points to an obsolete term.
 * **xref
 * A dbxref that describes an analogous term in another vocabulary (see dbxref formatting for information about how the value of this tag must be formatted). Cardinality: any. A term may have any number of xrefs.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Concept {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        
        if (o == null || getClass() != o.getClass()) return false;
        
        Concept concept = (Concept) o;
        
        return new EqualsBuilder()
                .append(definingTerminology, concept.definingTerminology)
                .append(localID, concept.localID)
                .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(definingTerminology)
                .append(localID)
                .toHashCode();
    }
    
    @JsonIgnore
    @Id
    private long id;
    private String iri;
    private String definingTerminology;
    private String prefLable;
    private String description;

    private String localID;
    private boolean parent;
    
    private Map<String,List<String>> annotations;
    private List<String> altLables;
    
    @JsonView(Views.GENERIC.class)
    @JsonProperty("iri")
    public String getIri() {
        return iri;
    }
    
    @JsonView(Views.GENERIC.class)
    @JsonProperty("prefLabel")
    public String getPrefLabel() {
        return prefLable;
    }
    
    @JsonView(Views.GENERIC.class)
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }
    
    @SuppressWarnings("unchecked")
	@JsonView(Views.GENERIC.class)
    @JsonProperty("altLabel")
    public List<String> getAltLabels() {
        if(altLables==null){
            return Collections.EMPTY_LIST;
        }
        return altLables;
    }

    @SuppressWarnings("unchecked")
	@JsonView(Views.GENERIC.class)
    @JsonProperty("annotations")
    public Map<String, List<String>> getAnnotations() {
        if(annotations==null){
            return Collections.EMPTY_MAP;
        }
        return annotations;
    }

    @JsonIgnore
    public String getLocalID() {
        return localID;
    }
    
    @JsonIgnore
    public String getDefiningTerminology() {
        return definingTerminology;
    }
    
    
    @JsonView(Views.JPM.class)
    @JsonProperty("jpm-id")
    public String getJProMinerFormatedID() {
        if(getDefiningTerminology()!=null) {
        	String localID = getLocalID();
        	
        	if(localID.contains(":")) {
        		String[] curie = localID.split(":");
                return curie[1].toUpperCase() + "@" + curie[0].toUpperCase();        		
        	}
        	
        	int idx = localID.lastIndexOf("/");
        	if(idx > 0) {
        		localID = localID.substring(idx+1);
        	}
        	
            return localID.toUpperCase() + "@" + getDefiningTerminology().toUpperCase();
        }
        return null;
    }
    
    @JsonView(Views.OBO.class)
    @JsonProperty("obo-id")
    public String getID() {
        if(getDefiningTerminology()!=null && getLocalID() != null) {
            return getLocalID().toUpperCase() + '@' + getDefiningTerminology().toUpperCase();
        }
        return null;
    }
    
    @JsonView(Views.RDF.class)
    @JsonProperty("curie-id")
    public String getPrefixedID() {
        if(getDefiningTerminology()!=null) {
            return getDefiningTerminology().toLowerCase() + ":" + getLocalID();
        }
        return null;
    }

     
    @JsonView(Views.BEL.class)
    @JsonProperty("bel-id")
    public String getBELID() {
        if(getDefiningTerminology()!=null) {
            return getDefiningTerminology().toUpperCase() + ":\"" + getPrefLabel()+"\"";
        }
        return null;
    }
    
    public void setIri(String iri) {
        this.iri = iri;
    }
    
    public void setPrefLabel(String prefLable) {
        this.prefLable = prefLable;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setAltLabels(List<String> altLables) {
        this.altLables = altLables;
    }

    public void setAnnotations(Map<String,List<String>> annotations) {
        this.annotations = annotations;
    }

    public void addLabel(String lable) {
        if (altLables == null) {
            altLables = new ArrayList<>();
        }
        altLables.add(lable);
    }

    public void addAnnotation(String key, String value) {
        if (annotations == null) {
        	annotations = new HashMap<String,List<String>>();
        }
        
        if(!annotations.containsKey(key)) {
        	annotations.put(key, new ArrayList<String>());
        }
        
        annotations.get(key).add(value);
    }

    public void setLocalID(String localID) {
        this.localID = localID;
    }
    
    public void setDefiningTerminology(String definingTerminology) {
        this.definingTerminology = definingTerminology;
    }
    
    public String toString() {
        return "Concept{" +
                "id=" + getID() +
                ", iri='" + iri + '\'' +
                ", prefLable='" + prefLable + '\'' +
                ", description='" + description + '\'' +
                ", altLables=" + altLables +
                ", annotations=" + annotations +
                '}';
    }
    
    String encoding;
    
    @JsonView(Views.BEL.class)
    @JsonProperty("encoding")
    public String getEncoding() {
        return encoding;
    }
    
    public Concept setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }
    
    
    boolean isPartOFNameSpace;
    
    @JsonView(Views.BEL.class)
    @JsonProperty("defined-as-namespace")
    public boolean isPartOFNameSpace() {
        return isPartOFNameSpace;
    }
    
   
    public void setPartofNameSpace(boolean b) {
        isPartOFNameSpace=b;
    }

	public boolean isParent() {
		return parent;
	}

	public void setParent(boolean parent) {
		this.parent = parent;
	}
}
