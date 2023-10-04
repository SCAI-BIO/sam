package de.fraunhofer.scai.bio.sam.domain.DAO;




import java.io.Serializable;


public class Terminology implements Serializable {
        
	private static final long serialVersionUID = -8061952565032338466L;
	private String olsID;
    private String iri;
    private String shortName;
    private String longName;
    private String description;

    public String getOlsID() {
        return olsID;
    }
    
    public void setOlsID(String olsID) {
        this.olsID = olsID;
    }
    
    public String getShortName() {
        return shortName;
    }
    
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    
    public String getIri() {
        return iri;
    }
    
    public void setIri(String iri) {
        this.iri = iri;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLongName() {
        return longName;
    }
    
    public void setLongName(String longName) {
        this.longName = longName;
    }

    @Override
    public String toString() {
        return "Terminology{" +
                " olsID='" + olsID + '\'' +
                ", iri='" + iri + '\'' +
                ", shortName='" + shortName + '\'' +
                ", longName='" + longName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
