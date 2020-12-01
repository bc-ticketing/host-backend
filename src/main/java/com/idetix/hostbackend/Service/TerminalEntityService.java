package com.idetix.hostbackend.Service;

import com.idetix.hostbackend.Entity.Exceptions.*;
import com.idetix.hostbackend.Entity.RequestStatus;
import com.idetix.hostbackend.Entity.TerminalEntity;
import com.idetix.hostbackend.Entity.VenueArea;
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
    private final String secret;
    @Autowired
    private TerminalEntityRepository repository;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private GuestEntityService guestEntityService;
    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    public TerminalEntityService(@Value("${RegistrationSecret}") String secret) {
        this.secret = secret;
    }


    public TerminalEntity registerTerminal(String secret, ArrayList<String> ticketType, VenueArea areaAccessFrom, VenueArea areaAccessTo) throws WrongSecretCodeException, NoAcessAreaProvidedException {
        if (!secret.contentEquals(this.secret)) {
            throw new WrongSecretCodeException("Wrong secret code try again!");
        }
        if (areaAccessTo == null) {
            throw new NoAcessAreaProvidedException("Please Provide Area accessed after passing this Terminal!");
        }
        if (areaAccessFrom == null) {
            throw new NoAcessAreaProvidedException("Please Provide Area before this Terminal!");
        }
        TerminalEntity toSave = new TerminalEntity();
        if (ticketType != null && !ticketType.isEmpty()) {
            toSave.setTicketType(ticketType);
        }
        toSave.setNumberOfTickets(0);
        toSave.setAreaAccessFrom(areaAccessFrom);
        toSave.setAreaAccessTo(areaAccessTo);
        toSave.setRequestStatus(RequestStatus.NOTYETCREATED);
        repository.save(toSave);
        return toSave;
    }


    public boolean unRegisterTerminal(UUID terminalId) throws NotRegisteredException {
        if (repository.findById(terminalId).orElse(null) == null) {
            throw new NotRegisteredException("This Terminal has not been registered");
        }
        TerminalEntity toDelete = repository.findById(terminalId).orElse(null);
        assert toDelete != null;
        repository.delete(toDelete);
        return true;
    }

    public TerminalEntity getTerminalStatus(UUID terminalId) throws NotRegisteredException {
        if (repository.findById(terminalId).orElse(null) == null) {
            throw new NotRegisteredException("This Terminal has not been registered");
        }
        return repository.findById(terminalId).orElse(null);
    }

    public TerminalEntity getNumberOfTicketsSelected(UUID terminalId) throws NotRegisteredException {
        if (repository.findById(terminalId).orElse(null) == null) {
            throw new NotRegisteredException("This Terminal has not been registered");
        }
        return repository.findById(terminalId).orElse(null);
    }


    public TerminalEntity getNewSecretCode(UUID terminalId) throws NotRegisteredException, NotYetUsedException {
        if (repository.findById(terminalId).orElse(null) == null) {
            throw new NotRegisteredException("This Terminal has not been registered");
        }
        TerminalEntity toModify = repository.findById(terminalId).orElse(null);
        assert toModify != null;
        if (toModify.getRequestStatus() != RequestStatus.PENDING) {
            if (toModify.getRequestStatus() == RequestStatus.GRANTED) {
                guestEntityService.setGuestAsEntered(toModify);
                toModify.setRandId(getNewId());
                toModify.setRequestStatus(RequestStatus.PENDING);
                toModify.setEthAddress(null);
            }
            else if (toModify.getRequestStatus() == RequestStatus.DENIED) {
                toModify.setRandId(getNewId());
                toModify.setRequestStatus(RequestStatus.PENDING);
                toModify.setEthAddress(null);
            }
            else if (toModify.getRequestStatus() == RequestStatus.NOTYETCREATED){
                toModify.setRandId(getNewId());
                toModify.setRequestStatus(RequestStatus.PENDING);
                toModify.setEthAddress(null);
            }
        } else {
            throw new NotYetUsedException("The Secret Code has not been used yet");
        }
        repository.save(toModify);
        return toModify;
    }

    public boolean verifyOwnershipOfTicket(String randId, int numberOfGuest, String signature, String ethAddress)
            throws BlockChainComunicationException,
            UnknownTerminalException,
            SignatureMismatchException,
            NotEnoughtTicketsException {
        if (numberOfGuest < 1) {
            throw new NotEnoughtTicketsException("You must select a ticket Number more than 0!");
        }
        List<TerminalEntity> accessRequestTerminals = repository.findByRandId(randId);
        if (accessRequestTerminals.isEmpty()) {
            throw new UnknownTerminalException("The RandId does not correspond to a Terminal");
        }
        TerminalEntity accessRequestTerminal = accessRequestTerminals.get(0);
        if (!securityService.verifyAddressFromSignature(ethAddress, signature, randId)) {
            accessRequestTerminal.setRequestStatus(RequestStatus.DENIED);
            repository.save(accessRequestTerminal);
            throw new SignatureMismatchException("Provided Signature does not match");
        }
        //Case 1: Enter into Venue:
        // check if the Guest is in the required before area
        // when Area from is ENTRANCE, no check needed
        if (accessRequestTerminal.getAreaAccessFrom() == VenueArea.ENTRANCE) {
            int totalTickets;
            if (accessRequestTerminal.getTicketType() == null) {
                try {
                    totalTickets = blockchainService.getGeneralTicketAmountForAddress(ethAddress);
                } catch (BlockChainComunicationException e) {
                    accessRequestTerminal.setRequestStatus(RequestStatus.DENIED);
                    repository.save(accessRequestTerminal);
                    throw e;
                }
            } else {
                try {
                    totalTickets = blockchainService.getTicketAmountForType(ethAddress, accessRequestTerminal.getTicketType());
                } catch (BlockChainComunicationException e) {
                    accessRequestTerminal.setRequestStatus(RequestStatus.DENIED);
                    repository.save(accessRequestTerminal);
                    throw e;
                }
            }
            int alreadyEntered = guestEntityService.getNumberOfGuestInVenue(ethAddress);
            int remainingTickets = totalTickets - alreadyEntered;
            if (numberOfGuest > remainingTickets) {
                accessRequestTerminal.setRequestStatus(RequestStatus.DENIED);
                repository.save(accessRequestTerminal);
                throw new NotEnoughtTicketsException("You do not have enough Tickets");
            }
        } else {
            //TO-DO: check if has ticket of type needed
            int guestInFromArea = guestEntityService.getNumberOfGuestInArea(ethAddress, accessRequestTerminal.getAreaAccessFrom());
            int guestInToArea = guestEntityService.getNumberOfGuestInArea(ethAddress, accessRequestTerminal.getAreaAccessTo());
            if( accessRequestTerminal.getTicketType() == null){
                if (guestInFromArea < numberOfGuest) {
                    accessRequestTerminal.setRequestStatus(RequestStatus.DENIED);
                    repository.save(accessRequestTerminal);
                    throw new NotEnoughtTicketsException("You do not have enough Tickets");
                }
            }else {
                int numberOfRequiredTicketsOwned = blockchainService.getTicketAmountForType(ethAddress, accessRequestTerminal.getTicketType());
                if (guestInFromArea < numberOfGuest || (numberOfRequiredTicketsOwned - guestInToArea) < numberOfGuest){
                    accessRequestTerminal.setRequestStatus(RequestStatus.DENIED);
                    repository.save(accessRequestTerminal);
                    throw new NotEnoughtTicketsException("You do not have enough Tickets");
                }
            }
        }
        accessRequestTerminal.setNumberOfTickets(numberOfGuest);
        accessRequestTerminal.setEthAddress(ethAddress);
        accessRequestTerminal.setRequestStatus(RequestStatus.GRANTED);
        repository.save(accessRequestTerminal);
        return true;
    }

    private String getNewId() {
        String candidateId = securityService.getAlphaNumericString(42);
        while (repository.findByRandId(candidateId).size() != 0) {
            candidateId = securityService.getAlphaNumericString(42);
        }
        return candidateId;
    }
}
