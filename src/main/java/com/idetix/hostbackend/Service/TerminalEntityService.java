package com.idetix.hostbackend.Service;

import com.idetix.hostbackend.Entity.Exceptions.AllreadyRegisteredException;
import com.idetix.hostbackend.Entity.Exceptions.WrongSecretCodeException;
import com.idetix.hostbackend.Entity.TerminalEntity;
import com.idetix.hostbackend.Repository.TerminalEntityRepository;
import com.idetix.hostbackend.Service.blockchain.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TerminalEntityService {
    @Autowired
    private TerminalEntityRepository repository;
    @Autowired
    private BlockchainService blockchainService;
    private final String secret;

    @Autowired
    public TerminalEntityService( @Value("${RegistrationSecret}") String secret){
        this.secret = secret;
    }


    public TerminalEntity  registerTerminal(String terminalId,String secret) throws AllreadyRegisteredException, WrongSecretCodeException {
        if (!secret.contentEquals(this.secret)){
            throw new WrongSecretCodeException("Wronq secret code try again!");
        }
        if(repository.findById(terminalId).orElse(null)!=null){
            throw new AllreadyRegisteredException("This Terminal is allready registerd!");
        }
        TerminalEntity toSave = new TerminalEntity(this.getNewId(),terminalId);
        repository.save(toSave);
        return toSave;
    }


    public boolean unRegisterTerminal(String terminalId){
        if (repository.findById(terminalId).orElse(null) == null){
            return true;
        }
        TerminalEntity toDelete = repository.findById(terminalId).orElse(null);
        repository.delete(toDelete);
        return true;
    }


    private String getNewId(){
        String candidateId = getAlphaNumericString(42);
        while (repository.findByRandID(candidateId).size()!= 0){
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
