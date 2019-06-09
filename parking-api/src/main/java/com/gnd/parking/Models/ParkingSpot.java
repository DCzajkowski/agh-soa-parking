package com.gnd.parking.Models;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    @OneToOne
    @JoinColumn(name = "current_ticket_id")
    private Ticket currentTicket;

    @Column(name = "is_occupied")
    private Boolean isOccupied;

    @Column(name = "last_time_taken_at")
    private Date lastTimeTakenAt;

    public ParkingSpot() {
        this.id = 0;
        this.isOccupied = false;
        this.region = null;
        this.currentTicket = null;
    }

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
    public Boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    @JsonManagedReference
    @JsonGetter("current_ticket")
    public Ticket getCurrentTicket() {
        return currentTicket;
    }

    public void setCurrentTicket(Ticket currentTicket) {
        this.currentTicket = currentTicket;
    }

    @JsonGetter("last_time_taken_at")
    public Date getLastTimeTakenAt() {
        return lastTimeTakenAt;
    }

    public void setLastTimeTakenAt(Date lastTimeTakenAt) {
        this.lastTimeTakenAt = lastTimeTakenAt;
    }
}
