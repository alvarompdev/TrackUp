package trackup.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import trackup.dto.request.UserRequestDTO;
import trackup.dto.response.UserResponseDTO;
import trackup.entity.User;
import trackup.repository.UserRepository;

/**
 * Test de la clase UserServiceImpl
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindUserById_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.findUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
        assertEquals("john@example.com", result.get().getEmail());
        verify(userRepository).findById(1L);
    }

    @Test
    void testFindUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.findUserById(1L);

        assertFalse(result.isPresent());
        verify(userRepository).findById(1L);
    }

    @Test
    void testFindUserEntityById_Success() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserEntityById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void testFindUserByUsername_Success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@example.com");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.findUserByUsername("john");

        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
        verify(userRepository).findByUsername("john");
    }

    @Test
    void testFindUserByUsername_NotFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.findUserByUsername("john");

        assertFalse(result.isPresent());
        verify(userRepository).findByUsername("john");
    }

    @Test
    void testGetAllUsers_UsersExist() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("john");
        user1.setEmail("john@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("jane");
        user2.setEmail("jane@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserResponseDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("john", result.get(0).getUsername());
        assertEquals("jane", result.get(1).getUsername());
        verify(userRepository).findAll();
    }

    @Test
    void testGetAllUsers_NoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.getAllUsers()
        );
        assertEquals("No hay usuarios registrados", exception.getMessage());
        verify(userRepository).findAll();
    }

    @Test
    void testCreateUser_Success() {
        UserRequestDTO requestDTO = new UserRequestDTO("john", "john@example.com", "password123");

        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("john");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("password123");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = userService.createUser(requestDTO);

        assertEquals("john", result.getUsername());
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository).findByUsername("john");
        verify(userRepository).findByEmail("john@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUser_DuplicateUsername() {
        UserRequestDTO requestDTO = new UserRequestDTO("john", "john@example.com", "password123");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(new User()));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.createUser(requestDTO)
        );
        assertEquals("El nombre de usuario ya está en uso", exception.getMessage());
        verify(userRepository).findByUsername("john");
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_DuplicateEmail() {
        UserRequestDTO requestDTO = new UserRequestDTO("john", "john@example.com", "password123");
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(new User()));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.createUser(requestDTO)
        );
        assertEquals("El correo electrónico ya está en uso", exception.getMessage());
        verify(userRepository).findByUsername("john");
        verify(userRepository).findByEmail("john@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        UserRequestDTO requestDTO = new UserRequestDTO("john_new", "john_new@example.com", "new_password");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("john");
        existingUser.setEmail("john@example.com");
        existingUser.setPassword("password123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserResponseDTO result = userService.updateUser(1L, requestDTO);

        assertEquals("john_new", result.getUsername());
        assertEquals("john_new@example.com", result.getEmail());
        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        UserRequestDTO requestDTO = new UserRequestDTO("john_new", "john_new@example.com", "new_password");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.updateUser(1L, requestDTO)
        );
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.deleteUser(1L)
        );
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRepository).existsById(1L);
        verify(userRepository, never()).deleteById(1L);
    }

}