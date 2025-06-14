package trackup.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trackup.dto.request.UserRequestDTO;
import trackup.dto.response.UserResponseDTO;
import trackup.entity.User;
import trackup.repository.UserRepository;
import trackup.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de usuarios
 * Se encarga de la lógica de negocio relacionada con los usuarios,
 * incluyendo la creación, actualización, eliminación y obtención de usuarios
 *
 * Utiliza un repositorio para acceder a la base de datos y
 * convierte las entidades a DTOs para mantener la separación de capas
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Service // Anotación que indica que esta clase es un servicio
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; // Repositorio de usuarios

    /**
     * Constructor con inyección de dependencias
     *
     * @param userRepository Repositorio de usuariosA
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserResponseDTO> findUserById(Long id) {
        return userRepository.findById(id) // Busca al usuario por su ID, y si lo encuentra lo transforma a un DTO
                .map(this::mapToDTO);
    }

    @Override
    public Optional<User> findUserEntityById(Long id) {
        return userRepository.findById(id); // Retorna directamente la entidad User en el caso de que exista
    }

    @Override
    public Optional<UserResponseDTO> findUserByUsername(String username) {
        return userRepository.findByUsername(username) // Busca al usuario por su ID, y si lo encuentra lo transforma a un DTO
                .map(this::mapToDTO);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll(); // Obtiene todos los usuarios existentes en la base de datos

        if (users.isEmpty()) { // Si no hay usuarios, lanza una excepción
            throw new RuntimeException("No hay usuarios registrados");
        }

        return users.stream() // Transforma cada entidad User a un UserResponseDTO y lo devuelve
                .map(this::mapToDTO) // Transforma cada entidad User a un UserResponseDTO
                .collect(Collectors.toList()); // Recoge todos los DTOs en una lista
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) { // Verifica si el nombre de usuario ya existe
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) { // Verifica si el correo electrónico ya existe
            throw new RuntimeException("El correo electrónico ya está en uso");
        }

        User user = new User(); // Crea una nueva entidad User
        user.setUsername(userDTO.getUsername()); // Asigna los valores del DTO a la entidad
        user.setEmail(userDTO.getEmail()); // Se asigna el correo electrónico
        user.setPassword(userDTO.getPassword()); // Se asigna la contraseña

        User savedUser = userRepository.save(user); // Guarda la entidad en la base de datos
        return mapToDTO(savedUser);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userDTO) {
        User user = userRepository.findById(id) // Busca al usuario por su ID
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")); // Si no lo encuentra, lanza una excepción

        user.setUsername(userDTO.getUsername()); // Se actualizan los valores del usuario
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        User updatedUser = userRepository.save(user); // Se guarda la entidad actualizada en la base de datos
        return mapToDTO(updatedUser); // Se transforma la entidad actualizada a un DTO y se devuelve
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) { // Verifica si el usuario existe
            throw new RuntimeException("Usuario no encontrado"); // Si no existe, lanza una excepción
        }

        userRepository.deleteById(id); // Si existe, lo elimina de la base de datos
    }

    /**
     * Convierte una entidad User a un DTO UserResponseDTO
     *
     * @param user Entidad User a convertir
     * @return DTO UserResponseDTO
     */
    private UserResponseDTO mapToDTO(User user) {
        return new UserResponseDTO( // Crea un nuevo DTO a partir de la entidad User
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    /**
     * Convierte un DTO UserRequestDTO a una entidad User
     *
     * COMENTADO PORQUE SE MARCA COMO NO USADO HASTA EL MOMENTO
     *
     * @param userDTO DTO a convertir
     * @return Entidad User
     */
    /*private User mapToEntity(UserRequestDTO userDTO) {
        User user = new User(); // Crea una nueva entidad User

        user.setUsername(userDTO.getUsername()); // Asigna los valores del DTO a la entidad
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        return user; // Devuelve la entidad User
    }*/

}