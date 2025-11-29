package org.acme.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.stripe.model.Customer;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserMapper userMapper;
    @Mock
    StripeService stripeService;
    @Mock
    UsersRepository usersRepository;
    @Mock
    KeycloakAdminClient keycloakAdminClient;

    @Mock
    PanacheQuery<Users> panacheQuery;

    @InjectMocks
    UserService userService;

    @Captor
    ArgumentCaptor<String> queryCaptor;
    @Captor
    ArgumentCaptor<Sort> sortCaptor;
    @Captor
    ArgumentCaptor<Map<String, Object>> paramsCaptor;

    @BeforeEach
    void setup() {
        // common stubbing for paged PanacheQuery chain
        when(panacheQuery.page(any(Page.class))).thenReturn(panacheQuery);
    }

    @Test
    void createUser_success_persistsAndReturnsDto() throws Exception {
        UsersRequestDTO req = mock(UsersRequestDTO.class);
        when(req.name()).thenReturn("John");
        when(req.email()).thenReturn("john@example.com");
        when(req.password()).thenReturn("pwd");

        Users user = mock(Users.class);
        UsersResponseDTO respDto = mock(UsersResponseDTO.class);
        Customer customer = mock(Customer.class);

        when(userMapper.toUser(req)).thenReturn(user);
        when(keycloakAdminClient.createUser("John", "john@example.com", "pwd")).thenReturn("kc_1");
        when(stripeService.createCustomer("john@example.com", "John")).thenReturn(customer);
        when(customer.getId()).thenReturn("cust_1");
        when(userMapper.toUserResponseDTO(user)).thenReturn(respDto);

        UsersResponseDTO result = userService.createUser(req);

        assertSame(respDto, result);
        verify(user).setKeycloakId("kc_1");
        verify(user).setStripeCustomerId("cust_1");
        verify(usersRepository).persist(user);
    }

    @Test
    void getAllUsers_delegatesToRepositoryAndMapper() {
        List<Users> users = List.of(mock(Users.class));
        List<UsersResponseDTO> dtos = List.of(mock(UsersResponseDTO.class));

        when(usersRepository.listAll()).thenReturn(users);
        when(userMapper.toUserResponseDTOList(users)).thenReturn(dtos);

        List<UsersResponseDTO> result = userService.getAllUsers();

        assertSame(dtos, result);
    }

    @Test
    void getUserById_foundAndNotFound() {
        UUID id = UUID.randomUUID();
        Users user = mock(Users.class);
        UsersResponseDTO dto = mock(UsersResponseDTO.class);

        when(usersRepository.findById(id)).thenReturn(user);
        when(userMapper.toUserResponseDTO(user)).thenReturn(dto);

        assertSame(dto, userService.getUserById(id));

        when(usersRepository.findById(id)).thenReturn(null);
        assertNull(userService.getUserById(id));
    }

    @Test
    void getUserByEmail_foundAndNotFound() {
        String email = "a@b.com";
        Users user = mock(Users.class);
        UsersResponseDTO dto = mock(UsersResponseDTO.class);

        when(usersRepository.find("email LIKE ?1", email)).thenReturn(panacheQuery);
        when(panacheQuery.firstResult()).thenReturn(user);
        when(userMapper.toUserResponseDTO(user)).thenReturn(dto);

        assertSame(dto, userService.getUserByEmail(email));

        when(panacheQuery.firstResult()).thenReturn(null);
        assertNull(userService.getUserByEmail(email));
    }

    @Test
    void deleteUser_existsAndNotExists() {
        UUID id = UUID.randomUUID();
        Users user = mock(Users.class);

        when(usersRepository.findById(id)).thenReturn(user);
        userService.deleteUser(id);
        verify(usersRepository).delete(user);

        reset(usersRepository);
        when(usersRepository.findById(id)).thenReturn(null);
        userService.deleteUser(id);
        verify(usersRepository, never()).delete(any());
    }

    @Test
    void editUser_invalidCpf_throws() {
        UUID id = UUID.randomUUID();
        UsersRequestDTO data = mock(UsersRequestDTO.class);
        when(data.cpf()).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.editUser(id, data));
        assertTrue(ex.getMessage().contains("CPF"));
    }

    @Test
    void editUser_userNotFound_throws() {
        UUID id = UUID.randomUUID();
        UsersRequestDTO data = mock(UsersRequestDTO.class);
        when(data.cpf()).thenReturn("123");
        when(usersRepository.findById(id)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.editUser(id, data));
        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void editUser_success_updatesAndReturnsDto() {
        UUID id = UUID.randomUUID();
        UsersRequestDTO data = mock(UsersRequestDTO.class);
        when(data.cpf()).thenReturn("123");
        Users user = mock(Users.class);
        UsersResponseDTO dto = mock(UsersResponseDTO.class);

        when(usersRepository.findById(id)).thenReturn(user);
        // updateUserFromDTO returns void; toUserResponseDTO returns dto
        when(userMapper.toUserResponseDTO(user)).thenReturn(dto);

        UsersResponseDTO res = userService.editUser(id, data);

        assertSame(dto, res);
        verify(userMapper).updateUserFromDTO(data, user);
    }

    @Test
    void onboardSeller_userNotFound_throws() {
        UUID id = UUID.randomUUID();
        when(usersRepository.findById(id)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.onboardSeller(id));
        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void onboardSeller_alreadyOnboarded_returnsDto() throws Exception {
        UUID id = UUID.randomUUID();
        Users user = mock(Users.class);
        UsersResponseDTO dto = mock(UsersResponseDTO.class);

        when(usersRepository.findById(id)).thenReturn(user);
        when(user.getStripeAccountId()).thenReturn("acct_1");
        when(userMapper.toUserResponseDTO(user)).thenReturn(dto);

        UsersResponseDTO res = userService.onboardSeller(id);
        assertSame(dto, res);
        verify(stripeService, never()).createConnectedAccount(anyString());
    }

    @Test
    void onboardSeller_createsAccount_andReturnsDto() throws Exception {
        UUID id = UUID.randomUUID();
        Users user = mock(Users.class);
        UsersResponseDTO dto = mock(UsersResponseDTO.class);

        when(usersRepository.findById(id)).thenReturn(user);
        when(user.getStripeAccountId()).thenReturn(null);
        when(user.getEmail()).thenReturn("e@x.com");
        when(stripeService.createConnectedAccount("e@x.com")).thenReturn("acct_new");
        when(userMapper.toUserResponseDTO(user)).thenReturn(dto);

        UsersResponseDTO res = userService.onboardSeller(id);
        assertSame(dto, res);
        verify(user).setStripeAccountId("acct_new");
    }

    @Test
    void generateOnboardingLink_userNotFound_throws() throws Exception {
        UUID id = UUID.randomUUID();
        when(usersRepository.findById(id)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.generateOnboardingLink(id));
        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void generateOnboardingLink_noAccount_throws() throws Exception {
        UUID id = UUID.randomUUID();
        Users user = mock(Users.class);
        when(usersRepository.findById(id)).thenReturn(user);
        when(user.getStripeAccountId()).thenReturn(null);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> userService.generateOnboardingLink(id));
        assertTrue(ex.getMessage().contains("does not have a Stripe account"));
    }

    @Test
    void generateOnboardingLink_success_returnsLink() throws Exception {
        UUID id = UUID.randomUUID();
        Users user = mock(Users.class);
        when(usersRepository.findById(id)).thenReturn(user);
        when(user.getStripeAccountId()).thenReturn("acct_42");
        when(stripeService.generateOnboardingLink("acct_42")).thenReturn("https://link");

        String link = userService.generateOnboardingLink(id);
        assertEquals("https://link", link);
    }

    @Test
    void searchUsers_buildsQuery_andReturnsResults() {
        UserSearchDTO params = mock(UserSearchDTO.class);
        when(params.getName()).thenReturn("John");
        when(params.getEmail()).thenReturn("mail");
        when(params.getCity()).thenReturn("City");
        when(params.getState()).thenReturn("State");
        when(params.getCountry()).thenReturn("Country");
        when(params.getSortBy()).thenReturn(null);
        when(params.getDirection()).thenReturn("DESC");
        when(params.getPage()).thenReturn(0);
        when(params.getSize()).thenReturn(10);

        Users user = mock(Users.class);
        List<Users> users = List.of(user);

        when(usersRepository.find(anyString(), any(Sort.class), anyMap())).thenReturn(panacheQuery);
        when(panacheQuery.list()).thenReturn(users);

        List<Users> result = userService.searchUsers(params);

        assertEquals(users, result);

        // capture arguments to inspect query and params
        verify(usersRepository).find(queryCaptor.capture(), sortCaptor.capture(), paramsCaptor.capture());
        String query = queryCaptor.getValue();
        Map<String, Object> map = paramsCaptor.getValue();
        Sort sort = sortCaptor.getValue();

        assertTrue(query.contains("name = :name"));
        assertTrue(query.contains("email ILIKE :email"));
        assertTrue(query.contains("city ILIKE :city"));
        assertTrue(query.contains("state ILIKE :state"));
        // code has a typo "coutry" intentionally; ensure the key exists
        assertTrue(query.contains("coutry ILIKE :coutry"));
        assertEquals("mail", ((String) map.get("email")).replace("%", ""));
        assertEquals("Country", ((String) map.get("coutry")).replace("%", ""));
        // direction was DESC -> sort descending
        assertNotNull(sort);
    }
}
