package com.idetix.hostbackend.Entity.Exceptions;

public class SignatureMismatchException extends Exception {
    public SignatureMismatchException(String errorMessage){
        super(errorMessage);
    }
}
