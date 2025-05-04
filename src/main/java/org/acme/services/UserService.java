package org.acme.services;

import java.util.List;
import java.util.UUID;

import org.acme.model.Users.Users;
import org.acme.model.Users.UsersRepository;
import org.acme.model.Users.UsersRequestDTO;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Users createUser(UsersRequestDTO data) {
        Users user = new Users(data);
        usersRepository.persist(user);
        return user;
    }

    public List<Users> getAllUsers() {
        return usersRepository.listAll();
    }

    public Users getUserById(UUID id) {
        return usersRepository.findById(id);
    }

    public void deleteUser(UUID id) {
        Users user = usersRepository.findById(id);
        if (user != null) {
            usersRepository.delete(user);
        }
    }
}
