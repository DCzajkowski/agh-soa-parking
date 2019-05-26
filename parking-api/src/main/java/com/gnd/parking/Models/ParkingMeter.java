package com.gnd.parking.Models;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parking_meters")
public class ParkingMeter implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "token")
    private String token;

    @JsonGetter("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonGetter("token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
