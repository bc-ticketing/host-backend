package com.idetix.hostbackend.Service;


import com.idetix.hostbackend.Entity.GuestEntity;
import com.idetix.hostbackend.Entity.GuestID;
import com.idetix.hostbackend.Entity.TerminalEntity;
import com.idetix.hostbackend.Entity.VenueArea;
import com.idetix.hostbackend.Repository.GuestEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestEntityService {

    @Autowired
    private GuestEntityRepository repository;


    public void setGuestAsEntered(TerminalEntity terminalEntity){
        if (terminalEntity.getAreaAccessfrom()==VenueArea.ENTRANCE){
            if (getNumberOfGuestInArea(terminalEntity.getEthAddress(),terminalEntity.getAreaAccessTo())!=0){
                GuestEntity toUpdate = repository.findById(new GuestID(terminalEntity.getEthAddress(),terminalEntity.getAreaAccessTo())).orElse(null);
                assert toUpdate != null;
                toUpdate.setAmountOfGuests(toUpdate.getAmountOfGuests()+terminalEntity.getNumberOfTickets());
                repository.save(toUpdate);
            }else {
                repository.save(new GuestEntity(terminalEntity.getEthAddress(), terminalEntity.getAreaAccessTo(), terminalEntity.getNumberOfTickets()));
            }
        }else {
            if (getNumberOfGuestInArea(terminalEntity.getEthAddress(),terminalEntity.getAreaAccessTo())!=0) {
                GuestEntity toUpdate = repository.findById(new GuestID(terminalEntity.getEthAddress(),terminalEntity.getAreaAccessTo())).orElse(null);
                assert toUpdate != null;
                toUpdate.setAmountOfGuests(toUpdate.getAmountOfGuests()+terminalEntity.getNumberOfTickets());
                repository.save(toUpdate);
                GuestEntity fromEntity = repository.findById(new GuestID(terminalEntity.getEthAddress(), terminalEntity.getAreaAccessfrom())).orElse(null);
                assert fromEntity != null;
                if(fromEntity.getAmountOfGuests() - terminalEntity.getNumberOfTickets() == 0){
                    repository.delete(fromEntity);
                }else {
                    fromEntity.setAmountOfGuests(fromEntity.getAmountOfGuests()-terminalEntity.getNumberOfTickets());
                    repository.save(fromEntity);
                }
            }else{
                GuestEntity fromEntity = repository.findById(new GuestID(terminalEntity.getEthAddress(), terminalEntity.getAreaAccessfrom())).orElse(null);
                assert fromEntity != null;
                if(fromEntity.getAmountOfGuests() - terminalEntity.getNumberOfTickets() == 0){
                    repository.delete(fromEntity);
                }else {
                    fromEntity.setAmountOfGuests(fromEntity.getAmountOfGuests()-terminalEntity.getNumberOfTickets());
                    repository.save(fromEntity);
                }
                repository.save(new GuestEntity(terminalEntity.getEthAddress(), terminalEntity.getAreaAccessTo(), terminalEntity.getNumberOfTickets()));
            }
        }
    }

    public int getNumberOfGuestInVenue(String ethAddress){
        List<GuestEntity> guestEntities = repository.findByGuestID_EthAddress(ethAddress);
        int inVenue = 0;
        for(GuestEntity guestEntity :guestEntities){
            inVenue = inVenue + guestEntity.getAmountOfGuests();
        }
        return  inVenue;
    }

    public int getNumberOfGuestInArea(String ethAddress, VenueArea venueArea){
        return  repository.findById(new GuestID(ethAddress,venueArea)).get().getAmountOfGuests();
    }
}
