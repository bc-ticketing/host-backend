package com.idetix.hostbackend.Entity.Exceptions;

public class NotRegisteredException extends Exception {
    public NotRegisteredException(String errorMessage){
        super(errorMessage);
    }
}
