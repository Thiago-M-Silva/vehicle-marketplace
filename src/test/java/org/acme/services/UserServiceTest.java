package org.acme.services;

import org.acme.repositories.UsersRepository;
import org.acme.dtos.UsersRequestDTO;
import org.acme.dtos.UsersResponseDTO;
import org.acme.interfaces.UserMapper;
import org.acme.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;
    @Mock
    private StripeService stripeService;
    @Mock
    private UsersRepository usersRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_shouldPersistAndReturnDTO() {
        UsersRequestDTO requestDTO = mock(UsersRequestDTO.class);
        Users user = mock(Users.class);
        UsersResponseDTO responseDTO = mock(UsersResponseDTO.class);

        when(userMapper.toUser(requestDTO)).thenReturn(user);
        when(userMapper.toUserResponseDTO(user)).thenReturn(responseDTO);

        UsersResponseDTO result = userService.createUser(requestDTO);

        verify(usersRepository).persist(user);
        assertEquals(responseDTO, result);
    }

    @Test
    void getAllUsers_shouldReturnMappedDTOList() {
        List<Users> usersList = Arrays.asList(mock(Users.class), mock(Users.class));
        List<UsersResponseDTO> dtoList = Arrays.asList(mock(UsersResponseDTO.class), mock(UsersResponseDTO.class));

        when(usersRepository.listAll()).thenReturn(usersList);
        when(userMapper.toUserResponseDTOList(usersList)).thenReturn(dtoList);

        List<UsersResponseDTO> result = userService.getAllUsers();

        assertEquals(dtoList, result);
    }

    @Test
    void getUserById_shouldReturnDTOIfFound() {
        UUID id = UUID.randomUUID();
        Users user = mock(Users.class);
        UsersResponseDTO dto = mock(UsersResponseDTO.class);

        when(usersRepository.findById(id)).thenReturn(user);
        when(userMapper.toUserResponseDTO(user)).thenReturn(dto);

        UsersResponseDTO result = userService.getUserById(id);

        assertEquals(dto, result);
    }

    @Test
    void getUserById_shouldReturnNullIfNotFound() {
        UUID id = UUID.randomUUID();
        when(usersRepository.findById(id)).thenReturn(null);

        UsersResponseDTO result = userService.getUserById(id);

        assertNull(result);
    }

    @Test
    void deleteUser_shouldDeleteIfFound() {
        UUID id = UUID.randomUUID();
        Users user = mock(Users.class);

        when(usersRepository.findById(id)).thenReturn(user);

        userService.deleteUser(id);

        verify(usersRepository).delete(user);
    }

    @Test
    void deleteUser_shouldDoNothingIfNotFound() {
        UUID id = UUID.randomUUID();
        when(usersRepository.findById(id)).thenReturn(null);

        userService.deleteUser(id);

        verify(usersRepository, never()).delete(any());
    }

    @Test
    void editUser_shouldUpdateAndReturnDTO() {
        UUID id = UUID.randomUUID();
        UsersRequestDTO requestDTO = mock(UsersRequestDTO.class);
        Users user = mock(Users.class);
        UsersResponseDTO responseDTO = mock(UsersResponseDTO.class);

        when(requestDTO.cpf()).thenReturn("12345678900");
        when(usersRepository.findById(id)).thenReturn(user);
        when(userMapper.toUserResponseDTO(user)).thenReturn(responseDTO);

        UsersResponseDTO result = userService.editUser(id, requestDTO);

        verify(userMapper).updateUserFromDTO(requestDTO, user);
        assertEquals(responseDTO, result);
    }

    @Test
    void editUser_shouldThrowIfCpfNullOrEmpty() {
        UUID id = UUID.randomUUID();
        UsersRequestDTO requestDTO = mock(UsersRequestDTO.class);
        when(requestDTO.cpf()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.editUser(id, requestDTO));

        when(requestDTO.cpf()).thenReturn("");
        assertThrows(IllegalArgumentException.class, () -> userService.editUser(id, requestDTO));
    }

    @Test
    void editUser_shouldThrowIfUserNotFound() {
        UUID id = UUID.randomUUID();
        UsersRequestDTO requestDTO = mock(UsersRequestDTO.class);
        when(requestDTO.cpf()).thenReturn("12345678900");
        when(usersRepository.findById(id)).thenReturn(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> userService.editUser(id, requestDTO));
        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void onboardSeller_shouldReturnDTOIfAlreadyOnboarded() throws Exception {
        UUID id = UUID.randomUUID();
        Users user = mock(Users.class);
        UsersResponseDTO dto = mock(UsersResponseDTO.class);

        when(usersRepository.findById(id)).thenReturn(user);
        when(user.getStripeAccountId()).thenReturn("acct_123");
        when(userMapper.toUserResponseDTO(user)).thenReturn(dto);

        UsersResponseDTO result = userService.onboardSeller(id);

        assertEquals(dto, result);
        verify(stripeService, never()).createConnectedAccount(any());
    }

    @Test
    void onboardSeller_shouldCreateStripeAccountIfNotOnboarded() throws Exception {
        UUID id = UUID.randomUUID();
        Users user = mock(Users.class);
        UsersResponseDTO dto = mock(UsersResponseDTO.class);

        when(usersRepository.findById(id)).thenReturn(user);
        when(user.getStripeAccountId()).thenReturn(null);
        when(user.getEmail()).thenReturn("test@example.com");
        when(stripeService.createConnectedAccount("test@example.com")).thenReturn("acct_456");
        when(userMapper.toUserResponseDTO(user)).thenReturn(dto);

        UsersResponseDTO result = userService.onboardSeller(id);

        verify(user).setStripeAccountId("acct_456");
        verify(usersRepository).persist(user);
        assertEquals(dto, result);
    }

    @Test
    void onboardSeller_shouldThrowIfUserNotFound() {
        UUID id = UUID.randomUUID();
        when(usersRepository.findById(id)).thenReturn(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> userService.onboardSeller(id));
        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void generateOnboardingLink_shouldReturnLinkIfStripeAccountExists() throws Exception {
        UUID id = UUID.randomUUID();
        Users user = mock(Users.class);

        when(usersRepository.findById(id)).thenReturn(user);
        when(user.getStripeAccountId()).thenReturn("acct_789");
        when(stripeService.generateOnboardingLink("acct_789")).thenReturn("https://onboarding.link");

        String result = userService.generateOnboardingLink(id);

        assertEquals("https://onboarding.link", result);
    }

    @Test
    void generateOnboardingLink_shouldThrowIfUserNotFound() {
        UUID id = UUID.randomUUID();
        when(usersRepository.findById(id)).thenReturn(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> userService.generateOnboardingLink(id));
        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void generateOnboardingLink_shouldThrowIfNoStripeAccount() {
        UUID id = UUID.randomUUID();
        Users user = mock(Users.class);

        when(usersRepository.findById(id)).thenReturn(user);
        when(user.getStripeAccountId()).thenReturn(null);

        Exception ex = assertThrows(IllegalStateException.class, () -> userService.generateOnboardingLink(id));
        assertTrue(ex.getMessage().contains("User does not have a Stripe account"));
    }
}
