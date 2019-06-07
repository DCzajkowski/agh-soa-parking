package com.gnd.parking.Contracts.Repositories;

import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.User;

import java.util.List;

public interface UsersRepositoryInterface {
    List<User> all();
    User find(Integer id);
    User find(String username);
    Boolean delete(Integer id);
    User update(User sourceUser) throws NestedObjectNotFoundException;
    User create(User sourceUser) throws NestedObjectNotFoundException;
    User save(User user);
}
