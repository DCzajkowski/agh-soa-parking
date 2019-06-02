package com.gnd.parking.Repositories;

import com.gnd.parking.Contracts.RegionsRepositoryInterface;
import com.gnd.parking.Contracts.UsersRepositoryInterface;
import com.gnd.parking.EntityManagers.ParkingEntityManager;
import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.Region;
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

    @EJB
    private RegionsRepositoryInterface regionsRepository;

    @Override
    public List<User> all() {
        return em.get()
            .createQuery("SELECT u FROM User u")
            .getResultList();
    }

    @Override
    public  User find(Integer id) {
        return em.get().find(User.class, id);
    }

    @Override
    public Boolean delete(Integer id) {
        User user = find(id);
        if (user != null) {
            em.get().remove(user);
            return true;
        }
        return false;
    }

    @Override
    public User save(User user) {
        em.get().getTransaction().begin();
        em.get().persist(user);
        em.get().getTransaction().commit();
        return user;
    }

    @Override
    public User create(User sourceUser) throws NestedObjectNotFoundException {
        User targetUser = new User();
        clone(targetUser, sourceUser);
        return save(targetUser);
    }

    @Override
    public User update(User sourceUser) throws NestedObjectNotFoundException {
        User targetUser = find(sourceUser.getId());
        clone(targetUser, sourceUser);
        return save(targetUser);
    }

     private User clone(User targetUser, User sourceUser) throws NestedObjectNotFoundException {
        if (sourceUser.getPassword() != null) {
            targetUser.setPassword(sourceUser.getPassword());
        }
        if (sourceUser.getRegion() != null) {
            Integer sourceRegionId = sourceUser.getRegion().getId();
            if (sourceRegionId != 0){
                Region newRegion = regionsRepository.find(sourceRegionId);
                if (newRegion == null) {
                    throw new NestedObjectNotFoundException("Region not found");
                }
                targetUser.setRegion(newRegion);
            } else {
                targetUser.setRegion(null);
            }
        }
        if (sourceUser.getRole() != null) {
            targetUser.setRole(sourceUser.getRole());
        }
        if (sourceUser.getUsername() != null) {
            targetUser.setUsername(sourceUser.getUsername());
        }
        return targetUser;
    }
}
