package com.gnd.parking.Contracts.Services.ParkingSpots;

import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotException;

public interface ParkingSpotTakerServiceInterface {
    boolean takeParkingSpot(Integer spotId) throws ParkingSpotException;
}
