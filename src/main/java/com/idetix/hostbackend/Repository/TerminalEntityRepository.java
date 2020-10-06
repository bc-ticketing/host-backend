package com.idetix.hostbackend.Repository;

import com.idetix.hostbackend.Entity.TerminalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TerminalEntityRepository extends JpaRepository<TerminalEntity, String > {
    List<TerminalEntity> findByTerminalID(String terminalId);
}
