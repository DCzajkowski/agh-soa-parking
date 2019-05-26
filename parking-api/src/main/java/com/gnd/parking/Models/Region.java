package com.gnd.parking.Models;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "regions")
public class Region implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "description", nullable = false)
    private String description;

    @JsonGetter("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonGetter("description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
