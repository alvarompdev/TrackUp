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

    // ----------------------------
    // Tests para findUserById
    // ----------------------------

    @Test
    void testFindUserById_Success() {
        // Given: Un usuario existente
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@example.com");

        // When: El repositorio devuelve el usuario
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Then: El servicio devuelve el DTO correctamente mapeado
        Optional<UserResponseDTO> result = userService.findUserById(1L);
        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
        assertEquals("john@example.com", result.get().getEmail());
        verify(userRepository).findById(1L);
    }

    @Test
    void testFindUserById_NotFound() {
        // Given: El repositorio no encuentra al usuario
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When: Se llama al servicio
        Optional<UserResponseDTO> result = userService.findUserById(1L);

        // Then: No hay resultado y se verifica la llamada al repositorio
        assertFalse(result.isPresent());
        verify(userRepository).findById(1L);
    }

    // ----------------------------
    // Test para findUserEntityById
    // ----------------------------

    @Test
    void testFindUserEntityById_Success() {
        // Given: Un usuario existente
        User user = new User();
        user.setId(1L);

        // When: El repositorio devuelve la entidad User
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Then: El servicio devuelve la entidad User sin mapear
        Optional<User> result = userService.findUserEntityById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(userRepository).findById(1L);
    }

    // ----------------------------
    // Tests para findUserByUsername
    // ----------------------------

    @Test
    void testFindUserByUsername_Success() {
        // Given: Un usuario existente
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@example.com");

        // When: El repositorio devuelve el usuario por nombre de usuario
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        // Then: El servicio devuelve el DTO correctamente mapeado
        Optional<UserResponseDTO> result = userService.findUserByUsername("john");
        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
        verify(userRepository).findByUsername("john");
    }

    @Test
    void testFindUserByUsername_NotFound() {
        // Given: El repositorio no encuentra al usuario por nombre de usuario
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        // When: Se llama al servicio
        Optional<UserResponseDTO> result = userService.findUserByUsername("john");

        // Then: No hay resultado y se verifica la llamada al repositorio
        assertFalse(result.isPresent());
        verify(userRepository).findByUsername("john");
    }

    // ----------------------------
    // Tests para getAllUsers
    // ----------------------------

    @Test
    void testGetAllUsers_UsersExist() {
        // Given: Dos usuarios existentes
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("john");
        user1.setEmail("john@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("jane");
        user2.setEmail("jane@example.com");

        // When: El repositorio devuelve una lista de usuarios
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Then: Se devuelve la lista correctamente mapeada
        List<UserResponseDTO> result = userService.getAllUsers();
        assertEquals(2, result.size());
        assertEquals("john", result.get(0).getUsername());
        assertEquals("jane", result.get(1).getUsername());
        verify(userRepository).findAll();
    }

    @Test
    void testGetAllUsers_NoUsers() {
        // Given: El repositorio devuelve una lista vacía
        when(userRepository.findAll()).thenReturn(List.of());

        // When: Se llama al servicio y se espera una excepción
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.getAllUsers()
        );

        // Then: La excepción tiene el mensaje correcto y se verifica la llamada al repositorio
        assertEquals("No hay usuarios registrados", exception.getMessage());
        verify(userRepository).findAll();
    }

    // ----------------------------
    // Tests para createUser
    // ----------------------------

    @Test
    void testCreateUser_Success() {
        // Given: Un DTO válido y repositorio vacío (sin duplicados)
        UserRequestDTO requestDTO = new UserRequestDTO("john", "john@example.com", "password123");
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        // When: Se crea un nuevo usuario
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("john");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("password123");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Then: El servicio devuelve el DTO correctamente mapeado y se verifican las llamadas al repositorio
        UserResponseDTO result = userService.createUser(requestDTO);
        assertEquals("john", result.getUsername());
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository).findByUsername("john");
        verify(userRepository).findByEmail("john@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUser_DuplicateUsername() {
        // Given: Un nombre de usuario duplicado
        UserRequestDTO requestDTO = new UserRequestDTO("john", "john@example.com", "password123");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(new User()));

        // When: Se intenta crear un usuario con nombre duplicado
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.createUser(requestDTO)
        );

        // Then: Se lanza la excepción correcta y no se llama a métodos innecesarios
        assertEquals("El nombre de usuario ya está en uso", exception.getMessage());
        verify(userRepository).findByUsername("john");
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_DuplicateEmail() {
        // Given: Un email duplicado
        UserRequestDTO requestDTO = new UserRequestDTO("john", "john@example.com", "password123");
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(new User()));

        // When: Se intenta crear un usuario con email duplicado
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.createUser(requestDTO)
        );

        // Then: Se lanza la excepción correcta y no se llama al save
        assertEquals("El correo electrónico ya está en uso", exception.getMessage());
        verify(userRepository).findByUsername("john");
        verify(userRepository).findByEmail("john@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    // ----------------------------
    // Tests para updateUser
    // ----------------------------

    @Test
    void testUpdateUser_Success() {
        // Given: Un usuario existente
        UserRequestDTO requestDTO = new UserRequestDTO("john_new", "john_new@example.com", "new_password");
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("john");
        existingUser.setEmail("john@example.com");
        existingUser.setPassword("password123");

        // When: Se actualizan los campos del usuario
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Then: El servicio devuelve el DTO actualizado y se verifica la llamada al repositorio
        UserResponseDTO result = userService.updateUser(1L, requestDTO);
        assertEquals("john_new", result.getUsername());
        assertEquals("john_new@example.com", result.getEmail());
        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        // Given: El usuario no existe
        UserRequestDTO requestDTO = new UserRequestDTO("john_new", "john_new@example.com", "new_password");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When: Se intenta actualizar un usuario inexistente
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.updateUser(1L, requestDTO)
        );

        // Then: Se lanza la excepción correcta y no se llama al save
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any(User.class));
    }

    // ----------------------------
    // Tests para deleteUser
    // ----------------------------

    @Test
    void testDeleteUser_Success() {
        // Given: El usuario existe
        when(userRepository.existsById(1L)).thenReturn(true);

        // When: Se elimina el usuario
        userService.deleteUser(1L);

        // Then: Se llama a deleteById
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        // Given: El usuario no existe
        when(userRepository.existsById(1L)).thenReturn(false);

        // When: Se intenta eliminar un usuario inexistente
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.deleteUser(1L)
        );

        // Then: Se lanza la excepción correcta y no se llama a deleteById
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRepository).existsById(1L);
        verify(userRepository, never()).deleteById(1L);
    }

}