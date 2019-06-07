package com.gnd.parking.Repositories;

import com.gnd.parking.Contracts.Repositories.RegionsRepositoryInterface;
import com.gnd.parking.EntityManagers.ParkingEntityManager;
import com.gnd.parking.Models.Region;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import java.util.List;

@Singleton
@Remote(RegionsRepositoryInterface.class)
public class RegionsRepository implements RegionsRepositoryInterface {
    @EJB
    ParkingEntityManager em;

    @Override
    public List<Region> all() {
        return em.get()
            .createQuery("SELECT r FROM Region r")
            .getResultList();
    }

    @Override
    public Region find(Integer id) {
        return em.get().find(Region.class, id);
    }

    @Override
    public Boolean delete(Integer id) {
        Region region = find(id);
        if (region != null) {
            em.get().remove(region);
            return true;
        }
        return false;
    }

    @Override
    public Region save(Region region) {
        em.get().getTransaction().begin();
        em.get().persist(region);
        em.get().getTransaction().commit();
        return region;
    }

    @Override
    public Region update(Region sourceRegion) {
        Region targetRegion = find(sourceRegion.getId());
        clone(targetRegion, sourceRegion);
        return save(targetRegion);
    }

    @Override
    public Region create(Region sourceRegion) {
        Region region = new Region();
        clone(region, sourceRegion);
        return save(region);
    }

    private Region clone(Region targetRegion, Region sourceRegion) {
        if (sourceRegion.getDescription() != null) {
            targetRegion.setDescription(sourceRegion.getDescription());
        }
        return targetRegion;
    }
}
