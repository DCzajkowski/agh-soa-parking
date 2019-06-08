package com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions;

public class ParkingSpotAlreadyTakenException extends ParkingSpotException {
    public ParkingSpotAlreadyTakenException() {
        super("Parking spot is already taken.");
    }
}