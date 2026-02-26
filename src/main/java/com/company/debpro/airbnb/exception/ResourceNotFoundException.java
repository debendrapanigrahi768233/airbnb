package com.company.debpro.airbnb.exception;

public class ResourceNotFoundException extends RuntimeException {
    // Alt + Insert to insert the constructor here
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
