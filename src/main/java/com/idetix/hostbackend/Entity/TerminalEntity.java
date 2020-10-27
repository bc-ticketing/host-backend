package com.idetix.hostbackend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Terminal_Entity_TBL")
public class TerminalEntity {
    @Id
    @GeneratedValue
    @Column(name="TERMINALID", unique = true)
    private UUID terminalId;
    private String randId;
    private ArrayList<String> ticketType;
    private String areaAccessTo;
    private int numberOfTickets;
    private boolean responseDone;
    private boolean accessAllowed;
}
