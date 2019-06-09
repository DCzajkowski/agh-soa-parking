package com.gnd.parking.Jobs;

import com.gnd.parking.Contracts.Jobs.CheckTicketJobInterface;
import com.gnd.parking.Contracts.Repositories.ParkingSpotsRepositoryInterface;
import com.gnd.parking.Models.ParkingSpot;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Stateful
@Remote(CheckTicketJobInterface.class)
public class CheckTicketJob implements CheckTicketJobInterface{

    @EJB(lookup = "java:global/parking-implementation-1.0/ParkingSpotsRepository")
    ParkingSpotsRepositoryInterface parkingSpotsRepository;

    private Integer parkingSpotId;

    @Override
    public void setParkingSpot(Integer parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    @Override
    public void run() {
        ParkingSpot parkingSpot = parkingSpotsRepository.find(this.parkingSpotId);
        if (parkingSpot == null) { return; }

        if (shouldHaveTicket(parkingSpot) && !hasTicket(parkingSpot)){
            System.out.println("should have ticket");
        }
    }

    private Boolean shouldHaveTicket(ParkingSpot parkingSpot) {
        return parkingSpot.isOccupied() && waitingForTicketPeriodPassed(parkingSpot);
    }

    private Boolean waitingForTicketPeriodPassed(ParkingSpot parkingSpot){
        Date takenAt = parkingSpot.getLastTimeTakenAt();
        Date now = new Date();
        return getDateDiff(takenAt, now, TimeUnit.SECONDS) >= 10;
    }

    private Boolean hasTicket(ParkingSpot parkingSpot) {
        return parkingSpot.getCurrentTicket() != null;
    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
}
