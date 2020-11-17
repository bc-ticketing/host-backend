package com.idetix.hostbackend.Entity.Exceptions;

public class UnknownTerminalException extends Exception {
    public UnknownTerminalException(String errorMessage){
        super(errorMessage);
    }
}
