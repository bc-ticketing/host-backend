package com.idetix.hostbackend.Controller;


import com.idetix.hostbackend.Entity.Exceptions.AllreadyRegisteredException;
import com.idetix.hostbackend.Entity.Exceptions.WrongSecretCodeException;
import com.idetix.hostbackend.Entity.TerminalEntity;
import com.idetix.hostbackend.Service.TerminalEntityService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class AccessController {
    @Autowired
    private TerminalEntityService service;


    @PostMapping("/registerTerminal")
    public TerminalEntity registerTerminal( @RequestParam String terminalId, @RequestParam String  secret){
        try {
            return service.registerTerminal(terminalId,secret);
        } catch (AllreadyRegisteredException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "This Terminal is allready registerd", e);
        } catch (WrongSecretCodeException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Wrong Secret Code, Try again", e);
        }
    }


    @PostMapping("/unRegisterTerminal")
    public boolean unRegisterTerminal( @RequestParam String terminalId){
        return service.unRegisterTerminal(terminalId);
    }

}
