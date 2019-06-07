package com.gnd.parking.Contracts.Repositories;

import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.ParkingSpot;

import java.util.List;

public interface ParkingSpotsRepositoryInterface {
    List<ParkingSpot> all();
    ParkingSpot find(Integer id);
    Boolean delete(Integer id);
    ParkingSpot update(ParkingSpot sourceParkingSpot) throws NestedObjectNotFoundException;
    ParkingSpot create(ParkingSpot sourceParkingSpot) throws NestedObjectNotFoundException;
    ParkingSpot save(ParkingSpot parkingSpot);
}
