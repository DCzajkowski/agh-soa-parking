package com.gnd.parking.Repositories;

import com.gnd.parking.Contracts.Repositories.ParkingSpotsRepositoryInterface;
import com.gnd.parking.Contracts.Repositories.RegionsRepositoryInterface;
import com.gnd.parking.Contracts.Repositories.TicketsRepositoryInterface;
import com.gnd.parking.EntityManagers.ParkingEntityManager;
import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.ParkingSpot;
import com.gnd.parking.Models.Region;
import com.gnd.parking.Models.Ticket;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import java.util.List;

@Singleton
@Remote(ParkingSpotsRepositoryInterface.class)
public class ParkingSpotsRepository implements ParkingSpotsRepositoryInterface {
    @EJB
    private ParkingEntityManager em;

    @EJB
    private RegionsRepositoryInterface regionsRepository;

    @EJB
    private TicketsRepositoryInterface ticketsRepository;

    @Override
    public List<ParkingSpot> all() {
        return em.get()
            .createQuery("SELECT p FROM ParkingSpot p")
            .getResultList();
    }

    @Override
    public List<ParkingSpot> allForRegion(int regionId) {
        return em.get()
            .createQuery("SELECT p FROM ParkingSpot p WHERE p.region.id = :regionId")
            .setParameter("regionId", regionId)
            .getResultList();
    }

    @Override
    public ParkingSpot find(Integer id) {
        return em.get().find(ParkingSpot.class, id);
    }

    @Override
    public Boolean delete(Integer id) {
        ParkingSpot parkingSpot = find(id);
        if (parkingSpot != null) {
            em.get().remove(parkingSpot);
            return true;
        }
        return false;
    }

    @Override
    public ParkingSpot save(ParkingSpot parkingSpot) {
        em.get().getTransaction().begin();
        em.get().persist(parkingSpot);
        em.get().getTransaction().commit();
        return parkingSpot;
    }

    @Override
    public ParkingSpot create(ParkingSpot sourceParkingSpot) throws NestedObjectNotFoundException {
        ParkingSpot targetParkingSpot = new ParkingSpot();
        clone(targetParkingSpot, sourceParkingSpot);

        return save(targetParkingSpot);
    }

    @Override
    public ParkingSpot update(ParkingSpot sourceParkingSpot) throws NestedObjectNotFoundException {
        ParkingSpot targetParkingSpot = find(sourceParkingSpot.getId());
        clone(targetParkingSpot, sourceParkingSpot);
        return save(targetParkingSpot);
    }

    private ParkingSpot clone(ParkingSpot targetParkingSpot, ParkingSpot sourceParkingSpot) throws NestedObjectNotFoundException {
        if (sourceParkingSpot.getRegion() != null) {
            Integer sourceRegionId = sourceParkingSpot.getRegion().getId();
            if (sourceRegionId != 0) {
                Region newRegion = regionsRepository.find(sourceRegionId);
                if (newRegion == null) {
                    throw new NestedObjectNotFoundException("Region not found");
                }
                targetParkingSpot.setRegion(newRegion);
            } else {
                targetParkingSpot.setRegion(null);
            }
        }
        if (sourceParkingSpot.getCurrentTicket() != null) {
            Integer currentTicketId = sourceParkingSpot.getCurrentTicket().getId();
            if (currentTicketId != 0) {
                Ticket newCurrentTicket = ticketsRepository.find(currentTicketId);
                if (newCurrentTicket == null) {
                    throw new NestedObjectNotFoundException("Ticket not found");
                }
                targetParkingSpot.setCurrentTicket(newCurrentTicket);
            } else {
                targetParkingSpot.setCurrentTicket(null);
            }
        } else {
            targetParkingSpot.setCurrentTicket(null);
        }
        if (sourceParkingSpot.isOccupied() != null) {
            targetParkingSpot.setOccupied(sourceParkingSpot.isOccupied());
        }
        if (sourceParkingSpot.getLastTimeTakenAt() != null) {
            targetParkingSpot.setLastTimeTakenAt(sourceParkingSpot.getLastTimeTakenAt());
        }
        return targetParkingSpot;
    }
}
