package com.idetix.hostbackend.Service;

import com.idetix.hostbackend.Entity.Exceptions.AllreadyRegisteredException;
import com.idetix.hostbackend.Entity.TerminalEntity;
import com.idetix.hostbackend.Repository.TerminalEntityRepository;
import com.idetix.hostbackend.Service.blockchain.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class TerminalEntityService {
    @Autowired
    private TerminalEntityRepository repository;
    @Autowired
    private BlockchainService blockchainService;


    public TerminalEntity  registerTerminal(String terminalId) throws AllreadyRegisteredException{
        if(repository.findByTerminalID(terminalId).size() > 0){
            throw new AllreadyRegisteredException("This Terminal is allready registerd!");
        }
        TerminalEntity toSave = new TerminalEntity(this.getNewId(),terminalId);
        repository.save(toSave);
        return toSave;
    }


    private String getNewId(){
        String candidateId = getAlphaNumericString(42);
        while (repository.findById(candidateId).orElse(null)!= null){
            candidateId = getAlphaNumericString(42);
        }
        return candidateId;
    }

    private String getAlphaNumericString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "0123456789"
                    + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

}
