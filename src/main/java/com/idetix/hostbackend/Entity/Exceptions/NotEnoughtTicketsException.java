package com.idetix.hostbackend.Entity.Exceptions;

public class NotEnoughtTicketsException extends Exception {
    public NotEnoughtTicketsException(String errorMessage){
        super(errorMessage);
    }
}
