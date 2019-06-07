package com.gnd.parking.Contracts.Repositories;

import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.Ticket;

import java.util.List;

public interface TicketsRepositoryInterface {
    List<Ticket> all();
    Ticket find(Integer id);
    Boolean delete(Integer id);
    Ticket update(Ticket sourceTicket) throws NestedObjectNotFoundException;
    Ticket create(Ticket sourceTicket) throws NestedObjectNotFoundException;
    Ticket save(Ticket ticket);
}
