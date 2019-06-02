package com.gnd.parking.Repositories;

import com.gnd.parking.Contracts.ParkingSpotsRepositoryInterface;
import com.gnd.parking.Contracts.TicketsRepositoryInterface;
import com.gnd.parking.Models.ParkingSpot;
import com.gnd.parking.EntityManagers.ParkingEntityManager;
import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.Ticket;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import java.util.List;

@Singleton
@Remote(TicketsRepositoryInterface.class)
public class TicketsRepository implements TicketsRepositoryInterface {
    @EJB
    ParkingEntityManager em;

    @EJB
    private ParkingSpotsRepositoryInterface parkingSpotsRepository;

    @Override
    public List<Ticket> all() {
        return em.get()
                .createQuery("SELECT t FROM Ticket t")
                .getResultList();
    }

    @Override
    public  Ticket find(Integer id) {
        return em.get().find(Ticket.class, id);
    }

    @Override
    public Boolean delete(Integer id) {
        Ticket ticket = find(id);
        if (ticket != null) {
            em.get().remove(ticket);
            return true;
        }
        return false;
    }

    @Override
    public Ticket save(Ticket ticket) {
        em.get().getTransaction().begin();
        em.get().persist(ticket);
        em.get().getTransaction().commit();
        return ticket;
    }

    @Override
    public Ticket create(Ticket sourceTicket) throws NestedObjectNotFoundException {
        Ticket targetTicket = new Ticket();
        clone(targetTicket, sourceTicket);
        return save(targetTicket);
    }

    @Override
    public Ticket update(Ticket sourceTicket) throws NestedObjectNotFoundException {
        Ticket targetTicket = find(sourceTicket.getId());
        clone(targetTicket, sourceTicket);
        return save(targetTicket);
    }

    private Ticket clone(Ticket targetTicket, Ticket sourceTicket) throws NestedObjectNotFoundException {
        if (sourceTicket.getValidFrom() != null) {
            targetTicket.setValidFrom(sourceTicket.getValidFrom());
        }
        if (sourceTicket.getValidTo() != null) {
            targetTicket.setValidTo(sourceTicket.getValidTo());
        }

        if (sourceTicket.getParkingSpot() != null) {
            Integer sourceParkingSpotId = sourceTicket.getParkingSpot().getId();
            if (sourceParkingSpotId != 0){
                ParkingSpot newParkingSpot = parkingSpotsRepository.find(sourceParkingSpotId);
                if (newParkingSpot == null) {
                    throw new NestedObjectNotFoundException("ParkingSpot not found");
                }
                targetTicket.setParkingSpot(newParkingSpot);
            } else {
                targetTicket.setParkingSpot(null);
            }
        }
        return targetTicket;
    }
}
