package com.gnd.parking.Services.ParkingSpots;

import com.gnd.parking.Contracts.Repositories.ParkingSpotsRepositoryInterface;
import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotAlreadyTakenException;
import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotDoesntExistException;
import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotException;
import com.gnd.parking.Contracts.Services.ParkingSpots.ParkingSpotTakerServiceInterface;
import com.gnd.parking.Models.ParkingSpot;


import javax.ejb.*;

@Singleton
@Remote(ParkingSpotTakerServiceInterface.class)
public class ParkingSpotTakerService implements ParkingSpotTakerServiceInterface{

    @EJB
    ParkingSpotsRepositoryInterface parkingSpotsRepository;

    @Override
    public boolean takeParkingSpot(Integer spotId) throws ParkingSpotException {
        ParkingSpot spot = parkingSpotsRepository.find(spotId);

        if (spot == null) {
            throw new ParkingSpotDoesntExistException();
        }

        if (spot.isOccupied()) {
           throw new ParkingSpotAlreadyTakenException();
        }
        spot.setOccupied(true);
        //TODO schedule here task for checking if someone bought ticket for this place
        parkingSpotsRepository.save(spot);
        return true;
    }
}
