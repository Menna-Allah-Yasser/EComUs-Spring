package org.iti.ecomus.exceptions;



// This class represents a custom exception thrown when a requested resource is not found
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
