package com.gnd.parking;

import com.gnd.parking.Contracts.Services.JMS.NotificationReceiverServiceInterface;
import com.gnd.parking.Contracts.Services.JMS.NotificationSenderServiceInterface;
import com.gnd.parking.Models.User;

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

    @WebMethod
    public String sayHelloWorldFrom(String from) {
        notificationSenderService.sendNotification("LOL",1,1);
        return "Hello, world, from " + from;
    }

    @WebMethod
    public String receiveString() {
        User user = new User();
        try {
            List<String> messages = notificationReceiverService.receiveNotifications(user);
            return messages.toString();
        } catch (JMSException e) {
            e.printStackTrace();
            return "fail";
        }
    }
}
