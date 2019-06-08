package com.gnd.parking;


import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotException;
import com.gnd.parking.Contracts.Services.ParkingSpots.ParkingSpotReleaserServiceInterface;
import com.gnd.parking.Contracts.Services.ParkingSpots.ParkingSpotTakerServiceInterface;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class ParkingSensor {

    @EJB(lookup = "java:global/parking-implementation-1.0/ParkingSpotTakerService")
    ParkingSpotTakerServiceInterface parkingSpotTaker;

    @EJB(lookup = "java:global/parking-implementation-1.0/ParkingSpotReleaserService")
    ParkingSpotReleaserServiceInterface parkingSpotReleaser;

    @WebMethod
    public String takeParkingSpot(Integer spotId) {
        try {
            parkingSpotTaker.takeParkingSpot(spotId);
            return "success";
        } catch (ParkingSpotException e) {
            return e.getMessage();
        }
    }

    @WebMethod
    public String releaseParkingSpot(Integer spotId) {
        try {
            parkingSpotReleaser.releaseParkingSpot(spotId);
            return "success";
        } catch (ParkingSpotException e) {
            return e.getMessage();
        }
    }
}
