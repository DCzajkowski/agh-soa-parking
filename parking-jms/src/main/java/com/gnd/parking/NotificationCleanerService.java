package com.gnd.parking;

import com.gnd.parking.Contracts.Services.JMS.NotificationCleanerServiceInterface;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Queue;

@Singleton
@Remote(NotificationCleanerServiceInterface.class)
public class NotificationCleanerService implements NotificationCleanerServiceInterface {
    @Inject
    JMSContext context;

    @Resource(lookup = "java:/jms/queue/SOA_Parking")
    Queue queue;

    @Override
    public void cleanNotificationsForParkingSpot(Integer parkingSpotId) {
        JMSConsumer consumer = context.createConsumer(queue, "parkingSpotId =" + parkingSpotId);

        Message message;
        while ((message = consumer.receiveNoWait()) != null) {
            //
        }
    }
}
