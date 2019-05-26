package com.gnd.parking.Models;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parking_spots")
public class ParkingSpot implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "is_occupied")
    private boolean isOccupied;

    @JsonGetter("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonGetter("region")
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @JsonGetter("is_occupied")
    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }
}
