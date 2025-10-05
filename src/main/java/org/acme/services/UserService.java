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
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {

    @Inject UserMapper userMapper;
    @Inject StripeService stripeService;
    @Inject UsersRepository usersRepository;

    /**
     * Creates a new user in the system
     * <p>
     * Receive a {@link UsersRequestDTO} containing the user information.
     * Convert it to a {@link Users} entity and persist it in the database.
     * </p>
     *
     * @param {@link UsersRequestDTO} data containing the user information
     * @return a {@link UsersResponseDTO} representing the created user
     * @throws IllegalArgumentException if the user CPF is null or empty
     */
    @Transactional
    public UsersResponseDTO createUser(UsersRequestDTO data) {
        Users user = userMapper.toUser(data);
        usersRepository.persist(user);
        return userMapper.toUserDTO(user);
    }

    /**
     * Retrieves all users in the system
     * <p>
     * Get a list of all users stored in database.
     * Convert it to a {@link List<UsersResponseDTO>} and return.
     * </p>
     *
     * @return a {@link List<UsersResponseDTO>} representing all users
     */
    public List<UsersResponseDTO> getAllUsers() {
        List<Users> users = usersRepository.listAll();
        return userMapper.toUserDTOList(users);
    }

    /**
     * Retrieves a user by their unique identifier
     * <p>
     * Receive a UUID id containing the user unique identifier.
     * Find the user in the database and return it as a {@link UsersResponseDTO}.
     * </p>
     *
     * @param UUID id containing the user unique identifier
     * @return a {@link UsersResponseDTO} representing the found user, or null if not found
     */
    public UsersResponseDTO getUserById(UUID id) {
        Users user = usersRepository.findById(id);
        return user != null ? userMapper.toUserDTO(user) : null;
    }

    /**
     * Deletes a user by their unique identifier
     * <p>
     * Receive a UUID id containing the user unique identifier.
     * Find the user in the database and delete it if found.
     * </p>
     *
     * @param UUID id containing the user unique identifier
     */
    @Transactional
    public void deleteUser(UUID id) {
        Users user = usersRepository.findById(id);
        if (user != null) {
            usersRepository.delete(user);
        }
    }

    /**
     * Edits an existing user in the system
     * <p>
     * Receive a UUID id containing the user unique identifier and a {@link UsersRequestDTO} with updated information.
     * Find the user in the database, update its information, and return it as a {@link UsersResponseDTO}.
     * </p>
     *
     * @param UUID id containing the user unique identifier
     * @param {@link UsersRequestDTO} data containing the updated user information
     * @return a {@link UsersResponseDTO} representing the updated user
     * @throws IllegalArgumentException if the user CPF is null or empty, or if the user is not found
     */
    @Transactional
    public UsersResponseDTO editUser(UUID id, UsersRequestDTO data) {
        if (data.cpf() == null || data.cpf().isEmpty()) {
            throw new IllegalArgumentException("User CPF cannot be null or empty");
        }

        Users user = usersRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userMapper.updateUserFromDTO(data, user);
        return userMapper.toUserDTO(user);
    }

    /**
     * Onboards a user as a seller by creating a Stripe connected account if one does not already exist.
     * <p>
     * If the user already has a Stripe account ID, returns the user data as a DTO.
     * Otherwise, creates a new Stripe connected account, updates the user with the new account ID,
     * persists the changes, and returns the updated user data as a DTO.
     * </p>
     *
     * @param userId the unique identifier of the user to onboard as a seller
     * @return a {@link UsersResponseDTO} representing the onboarded seller
     * @throws IllegalArgumentException if the user with the specified ID is not found
     * @throws Exception if an error occurs during the Stripe account creation or persistence process
     */
    public UsersResponseDTO onboardSeller(UUID userId) throws Exception {
        Users user = usersRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        if(user.getStripeAccountId() != null) {
            return userMapper.toUserDTO(user); 
        }

        String accountId = stripeService.createConnectedAccount(user.getEmail());

        user.setStripeAccountId(accountId);
        usersRepository.persist(user);

        return userMapper.toUserDTO(user);
    }

    /**
     * Generates an onboarding link for the specified user using their Stripe account ID.
     *
     * @param userId the UUID of the user for whom to generate the onboarding link
     * @return the onboarding link as a String
     * @throws IllegalArgumentException if the user is not found with the given ID
     * @throws IllegalStateException if the user does not have a Stripe account ID
     * @throws Exception if an error occurs during onboarding link generation
     */
    public String generateOnboardingLink(UUID userId) throws Exception {
        Users user = usersRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        if (user.getStripeAccountId() == null) {
            throw new IllegalStateException("User does not have a Stripe account. Please onboard first.");
        }

        return stripeService.generateOnboardingLink(user.getStripeAccountId());
    }
}
