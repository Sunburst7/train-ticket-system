package com.example.backend.entity;

import com.example.backend.key.StationKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 列车经停车站
 */
@Entity
@Table(name = "station")
@Data
@IdClass(StationKey.class)
public class Station implements Serializable {

    @Id
    private int sid;

    @Id
    @Column(length = 5)
    private String tid;

    @Column(length = 10)
    private String sname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp arrive_date;
    private float price;
    private int mileage;
}
