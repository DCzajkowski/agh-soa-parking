package com.gnd.parking.Services.ParkingSpots;

import com.gnd.parking.Contracts.Repositories.ParkingSpotsRepositoryInterface;
import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotAlreadyTakenException;
import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotDoesntExistException;
import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotException;
import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotNotOccupiedException;
import com.gnd.parking.Contracts.Services.ParkingSpots.ParkingSpotReleaserServiceInterface;
import com.gnd.parking.Models.ParkingSpot;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;

@Singleton
@Remote(ParkingSpotReleaserServiceInterface.class)
public class ParkingSpotReleaserService implements ParkingSpotReleaserServiceInterface {

    @EJB
    ParkingSpotsRepositoryInterface parkingSpotsRepository;

    @Override
    public boolean releaseParkingSpot(Integer spotId) throws ParkingSpotException {
        ParkingSpot spot = parkingSpotsRepository.find(spotId);

        if (spot == null) {
            throw new ParkingSpotDoesntExistException();
        }

        if (!spot.isOccupied()) {
            throw new ParkingSpotNotOccupiedException();
        }
        spot.setOccupied(false);
        //TODO Perform actions when car leaves parkingSpot
        parkingSpotsRepository.save(spot);
        return true;
    }

}
