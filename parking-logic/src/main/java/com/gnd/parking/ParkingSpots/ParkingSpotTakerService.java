package com.gnd.parking.ParkingSpots;

import com.gnd.parking.Contracts.Jobs.CheckTicketJobInterface;
import com.gnd.parking.Contracts.Repositories.ParkingSpotsRepositoryInterface;
import com.gnd.parking.Contracts.Services.JMS.NotificationCleanerServiceInterface;
import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotAlreadyTakenException;
import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotDoesntExistException;
import com.gnd.parking.Contracts.Services.ParkingSpots.Exceptions.ParkingSpotException;
import com.gnd.parking.Contracts.Services.ParkingSpots.ParkingSpotTakerServiceInterface;
import com.gnd.parking.Contracts.Services.Scheduling.ParkingSchedulerServiceInterface;
import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.ParkingSpot;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Singleton
@Remote(ParkingSpotTakerServiceInterface.class)
public class ParkingSpotTakerService implements ParkingSpotTakerServiceInterface{

    @EJB(lookup = "java:global/parking-implementation-1.0/ParkingSpotsRepository")
    ParkingSpotsRepositoryInterface parkingSpotsRepository;

    @EJB(lookup = "java:global/parking-jms-1.0/NotificationCleanerService")
    NotificationCleanerServiceInterface notificationCleanerService;

    @EJB(lookup = "java:global/parking-jobs-1.0/CheckTicketJob")
    CheckTicketJobInterface checkTicketJob;

    @EJB(lookup = "java:global/parking-jobs-1.0/ParkingSchedulerService")
    ParkingSchedulerServiceInterface parkingSchedulerService;

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
        spot.setLastTimeTakenAt(new Date());
        try {
            parkingSpotsRepository.update(spot);
        } catch (NestedObjectNotFoundException e) {
            throw new ParkingSpotException(e.getMessage());
        }

        checkTicketJob.setParkingSpot(spotId);
        parkingSchedulerService.schedule(checkTicketJob,10, TimeUnit.SECONDS);

        notificationCleanerService.cleanNotificationsForParkingSpot(spotId);

        return true;
    }
}
