package de.fraunhofer.scai.bio.sam.domain.DAO;

/**
 * Mapping
 * <p>
 * A binary relation between Concepts. The relation is typed. Either @EXACT, @CLOSE or @RELATED.
 * This relation is not commutative and not transitive!
 *
 * A Mapping is stated as: A relates to B.
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 **/
public class Mapping {
    public Mapping() {
    
    }
    
    public static enum MAPPING_TYPE {
        EXACT,
        CLOSE,
        RELATED
    }
    
    private MAPPING_TYPE type;
    private de.fraunhofer.scai.bio.sam.domain.DAO.Concept conceptA;
    private Concept conceptB;
    
    public Mapping(MAPPING_TYPE type, Concept conceptA, Concept conceptB) {
        this.type = type;
        this.conceptA = conceptA;
        this.conceptB = conceptB;
    }
    
    public MAPPING_TYPE getType(){return type;}
    public Concept getA(){return conceptA;};
    public Concept getB(){return conceptB;};
    
    
}
