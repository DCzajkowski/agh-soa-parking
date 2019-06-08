package com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions;

public class ParkingSpotNotOccupiedException extends ParkingSpotException {
    public ParkingSpotNotOccupiedException() {
        super("Parking spot is not occupied.");
    }
}