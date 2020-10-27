package com.idetix.hostbackend.Service;

import com.idetix.hostbackend.Entity.Exceptions.AllreadyRegisteredException;
import com.idetix.hostbackend.Entity.Exceptions.NotRegisteredException;
import com.idetix.hostbackend.Entity.Exceptions.NotYetUsedException;
import com.idetix.hostbackend.Entity.Exceptions.WrongSecretCodeException;
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
        toSave.setAccessAllowed(false);
        toSave.setResponseDone(false);
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
        if (toModify.isResponseDone()){
            //TODO: Change the UserStatus to entered

            toModify.setRandId(getNewId());
        }
        else{ throw new NotYetUsedException("The Secret Code has not been used yet");}
        return toModify;
    }


    private String getNewId(){
        String candidateId = securityService.getAlphaNumericString(42);
        while (repository.findByRandId(candidateId).size()!= 0){
            candidateId = securityService.getAlphaNumericString(42);
        }
        return candidateId;
    }

}
