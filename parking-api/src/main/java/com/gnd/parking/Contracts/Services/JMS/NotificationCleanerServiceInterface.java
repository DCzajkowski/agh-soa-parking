package com.gnd.parking.Contracts.Services.JMS;

public interface NotificationCleanerServiceInterface {
    void cleanNotificationsForParkingSpot(Integer parkingSpotId);
}
