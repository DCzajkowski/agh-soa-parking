package com.gnd.parking.EntityManagers;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

@Singleton
public class ParkingEntityManager {
    private EntityManager em;

    public ParkingEntityManager() {
        em = Persistence.createEntityManagerFactory("Parking").createEntityManager();
    }

    public EntityManager get() {
        return em;
    }
}
