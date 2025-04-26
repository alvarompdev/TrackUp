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
@Service
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

    /*@Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapToDTO(user);
    }*/
    @Override
    public Optional<UserResponseDTO> getUserById(Long id) {
        return userRepository.findById(id) // Busca al usuario por su ID, y si lo encuentra lo transforma a un DTO
                .map(this::mapToDTO);
    }

    @Override
    public Optional<User> getUserEntityById(Long id) {
        return userRepository.findById(id); // Retorna directamente la entidad User en el caso de que exista
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll(); // Obtiene todos los usuarios existentes en la base de datos

        if (users.isEmpty()) { // Si no hay usuarios, lanza una excepción
            throw new RuntimeException("No hay usuarios registrados");
        }

        return users.stream() // Transforma cada entidad User a un UserResponseDTO y lo devuelve
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /*@Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username); // Busca un usuario por su nombre de usuario
    }*/
    @Override
    public Optional<UserResponseDTO> getUserByUsername(String username) {
        return userRepository.findByUsername(username) // Busca al usuario por su ID, y si lo encuentra lo transforma a un DTO
                .map(this::mapToDTO);
    }

    /*@Override
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        User user = new User(); // Se crea una nueva entidad User a partir del DTO recibido
        user.setUsername(userDTO.getUsername()); // Se asignan los valores del DTO a la entidad
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        User savedUser = userRepository.save(user); // Se guarda la entidad en la base de datos
        return mapToDTO(savedUser); // Se transforma la entidad guardada a un DTO y se devuelve
    }*/

    @Override
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está en uso");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        User savedUser = userRepository.save(user);
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
     * @param userDTO DTO a convertir
     * @return Entidad User
     */
    private User mapToEntity(UserRequestDTO userDTO) {
        User user = new User(); // Crea una nueva entidad User

        user.setUsername(userDTO.getUsername()); // Asigna los valores del DTO a la entidad
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        return user; // Devuelve la entidad User
    }

}