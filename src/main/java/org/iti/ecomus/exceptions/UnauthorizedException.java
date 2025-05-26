package org.iti.ecomus.exceptions;


// For access control/authorization failures
public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }

}
