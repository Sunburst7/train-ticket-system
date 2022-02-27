package com.example.backend.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * 用户类
 */
@Entity
@Table(name = "user")
@Data
public class User implements Serializable{

    @Id
    private String usercard;

    @Column(length = 10)
    private String uid;

    @Column(length = 5)
    private String uname;

    @Column(length = 15)
    private String password;

    @Column(length = 20)
    private String email;

    private String phone;

    private Date birth;

    private int identity;

    private int verified;


}
