package com.idetix.hostbackend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class GuestEntity {

    @EmbeddedId
    private GuestID guestID;
    private int amountOfGuests;

    public GuestEntity(String ethAddress, VenueArea venueArea, int amountOfGuests){
        this.guestID= new GuestID(ethAddress,venueArea);
        this.amountOfGuests= amountOfGuests;
    }

}
