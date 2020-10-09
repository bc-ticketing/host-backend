package com.idetix.hostbackend.Entity.Exceptions;

public class WrongSecretCodeException extends Exception {
    public WrongSecretCodeException(String errorMessage){
        super(errorMessage);
    }
}
