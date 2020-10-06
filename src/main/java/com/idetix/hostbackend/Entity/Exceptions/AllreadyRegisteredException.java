package com.idetix.hostbackend.Entity.Exceptions;

import lombok.AllArgsConstructor;

public class AllreadyRegisteredException extends Exception {
    public AllreadyRegisteredException (String errorMessage){
        super(errorMessage);
    }
}
