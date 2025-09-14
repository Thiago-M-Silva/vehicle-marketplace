package org.acme.services;

import java.util.List;
import java.util.UUID;

import org.acme.model.Users;
import org.acme.repositories.UsersRepository;

import org.acme.dtos.UsersRequestDTO;
import org.acme.dtos.UsersResponseDTO;
import org.acme.interfaces.UserMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

    @Inject UsersRepository usersRepository;
    @Inject UserMapper userMapper;

    public UsersResponseDTO createUser(UsersRequestDTO data) {
        Users user = userMapper.toUser(data);
        usersRepository.persist(user);
        return userMapper.toUserDTO(user);
    }

    public List<UsersResponseDTO> getAllUsers() {
        List<Users> users = usersRepository.listAll();
        return userMapper.toUserDTOList(users);
    }

    public UsersResponseDTO getUserById(UUID id) {
        Users user = usersRepository.findById(id);
        return user != null ? userMapper.toUserDTO(user) : null;
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

        Users user = usersRepository.find("cpf", data.cpf()).firstResult();
        if (user == null) {
            throw new IllegalArgumentException("User not found with CPF: " + data.cpf());
        }

        userMapper.updateUserFromDTO(data, user);
        usersRepository.persist(user);

        return data;
    }
}
