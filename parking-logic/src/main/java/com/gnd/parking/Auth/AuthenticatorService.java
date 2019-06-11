package com.gnd.parking.Auth;

import com.gnd.parking.Contracts.Repositories.UsersRepositoryInterface;
import com.gnd.parking.Contracts.Services.Auth.AuthenticatorServiceInterface;
import com.gnd.parking.Exceptions.NestedObjectNotFoundException;
import com.gnd.parking.Models.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;

@Singleton
@Remote(AuthenticatorServiceInterface.class)
public class AuthenticatorService implements AuthenticatorServiceInterface {
    @EJB(lookup = "java:global/parking-implementation-1.0/UsersRepository")
    UsersRepositoryInterface usersRepository;

    @Override
    public boolean authenticate(String username, String password) {
        User user = usersRepository.find(username);

        if (user == null) {
            return false;
        }

        String hashedPassword = user.getPassword();

        return BCrypt.checkpw(password, hashedPassword);
    }

    @Override
    public boolean changePassword(User user, String newPassword) {
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));

        try {
            usersRepository.update(user);

            return true;
        } catch (NestedObjectNotFoundException e) {
            throw new RuntimeException("This should not happen, as there was no change in region.");
        }
    }

    @Override
    public boolean changePassword(User user, String oldPassword, String newPassword) {
        boolean valid = BCrypt.checkpw(oldPassword, user.getPassword());

        if (!valid) {
            return false;
        }

        return changePassword(user, newPassword);
    }
}
