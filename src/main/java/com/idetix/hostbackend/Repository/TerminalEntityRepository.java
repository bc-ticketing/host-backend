package com.idetix.hostbackend.Repository;

import com.idetix.hostbackend.Entity.TerminalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface TerminalEntityRepository extends JpaRepository<TerminalEntity, UUID> {
    List<TerminalEntity> findByRandId(String randId);
}
