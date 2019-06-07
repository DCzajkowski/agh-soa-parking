package com.gnd.parking.Repositories;

import com.gnd.parking.Contracts.Repositories.ParkingSpotsRepositoryInterface;
import com.gnd.parking.Contracts.Repositories.RegionsRepositoryInterface;
import com.gnd.parking.EntityManagers.ParkingEntityManager;
import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.ParkingSpot;
import com.gnd.parking.Models.Region;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import java.util.List;

@Singleton
@Remote(ParkingSpotsRepositoryInterface.class)
public class ParkingSpotsRepository implements ParkingSpotsRepositoryInterface {
    @EJB
    ParkingEntityManager em;

    @EJB
    private RegionsRepositoryInterface regionsRepository;

    @Override
    public List<ParkingSpot> all() {
        return em.get()
            .createQuery("SELECT p FROM ParkingSpot p")
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
        if (sourceParkingSpot.isOccupied() != null) {
            targetParkingSpot.setOccupied(sourceParkingSpot.isOccupied());
        }
        return targetParkingSpot;
    }
}
