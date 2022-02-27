package com.example.backend.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "train")
@Data
public class Train implements Serializable {

    @Id
    @Column(length = 5)
    private String tid;

    private char type;

    @Column(length = 10)
    private String start_station;

    @Column(length = 10)
    private String end_station;

    private Date start_date;

    private Date end_date;

    private int state;

}
