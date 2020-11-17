package com.idetix.hostbackend.Service;

import com.idetix.hostbackend.Entity.Exceptions.*;
import com.idetix.hostbackend.Entity.GuestEntity;
import com.idetix.hostbackend.Entity.RequestStatus;
import com.idetix.hostbackend.Entity.TerminalEntity;
import com.idetix.hostbackend.Repository.TerminalEntityRepository;
import com.idetix.hostbackend.Service.Blockchain.BlockchainService;
import com.idetix.hostbackend.Service.Security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TerminalEntityService {
    @Autowired
    private TerminalEntityRepository repository;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private GuestEntityService guestEntityService;
    @Autowired
    private BlockchainService blockchainService;
    private final String secret;

    @Autowired
    public TerminalEntityService( @Value("${RegistrationSecret}") String secret){
        this.secret = secret;
    }


    public TerminalEntity registerTerminal(String secret, ArrayList<String> ticketType, String areaAccessTo) throws  WrongSecretCodeException {
        if (!secret.contentEquals(this.secret)){
            throw new WrongSecretCodeException("Wronq secret code try again!");
        }
        TerminalEntity toSave = new TerminalEntity();
        toSave.setRandId(getNewId());
        toSave.setTicketType(ticketType);
        toSave.setNumberOfTickets(0);
        toSave.setRequestStatus(RequestStatus.PENDING);
        repository.save(toSave);
        return toSave;
    }


    public boolean unRegisterTerminal(UUID terminalId) throws NotRegisteredException {
        if (repository.findById(terminalId).orElse(null) == null){
            throw new NotRegisteredException("This Terminal has not been registered");
        }
        TerminalEntity toDelete = repository.findById(terminalId).orElse(null);
        repository.delete(toDelete);
        return true;
    }

    public TerminalEntity getTerminalStatus(UUID terminalId) throws NotRegisteredException{
        if (repository.findById(terminalId).orElse(null) == null){
            throw new NotRegisteredException("This Terminal has not been registered");
        }
        TerminalEntity toReturn = repository.findById(terminalId).orElse(null);
        return toReturn;
    }

    public TerminalEntity getNewSecretCode(UUID terminalId) throws NotRegisteredException, NotYetUsedException {
        if (repository.findById(terminalId).orElse(null) == null){
            throw new NotRegisteredException("This Terminal has not been registered");
        }
        TerminalEntity toModify = repository.findById(terminalId).orElse(null);
        if (toModify.getRequestStatus()!=RequestStatus.PENDING ){
            //TODO: Change the UserStatus to entered

            toModify.setRandId(getNewId());
        }
        else{ throw new NotYetUsedException("The Secret Code has not been used yet");}
        return toModify;
    }

    public boolean verifyOwnershipOfTicket(String randId, int numberOfGuest, String signature, String ethAddress) throws UnknownTerminalException,SignatureMismatchException {
        List<TerminalEntity> AccessRequestTerminals = repository.findByRandId(randId);
        if(AccessRequestTerminals.isEmpty()){
            throw new UnknownTerminalException("The RandId does not correspond to a Terminal");
        }
        TerminalEntity AccessRequestTerminal = AccessRequestTerminals.get(0);
        if (securityService.verifyAddressFromSignature(ethAddress,signature,randId)){
            throw new SignatureMismatchException("Provided Signature does not match");
        }


        return true;

    }

    private String getNewId(){
        String candidateId = securityService.getAlphaNumericString(42);
        while (repository.findByRandId(candidateId).size()!= 0){
            candidateId = securityService.getAlphaNumericString(42);
        }
        return candidateId;
    }


}
