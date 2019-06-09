package com.gnd.parking.Tickets;

import com.gnd.parking.Contracts.Jobs.CheckTicketJobInterface;
import com.gnd.parking.Contracts.Jobs.EndTicketJobInterface;
import com.gnd.parking.Contracts.Repositories.ParkingSpotsRepositoryInterface;
import com.gnd.parking.Contracts.Repositories.TicketsRepositoryInterface;
import com.gnd.parking.Contracts.Services.Scheduling.ParkingSchedulerServiceInterface;
import com.gnd.parking.Contracts.Services.Tickets.PurchaseTicketRequestInterface;
import com.gnd.parking.Contracts.Services.Tickets.PurchaseTicketServiceInterface;
import com.gnd.parking.Contracts.Services.Tickets.Exceptions.TicketPurchaseException;
import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.ParkingSpot;
import com.gnd.parking.Models.Ticket;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Stateless
@Remote(PurchaseTicketServiceInterface.class)
public class PurchaseTicketService implements PurchaseTicketServiceInterface{

    @EJB(lookup = "java:global/parking-implementation-1.0/ParkingSpotsRepository")
    ParkingSpotsRepositoryInterface parkingSpotsRepository;

    @EJB(lookup = "java:global/parking-implementation-1.0/TicketsRepository")
    TicketsRepositoryInterface ticketsRepository;

    @EJB(lookup = "java:global/parking-jobs-1.0/EndTicketJob")
    EndTicketJobInterface endTicketJob;

    @EJB(lookup = "java:global/parking-jobs-1.0/ParkingSchedulerService")
    ParkingSchedulerServiceInterface parkingSchedulerService;

    @Override
    public void purchaseTicket(Date validTo, Integer parkingSpotId) throws TicketPurchaseException {
        Ticket newTicket = new Ticket();
        newTicket.setValidFrom(new Date());
        newTicket.setValidTo(validTo);
        validateTicketTimes(newTicket);

        if (parkingSpotId == null)
            throw new TicketPurchaseException("parking_spot_id has to be present");

        ParkingSpot parkingSpot = parkingSpotsRepository.find(parkingSpotId);
        validateParkingSpot(parkingSpot);


        try {
            newTicket.setParkingSpot(parkingSpot);
            newTicket = ticketsRepository.create(newTicket);
            parkingSpot.setCurrentTicket(newTicket);
            parkingSpotsRepository.update(parkingSpot);
        } catch (NestedObjectNotFoundException e) {
            e.printStackTrace();
            throw new TicketPurchaseException("Something went wrong");
        }

        endTicketJob.setTicketId(newTicket.getId());
        long diff = getDateDiff(
                newTicket.getValidFrom(),
                newTicket.getValidTo(),
                TimeUnit.SECONDS
        );
        parkingSchedulerService.schedule(endTicketJob,diff+1,TimeUnit.SECONDS);
    }

    private void validateTicketTimes(Ticket sourceTicket) throws TicketPurchaseException {
        Date validTo = sourceTicket.getValidTo();
        Date validFrom = sourceTicket.getValidFrom();

        if (validTo == null)
            throw new TicketPurchaseException("valid_to has to be present");
        if (validTo.compareTo(validFrom) <= 0)
            throw new TicketPurchaseException("valid_to has to be in the future");
    }

    private void validateParkingSpot(ParkingSpot parkingSpot) throws TicketPurchaseException {
        if (parkingSpot == null)
            throw new TicketPurchaseException("Parking spot doesnt exist");

        if (parkingSpot.getCurrentTicket() != null)
            throw new TicketPurchaseException("Parking spot already has ticket");

        if (!parkingSpot.isOccupied())
            throw new TicketPurchaseException("Parking spot is not occupied");
    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
}
