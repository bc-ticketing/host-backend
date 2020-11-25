package com.idetix.hostbackend.Service.Blockchain;

import com.idetix.hostbackend.Entity.Exceptions.BlockChainComunicationException;
import com.idetix.hostbackend.Entity.TerminalEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface BlockchainService {
    public int getGenerallTicketAmountForAddress (String ethAddress) throws BlockChainComunicationException;
    public int getTicketAmountForType (String ethAddress, List<String> ticketTypes) throws BlockChainComunicationException;

}
