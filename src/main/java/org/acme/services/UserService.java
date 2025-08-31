package org.acme.services;

import java.util.List;
import java.util.UUID;

import org.acme.model.Users;
import org.acme.repositories.UsersRepository;

import io.vertx.mutiny.ext.auth.User;

import org.acme.dtos.UsersRequestDTO;
import org.acme.dtos.UsersResponseDTO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

    @Inject UsersRepository usersRepository;

    public Users createUser(UsersRequestDTO data) {
        Users user = new Users(data);
        usersRepository.persist(user);
        return user;
    }

    public List<UsersResponseDTO> getAllUsers() {
        List<Users> users = usersRepository.listAll();
        return users.stream()
                .map(UsersResponseDTO::new)
                .toList();
    }

    public UsersResponseDTO getUserById(UUID id) {
        Users user =  usersRepository.findById(id);
        return user != null ? new UsersResponseDTO(user) : null;
    }

    public void deleteUser(UUID id) {
        Users user = usersRepository.findById(id);
        if (user != null) {
            usersRepository.delete(user);
        }
    }

    public UsersRequestDTO editUser(UsersRequestDTO data) {
        if (data.cpf() == null || data.cpf().isEmpty()) {
            throw new IllegalArgumentException("User CPF cannot be null or empty");
        }

        usersRepository.update("cpf like ?1", data.cpf(), data);
        return data;
    }
}
    