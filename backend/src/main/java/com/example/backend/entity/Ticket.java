package com.example.backend.entity;

import com.example.backend.key.TicketKey;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ticket")
@Data
@IdClass(TicketKey.class)
public class Ticket implements Serializable {

    @Id
    private String usercard;

    @Id
    @Column(length = 5)
    private String tid;

    @Id
    private int startSid;

    @Id
    private int arriveSid;


    private int cid;
    private int seatSequence;
    private float price;
    private int paid;

    public Ticket(String usercard, String tid, int startSid, int arriveSid, int cid, int seatSequence, float price) {
        this.usercard = usercard;
        this.tid = tid;
        this.startSid = startSid;
        this.arriveSid = arriveSid;
        this.cid = cid;
        this.seatSequence = seatSequence;
        this.price = price;
        this.paid = 0;
    }

    public Ticket(String usercard, String tid, int startSid, int arriveSid, int cid, int seatSequence, float price,int paid) {
        this.usercard = usercard;
        this.tid = tid;
        this.startSid = startSid;
        this.arriveSid = arriveSid;
        this.cid = cid;
        this.seatSequence = seatSequence;
        this.price = price;
        this.paid = paid;
    }

    public Ticket() {

    }
}
