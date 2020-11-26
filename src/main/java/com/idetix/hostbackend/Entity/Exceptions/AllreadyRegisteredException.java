package com.idetix.hostbackend.Entity.Exceptions;

public class AllreadyRegisteredException extends Exception {
    public AllreadyRegisteredException(String errorMessage) {
        super(errorMessage);
    }
}
