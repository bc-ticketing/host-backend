package com.idetix.hostbackend.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class GuestID implements Serializable {

    private String ethAddress;
    private VenueArea venueArea;

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuestID that = (GuestID) o;

        if (!ethAddress.equals(that.ethAddress)) return false;
        return venueArea.equals(that.venueArea);
    }

    @Override
    public int hashCode(){
        int result = ethAddress.hashCode();
        result = 31 * result + venueArea.hashCode();
        return result;
    }
}
