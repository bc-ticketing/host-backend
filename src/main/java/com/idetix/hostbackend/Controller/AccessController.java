package com.idetix.hostbackend.Controller;


import com.idetix.hostbackend.Entity.Exceptions.*;
import com.idetix.hostbackend.Entity.RequestStatus;
import com.idetix.hostbackend.Entity.TerminalEntity;
import com.idetix.hostbackend.Service.GuestEntityService;
import com.idetix.hostbackend.Service.TerminalEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.UUID;

@RestController
public class AccessController {
    @Autowired
    private TerminalEntityService terminalEntityService;
    @Autowired
    private GuestEntityService guestEntityService;


    @PostMapping("/registerTerminal")
    public UUID registerTerminal(
            @RequestParam String secret,
            @RequestParam ArrayList<String> ticketType,
            @RequestParam String areaAccessTo) {
        try {
            TerminalEntity toReturn = terminalEntityService.registerTerminal(secret, ticketType, areaAccessTo);
            System.out.println(toReturn.terminalId);
            return toReturn.terminalId;
        } catch (WrongSecretCodeException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Wrong Secret Code, Try again", e);
        }
    }


    @GetMapping("/getTerminalStatus")
    public RequestStatus getTerminalStatus(@RequestParam UUID terminalId) {
        try {
            return terminalEntityService.getTerminalStatus(terminalId).getRequestStatus();
        } catch (NotRegisteredException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "This Terminal has not been registered", e);
        }
    }


    @PostMapping("/NewSecretCode")
    public String getNewSecretCode(@RequestParam UUID terminalId) {
        try {
            return terminalEntityService.getNewSecretCode(terminalId).getRandId();
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


    @PostMapping("/verifyOwnershipOfTicket")
    public boolean verifyOwnershipOfTicket(
            @RequestParam String RandId,
            @RequestParam int numberOfGuest,
            @RequestParam String signature,
            @RequestParam String ethAddress) {
        try {
            return terminalEntityService.verifyOwnershipOfTicket(RandId, numberOfGuest, signature, ethAddress);
        } catch (SignatureMismatchException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Provided Signature does not match", e);
        } catch (UnknownTerminalException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "The RandId does not correspond to a Terminal", e);
        }
    }

}
