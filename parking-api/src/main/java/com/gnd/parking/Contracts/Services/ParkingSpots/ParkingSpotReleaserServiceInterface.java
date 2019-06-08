package com.gnd.parking.Contracts.Services.ParkingSpots;

import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotException;

public interface ParkingSpotReleaserServiceInterface {
    boolean releaseParkingSpot(Integer spotId) throws ParkingSpotException;
}
