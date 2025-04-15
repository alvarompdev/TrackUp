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

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapToDTO(user);
    }

    @Override
    public Optional<User> getUserEntityById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }

    private UserResponseDTO mapToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    private User mapToEntity(UserRequestDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return user;
    }

}