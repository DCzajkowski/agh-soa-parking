package com.gnd.parking.Contracts.Services.JMS;

import com.gnd.parking.Models.User;

import javax.jms.JMSException;
import java.util.List;

public interface NotificationReceiverServiceInterface {
    List<String> receiveNotifications(User user) throws JMSException;
}
