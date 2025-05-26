package org.iti.ecomus.exceptions;


// Thrown for violations like trying to create a resource that already exists
// Adding user with existing email
public class ConflictException extends RuntimeException{
    public ConflictException(String message) {
        super(message);
    }
}
