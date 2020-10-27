package com.idetix.hostbackend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Guest_Entity_TBL")
public class GuestEntity {

    @Id
    @Column(name="ETHADDRESS", unique = true)
    private String ethAddress;
    private String currentArea;

}
