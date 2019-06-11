package com.gnd.parking.Jobs;

import com.gnd.parking.Contracts.Jobs.EndTicketJobInterface;
import com.gnd.parking.Contracts.Repositories.ParkingSpotsRepositoryInterface;
import com.gnd.parking.Contracts.Repositories.TicketsRepositoryInterface;
import com.gnd.parking.Contracts.Services.JMS.NotificationSenderServiceInterface;
import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.ParkingSpot;
import com.gnd.parking.Models.Ticket;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

@Stateful
@Remote(EndTicketJobInterface.class)
public class EndTicketJob implements EndTicketJobInterface {

    @EJB(lookup = "java:global/parking-implementation-1.0/TicketsRepository")
    TicketsRepositoryInterface ticketsRepository;

    @EJB(lookup = "java:global/parking-implementation-1.0/ParkingSpotsRepository")
    ParkingSpotsRepositoryInterface parkingSpotsRepository;

    @EJB(lookup = "java:global/parking-jms-1.0/NotificationSenderService")
    NotificationSenderServiceInterface notificationSenderService;

    private Integer ticketId;

    @Override
    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    @Override
    public void run() {
        Ticket ticket = ticketsRepository.find(this.ticketId);

        if (ticket == null) {
            return;
        }

        ParkingSpot parkingSpot = ticket.getParkingSpot();

        if (parkingSpot == null) {
            return;
        }

        parkingSpot = parkingSpotsRepository.find(parkingSpot.getId());

        if (
            parkingSpot.getCurrentTicket() == null
                || parkingSpot.getCurrentTicket().getId() != ticketId
        ) {
            return;
        }

        parkingSpot.setCurrentTicket(null);

        try {
            parkingSpotsRepository.update(parkingSpot);
        } catch (NestedObjectNotFoundException e) {
            e.printStackTrace();
        }

        if (parkingSpot.isOccupied()) {
            sendNotification(parkingSpot);
        }
    }

    private void sendNotification(ParkingSpot parkingSpot) {
        notificationSenderService.sendNotification(
            "Parking place with id " + parkingSpot.getId() + " doesn't have valid ticket",
            parkingSpot.getId(),
            parkingSpot.getRegion().getId()
        );
    }
}
