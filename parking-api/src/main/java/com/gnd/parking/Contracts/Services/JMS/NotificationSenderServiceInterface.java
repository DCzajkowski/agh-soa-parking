package com.gnd.parking.Contracts.Services.JMS;

public interface NotificationSenderServiceInterface {
    void sendNotification(String message, Integer parkingSpotId, Integer regionId);
}
