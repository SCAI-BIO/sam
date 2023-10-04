package de.fraunhofer.scai.bio.sam.service.exceptions;

/**
 * InvalidDataException
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 **/
public class InvalidDataException extends Exception{
    /**
     * 
     */
    private static final long serialVersionUID = 3345079989194271172L;

    public InvalidDataException(String id_not_set) {
        super(id_not_set);
    }
    
    public InvalidDataException(Throwable throwable) {
        super(throwable);
    }
    
    public InvalidDataException() {
    }
}
