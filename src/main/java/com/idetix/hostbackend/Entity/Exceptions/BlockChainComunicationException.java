package com.idetix.hostbackend.Entity.Exceptions;

public class BlockChainComunicationException extends Exception {
    public BlockChainComunicationException(String errorMessage){
        super(errorMessage);
    }
}
