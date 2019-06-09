package com.gnd.parking.ParkingSpots;

import com.gnd.parking.Contracts.Repositories.ParkingSpotsRepositoryInterface;
import com.gnd.parking.Contracts.Services.JMS.NotificationCleanerServiceInterface;
import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotDoesntExistException;
import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotException;
import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotNotOccupiedException;
import com.gnd.parking.Contracts.Services.ParkingSpots.ParkingSpotReleaserServiceInterface;
import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.ParkingSpot;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;

@Singleton
@Remote(ParkingSpotReleaserServiceInterface.class)
public class ParkingSpotReleaserService implements ParkingSpotReleaserServiceInterface {

    @EJB(lookup = "java:global/parking-implementation-1.0/ParkingSpotsRepository")
    ParkingSpotsRepositoryInterface parkingSpotsRepository;

    @EJB(lookup = "java:global/parking-jms-1.0/NotificationCleanerService")
    NotificationCleanerServiceInterface notificationCleanerService;

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
        spot.setCurrentTicket(null);
        try {
            parkingSpotsRepository.update(spot);
        } catch (NestedObjectNotFoundException e) {
            throw new ParkingSpotException(e.getMessage());
        }

        notificationCleanerService.cleanNotificationsForParkingSpot(spotId);

        return true;
    }

}
