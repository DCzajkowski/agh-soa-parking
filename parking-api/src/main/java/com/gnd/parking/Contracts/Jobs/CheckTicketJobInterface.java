package com.gnd.parking.Contracts.Jobs;

public interface CheckTicketJobInterface extends Runnable {
    void setParkingSpot(Integer parkingSpotId);
    void run();
}
