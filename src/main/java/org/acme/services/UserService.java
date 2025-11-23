package org.acme.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.acme.dtos.UserSearchDTO;
import org.acme.dtos.UsersRequestDTO;
import org.acme.dtos.UsersResponseDTO;
import org.acme.infra.KeycloakAdminClient;
import org.acme.interfaces.UserMapper;
import org.acme.model.Users;
import org.acme.repositories.UsersRepository;

import com.stripe.model.Customer;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserService {

    @Inject UserMapper userMapper;
    @Inject StripeService stripeService;
    @Inject UsersRepository usersRepository;
    @Inject KeycloakAdminClient keycloakAdminClient;

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
    public UsersResponseDTO createUser(UsersRequestDTO data) throws Exception {
        Users user = userMapper.toUser(data);
        String userKeycloakId = keycloakAdminClient.createUser(
                data.name(),
                data.email(),
                data.password()
        );
        Customer customer = stripeService.createCustomer(data.email(), data.name());

        user.setKeycloakId(userKeycloakId);
        user.setStripeCustomerId(customer.getId());
        usersRepository.persist(user);

        return userMapper.toUserResponseDTO(user);
    }

    /**
     * Retrieves all users in the system
     * <p>
     * Get a list of all users stored in database. Convert it to a
     * {@link List<UsersResponseDTO>} and return.
     * </p>
     *
     * @return a {@link List<UsersResponseDTO>} representing all users
     */
    public List<UsersResponseDTO> getAllUsers() {
        List<Users> users = usersRepository.listAll();
        return userMapper.toUserResponseDTOList(users);
    }

    /**
     * Retrieves a user by their unique identifier
     * <p>
     * Receive a UUID id containing the user unique identifier. Find the user in
     * the database and return it as a {@link UsersResponseDTO}.
     * </p>
     *
     * @param UUID id containing the user unique identifier
     * @return a {@link UsersResponseDTO} representing the found user, or null
     * if not found
     */
    public UsersResponseDTO getUserById(UUID id) {
        Users user = usersRepository.findById(id);
        return user != null ? userMapper.toUserResponseDTO(user) : null;
    }

    /**
     * Retrieves a user by their unique identifier
     * <p>
     * Receive a UUID id containing the user unique identifier. Find the user in
     * the database and return it as a {@link UsersResponseDTO}.
     * </p>
     *
     * @param UUID id containing the user unique identifier
     * @return a {@link UsersResponseDTO} representing the found user, or null
     * if not found
     */
    public UsersResponseDTO getUserByEmail(String email) {
        Users user = usersRepository.find("email LIKE ?1", email).firstResult();
        return user != null ? userMapper.toUserResponseDTO(user) : null;
    }

    /**
     * Deletes a user by their unique identifier
     * <p>
     * Receive a UUID id containing the user unique identifier. Find the user in
     * the database and delete it if found.
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
     * Receive a UUID id containing the user unique identifier and a
     * {@link UsersRequestDTO} with updated information. Find the user in the
     * database, update its information, and return it as a
     * {@link UsersResponseDTO}.
     * </p>
     *
     * @param UUID id containing the user unique identifier
     * @param {@link UsersRequestDTO} data containing the updated user
     * information
     * @return a {@link UsersResponseDTO} representing the updated user
     * @throws IllegalArgumentException if the user CPF is null or empty, or if
     * the user is not found
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
        return userMapper.toUserResponseDTO(user);
    }

    /**
     * Onboards a user as a seller by creating a Stripe connected account if one
     * does not already exist.
     * <p>
     * If the user already has a Stripe account ID, returns the user data as a
     * DTO. Otherwise, creates a new Stripe connected account, updates the user
     * with the new account ID, persists the changes, and returns the updated
     * user data as a DTO.
     * </p>
     *
     * @param userId the unique identifier of the user to onboard as a seller
     * @return a {@link UsersResponseDTO} representing the onboarded seller
     * @throws IllegalArgumentException if the user with the specified ID is not
     * found
     * @throws Exception if an error occurs during the Stripe account creation
     * or persistence process
     */
    @Transactional
    public UsersResponseDTO onboardSeller(UUID userId) throws Exception {
        Users user = usersRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }

        if (user.getStripeAccountId() != null) {
            return userMapper.toUserResponseDTO(user);
        }

        String accountId = stripeService.createConnectedAccount(user.getEmail());

        user.setStripeAccountId(accountId);

        return userMapper.toUserResponseDTO(user);
    }

    /**
     * Generates an onboarding link for the specified user using their Stripe
     * account ID.
     *
     * @param userId the UUID of the user for whom to generate the onboarding
     * link
     * @return the onboarding link as a String
     * @throws IllegalArgumentException if the user is not found with the given
     * ID
     * @throws IllegalStateException if the user does not have a Stripe account
     * ID
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

    /**
     * Searches for users based on the provided search parameters.
     * 
     * @param searchParams A DTO containing search criteria including:
     *                    - name: exact match for user name
     *                    - email: partial match for email (case insensitive)
     *                    - city: partial match for city (case insensitive)
     *                    - state: partial match for state (case insensitive)
     *                    - country: partial match for country (case insensitive)
     *                    - sortBy: field to sort results by (defaults to "createDate")
     *                    - direction: sort direction ("ASC" or "DESC", defaults to "ASC")
     *                    - page: page number (zero-based, minimum 0)
     *                    - size: page size (minimum 1)
     * 
     * @return A List of Users matching the search criteria, paginated according to the specified page and size
     */
    public List<Users> searchUsers(UserSearchDTO searchParams){
        StringBuilder query = new StringBuilder("1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (searchParams.getName() != null && !searchParams.getName().isBlank()) {
            query.append(" AND name = :name");
            params.put("name", searchParams.getName());
        }
        if (searchParams.getEmail() != null && !searchParams.getEmail().isBlank()) {
            query.append(" AND email ILIKE :email");
            params.put("email", "%" + searchParams.getEmail() + "%");
        }
        if (searchParams.getCity() != null && !searchParams.getCity().isBlank()) {
            query.append(" AND city ILIKE :city");
            params.put("city", "%" + searchParams.getCity() + "%");
        }
        if (searchParams.getState() != null && !searchParams.getState().isBlank()) {
            query.append(" AND state ILIKE :state");
            params.put("state", "%" + searchParams.getState() + "%");
        }
        if (searchParams.getCountry() != null && !searchParams.getCountry().isBlank()) {
            query.append(" AND coutry ILIKE :coutry");
            params.put("coutry", "%" + searchParams.getCountry() + "%");
        }

        String sortBy = (searchParams.getSortBy() == null || searchParams.getSortBy().isBlank())
                ? "createDate" : searchParams.getSortBy();

        String direction = (searchParams.getDirection() == null || searchParams.getDirection().isBlank())
                ? "ASC" : searchParams.getDirection().toUpperCase();

        Sort sort = direction.equals("DESC")
                ? Sort.descending(sortBy)
                : Sort.ascending(sortBy);

        var panacheQuery = usersRepository.find(query.toString(), sort, params);

        var page = Page.of(Math.max(0, searchParams.getPage()), Math.max(1, searchParams.getSize()));
        List<Users> result = panacheQuery
                .page(page)
                .list();

        return result;
    }
}
