package de.fraunhofer.scai.bio.sam.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import de.fraunhofer.scai.bio.sam.service.exceptions.InvalidDataException;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;

/**
 * CustomResponseEntityExceptionHandler
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 **/
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(value =NotFoundException.class )
    protected ResponseEntity<Object> handleNotFoundException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
    
    @ExceptionHandler(value = InvalidDataException.class )
    protected ResponseEntity<Object> handleInvalidDataException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.valueOf(400), request);
    }

}

