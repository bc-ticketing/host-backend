package com.idetix.hostbackend.Repository;

import com.idetix.hostbackend.Entity.GuestEntity;
import com.idetix.hostbackend.Entity.GuestID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GuestEntityRepository extends JpaRepository<GuestEntity, GuestID> {
    List<GuestEntity>findByGuestID_EthAddress(String ethAddress);
}
