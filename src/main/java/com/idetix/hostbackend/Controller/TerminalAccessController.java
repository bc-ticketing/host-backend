package com.idetix.hostbackend.Controller;


import com.idetix.hostbackend.Entity.Exceptions.AllreadyRegisteredException;
import com.idetix.hostbackend.Entity.Exceptions.NotRegisteredException;
import com.idetix.hostbackend.Entity.Exceptions.NotYetUsedException;
import com.idetix.hostbackend.Entity.Exceptions.WrongSecretCodeException;
import com.idetix.hostbackend.Entity.TerminalEntity;
import com.idetix.hostbackend.Service.TerminalEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class TerminalAccessController {
    @Autowired
    private TerminalEntityService terminalEntityService;


    @PostMapping("/registerTerminal")
    public TerminalEntity registerTerminal(
            @RequestParam String secret,
            @RequestParam ArrayList<String> ticketType,
            @RequestParam String areaAccessTo)
    {
        try {
            return terminalEntityService.registerTerminal(secret, ticketType, areaAccessTo);
        } catch (WrongSecretCodeException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Wrong Secret Code, Try again", e);
        }
    }


    @GetMapping("/getTerminalStatus")
    public TerminalEntity getTerminalStatus(@RequestParam UUID terminalId) {
        try {
            return terminalEntityService.getTerminalStatus(terminalId);
        } catch (NotRegisteredException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "This Terminal has not been registered", e);
        }
    }


    @PostMapping("/NewSecretCode")
    public TerminalEntity getNewSecretCode(@RequestParam UUID terminalId) {
        try {
            return terminalEntityService.getNewSecretCode(terminalId);
        } catch (NotRegisteredException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "This Terminal has not been registered", e);
        } catch (NotYetUsedException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "The Secret Code has not been used yet", e);
        }
    }


    @PostMapping("/unRegisterTerminal")
    public boolean unRegisterTerminal(@RequestParam UUID terminalId) {
        try {
            return terminalEntityService.unRegisterTerminal(terminalId);
        } catch (NotRegisteredException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "This Terminal has not been registered", e);
        }
    }

}
