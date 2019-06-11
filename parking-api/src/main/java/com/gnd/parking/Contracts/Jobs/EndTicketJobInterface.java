package com.gnd.parking.Contracts.Jobs;

public interface EndTicketJobInterface extends Runnable {
    void setTicketId(Integer ticketId);

    void run();
}
