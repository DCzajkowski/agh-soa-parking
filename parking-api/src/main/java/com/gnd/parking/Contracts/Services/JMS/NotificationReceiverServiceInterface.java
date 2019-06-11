package com.gnd.parking.Contracts.Services.JMS;

import javax.jms.JMSException;
import java.util.List;

public interface NotificationReceiverServiceInterface {
    List<String> receiveNotificationsForRegion(Integer regionId) throws JMSException;

    List<String> receiveNotifications() throws JMSException;
}
