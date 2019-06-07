package com.gnd.parking.Services.Auth;

import com.gnd.parking.Contracts.Repositories.UsersRepositoryInterface;
import com.gnd.parking.Contracts.Services.Auth.AuthenticatorServiceInterface;
import com.gnd.parking.Models.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Singleton;

@Singleton
@Remote(AuthenticatorServiceInterface.class)
public class AuthenticatorService implements AuthenticatorServiceInterface {
    @EJB
    UsersRepositoryInterface usersRepository;

    public boolean authenticate(String username, String password) {
        User user = usersRepository.find(username);

        if (user == null) {
            return false;
        }

        String hashedPassword = user.getPassword();

        return BCrypt.checkpw(password, hashedPassword);
    }
}
