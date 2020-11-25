package com.idetix.hostbackend.Repository;

import com.idetix.hostbackend.Entity.GuestEntity;
import com.idetix.hostbackend.Entity.GuestID;
import com.idetix.hostbackend.Entity.TerminalEntity;
import com.idetix.hostbackend.Entity.VenueArea;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface GuestEntityRepository extends JpaRepository<GuestEntity, GuestID> {
    List<GuestEntity>findByGuestID_EthAddress(String ethAddress);
}
