package com.gnd.parking;

import com.gnd.parking.Contracts.Services.JMS.NotificationReceiverServiceInterface;
import com.gnd.parking.Models.Region;
import com.gnd.parking.Models.User;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Remote(NotificationReceiverServiceInterface.class)
public class NotificationReceiverService implements NotificationReceiverServiceInterface{

    @Inject
    JMSContext context;

    @Resource(lookup="java:/jms/queue/SOA_Parking")
    Queue queue;

    @Override
    public List<String> receiveNotificationsForRegion(Integer regionId) throws JMSException {
        JMSConsumer consumer = context.createConsumer(queue, "regionId =" + regionId);
        return getMessages(consumer);
    }

    @Override
    public List<String> receiveNotifications() throws JMSException {
        JMSConsumer consumer = context.createConsumer(queue);
        return getMessages(consumer);
    }

    private List<String> getMessages(JMSConsumer consumer) throws JMSException {
        List<String> messages = new ArrayList<>();
        Message message;
        while ((message = consumer.receiveNoWait()) != null) {
            TextMessage textMessage = (TextMessage) message;
            messages.add(textMessage.getText());
        }
        return messages;
    }
}
