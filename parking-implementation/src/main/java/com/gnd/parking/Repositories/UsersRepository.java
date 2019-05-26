package com.gnd.parking.Repositories;

import com.gnd.parking.Contracts.UsersRepositoryInterface;
import com.gnd.parking.EntityManagers.ParkingEntityManager;
import com.gnd.parking.Models.User;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import java.util.List;

@Singleton
@Remote(UsersRepositoryInterface.class)
public class UsersRepository implements UsersRepositoryInterface {
    @EJB
    ParkingEntityManager em;

    @Override
    public List<User> all() {
        return em.get()
            .createQuery("SELECT u FROM User u")
            .getResultList();
    }
}
