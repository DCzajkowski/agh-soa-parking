package com.gnd.parking;

import com.gnd.parking.Contracts.Services.JMS.NotificationReceiverServiceInterface;
import com.gnd.parking.Contracts.Services.JMS.NotificationSenderServiceInterface;
import com.gnd.parking.Contracts.Services.JMS.NotificationCleanerServiceInterface;

import javax.ejb.EJB;
import javax.jms.JMSException;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public class HelloWorld {

    @EJB(lookup = "java:global/parking-jms-1.0/NotificationSenderService")
    NotificationSenderServiceInterface notificationSenderService;

    @EJB(lookup = "java:global/parking-jms-1.0/NotificationReceiverService")
    NotificationReceiverServiceInterface notificationReceiverService;

    @EJB(lookup = "java:global/parking-jms-1.0/NotificationCleanerService")
    NotificationCleanerServiceInterface notificationCleanerService;

    @WebMethod
    public String sendNotification(Integer parkingSpotId, Integer regionId) {
        notificationSenderService.sendNotification("LOL",parkingSpotId,regionId);
        return "success";
    }

    @WebMethod
    public String receiveNotificationsForRegion(Integer regionId) {
        try {
            List<String> messages = notificationReceiverService.receiveNotificationsForRegion(regionId);
            return messages.toString();
        } catch (JMSException e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @WebMethod
    public String clearNotificationForParkingSpot(Integer parkingSpotId) {
        notificationCleanerService.cleanNotificationsForParkingSpot(parkingSpotId);
        return "success";
    }
}
