package com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions;

public class ParkingSpotDoesntExistException extends ParkingSpotException {
    public ParkingSpotDoesntExistException() {
        super("Parking spot doesnt exist.");
    }
}
