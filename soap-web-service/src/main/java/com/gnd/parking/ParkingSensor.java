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
    public String takeParkingSpot(Integer spotId) throws ParkingSpotException {
        parkingSpotTaker.takeParkingSpot(spotId);
        return "success";
    }

    @WebMethod
    public String releaseParkingSpot(Integer spotId) throws ParkingSpotException {
        parkingSpotReleaser.releaseParkingSpot(spotId);
        return "success";
    }
}
