package com.idetix.hostbackend.Service.Blockchain;

import com.idetix.hostbackend.Entity.Exceptions.BlockChainComunicationException;

import java.util.List;

public interface BlockchainService {
    int getGeneralTicketAmountForAddress(String ethAddress) throws BlockChainComunicationException;

    int getTicketAmountForType(String ethAddress, List<String> ticketTypes) throws BlockChainComunicationException;

}
