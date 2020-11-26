package com.idetix.hostbackend.Controller;


import com.idetix.hostbackend.Entity.RequestStatus;
import com.idetix.hostbackend.Entity.TerminalEntity;
import com.idetix.hostbackend.Entity.VenueArea;
import com.idetix.hostbackend.Service.GuestEntityService;
import com.idetix.hostbackend.Service.TerminalEntityService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.UUID;

@RestController
public class AccessController {
    @Autowired
    private TerminalEntityService terminalEntityService;
    @Autowired
    private GuestEntityService guestEntityService;


    @SneakyThrows
    @PostMapping("/registerTerminal")
    public UUID registerTerminal(
            @RequestParam String secret,
            @RequestParam(required = false) ArrayList<String> ticketType,
            @RequestParam VenueArea areaAccessFrom,
            @RequestParam VenueArea areaAccessTo) {
        TerminalEntity toReturn = terminalEntityService.registerTerminal(secret, ticketType, areaAccessFrom, areaAccessTo);
        return toReturn.terminalId;
    }


    @SneakyThrows
    @GetMapping("/getTerminalStatus")
    public RequestStatus getTerminalStatus(@RequestParam UUID terminalId) {
        return terminalEntityService.getTerminalStatus(terminalId).getRequestStatus();
    }

    @SneakyThrows
    @GetMapping("/getNumberOfTicketsSelected")
    public int getNumberOfTicketsSelected(@RequestParam UUID terminalId) {
        return terminalEntityService.getNumberOfTicketsSelected(terminalId).getNumberOfTickets();
    }


    @SneakyThrows
    @PostMapping("/NewSecretCode")
    public String getNewSecretCode(@RequestParam UUID terminalId) {
        return terminalEntityService.getNewSecretCode(terminalId).getRandId();
    }


    @SneakyThrows
    @PostMapping("/unRegisterTerminal")
    public boolean unRegisterTerminal(@RequestParam UUID terminalId) {
        return terminalEntityService.unRegisterTerminal(terminalId);
    }


    @SneakyThrows
    @PostMapping("/verifyOwnershipOfTicket")
    public boolean verifyOwnershipOfTicket(
            @RequestParam String RandId,
            @RequestParam int numberOfGuest,
            @RequestParam String signature,
            @RequestParam String ethAddress) {
        return terminalEntityService.verifyOwnershipOfTicket(RandId, numberOfGuest, signature, ethAddress);
    }
}
