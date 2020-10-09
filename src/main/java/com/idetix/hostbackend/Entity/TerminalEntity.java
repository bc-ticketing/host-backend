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
@Table(name = "Terminal_Entity_TBL")
public class TerminalEntity {
    @Id
    @Column(name="TERMINALID", unique = true, columnDefinition = "VARCHAR(250)")
    private String terminalID;
    private String randID;
}
