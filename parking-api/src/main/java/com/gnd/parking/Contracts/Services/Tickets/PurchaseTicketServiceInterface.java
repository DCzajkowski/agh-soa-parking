package com.gnd.parking.Contracts.Services.Tickets;

import com.gnd.parking.Contracts.Services.Tickets.Exceptions.TicketPurchaseException;

import java.util.Date;

public interface PurchaseTicketServiceInterface {
    void purchaseTicket(Date validTo, Integer parkingSpotId) throws TicketPurchaseException;
}
