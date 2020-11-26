package com.idetix.hostbackend.Entity;

import com.idetix.hostbackend.Service.StringListConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
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
    @Column(name = "TERMINALID")
    @Type(type = "org.hibernate.type.UUIDCharType")
    public UUID terminalId;
    private String randId;
    @Convert(converter = StringListConverter.class)
    private List<String> ticketType;
    private VenueArea areaAccessfrom;
    private VenueArea areaAccessTo;
    private int numberOfTickets;
    private RequestStatus requestStatus;
    private String ethAddress;
}
