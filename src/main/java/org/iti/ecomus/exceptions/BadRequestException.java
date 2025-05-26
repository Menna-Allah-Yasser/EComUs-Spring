package org.iti.ecomus.exceptions;

// Thrown when the client sends an invalid or malformed request
// Data field missing
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
