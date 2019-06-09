package com.gnd.parking.Contracts.Jobs;

import com.gnd.parking.Models.Ticket;

public interface EndTicketJobInterface extends Runnable{
    void setTicketId(Integer ticketId);

    void run();
}
