package com.gnd.parking;

import com.gnd.parking.Contracts.Services.JMS.NotificationSenderServiceInterface;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.*;

@Singleton
@Remote(NotificationSenderServiceInterface.class)
public class NotificationSenderService implements NotificationSenderServiceInterface{

    @Inject
    JMSContext context;

    @Resource(lookup="java:/jms/queue/SOA_Parking")
    Queue queue;

    @Override
    public void sendNotification(String message, Integer parkingSpotId, Integer regionId) {
        JMSProducer producer = context.createProducer();
        producer.setProperty("parkingSpotId", parkingSpotId);
        producer.setProperty("regionId", regionId);
        producer.send(queue, message);
    }
}
