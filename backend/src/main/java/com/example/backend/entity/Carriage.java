package com.example.backend.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * 车厢类
 */
@Entity
@Table(name = "carriage")
@Data
public class Carriage implements Serializable {

    @Id
    private int cid;
    private int ctype;//商务0 一等1 二等2 卧铺3
    private int seat_number;


}
