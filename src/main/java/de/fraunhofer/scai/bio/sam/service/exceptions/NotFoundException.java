package de.fraunhofer.scai.bio.sam.service.exceptions;

/**
 * NotFoundException
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 **/
public class NotFoundException extends Exception{
    /**
     * 
     */
    private static final long serialVersionUID = 1487399764510394360L;

    public NotFoundException(){
        super();
    }
    public NotFoundException(Throwable cause) {
        super(cause);
    }
    
    public NotFoundException(String s) {
        super(s);
    }

}
