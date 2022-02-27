package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "train_carriage")
@Data
public class Train_Carriage implements Serializable {

    @Id
    private int cid;

    @Column(length = 5)
    private String tid;

    private int sequence;

    public Train_Carriage(int cid, String tid, int sequence) {
        this.cid = cid;
        this.tid = tid;
        this.sequence = sequence;
    }

    public Train_Carriage() {

    }
}
