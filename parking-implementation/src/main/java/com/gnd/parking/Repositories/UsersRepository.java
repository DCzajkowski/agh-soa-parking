package com.gnd.parking.Repositories;

import com.gnd.parking.Contracts.UsersRepositoryInterface;
import com.gnd.parking.Models.User;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Remote(UsersRepositoryInterface.class)
public class UsersRepository implements UsersRepositoryInterface {
    @Override
    public List<User> all() {
        List<User> users = new ArrayList<>();
        users.add(new User());

        return users;
    }
}
