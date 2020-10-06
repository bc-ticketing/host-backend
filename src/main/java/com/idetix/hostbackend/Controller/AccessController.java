package com.idetix.hostbackend.Controller;


import com.idetix.hostbackend.Entity.TerminalEntity;
import com.idetix.hostbackend.Service.TerminalEntityService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class AccessController {

    private final TerminalEntityService service;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final String secret;

    @Autowired
    public AccessController(TerminalEntityService service,SimpMessagingTemplate simpMessagingTemplate, @Value("${RegistrationSecret}") String secret){
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.secret = secret;
        this.service = service;
    }

    @SneakyThrows
    public boolean registerTerminal(String secret, String terminalId){
        if(secret.contentEquals(this.secret)){
            TerminalEntity terminalEntity = service.registerTerminal(terminalId);
            simpMessagingTemplate.convertAndSendToUser(terminalId,"/msg",terminalEntity.getRandID());
            return true;
        }
        return false;
    }

}
