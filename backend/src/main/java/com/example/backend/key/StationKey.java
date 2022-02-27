package com.example.backend.key;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

public class StationKey implements Serializable {

    private int sid;

    private String tid;

}
