package trackup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trackup.dto.request.UserRequestDTO;
import trackup.dto.response.GoalResponseDTO;
import trackup.dto.response.HabitResponseDTO;
import trackup.dto.response.UserResponseDTO;
import trackup.entity.User;
import trackup.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * Constructor con inyección de dependencias
     *
     * @param userService Servicio de usuario
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Obtener todos los usuarios
     *
     * GET <a href="http://localhost:8080/api/users">...</a>
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> findAllUsers() {
        List<UserResponseDTO> usersList = userService.getAllUsers();
        if (usersList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usersList);
    }

    /**
     * Obtener un usuario por su ID
     *
     * GET <a href="http://localhost:8080/api/user/">...</a>{id}
     */
    @GetMapping("/user/id/{id}")
    /*public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            UserResponseDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }*/
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo
        }

        // Usamos el Optional para comprobar si el usuario existe
        Optional<UserResponseDTO> userResponseDTO = userService.getUserById(id);

        // Si el usuario existe, devolvemos el DTO con 200 OK, si no, devolvemos 404 Not Found
        return userResponseDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * Obtener un usuario por su nombre de usuario
     *
     * GET <a href="http://localhost:8080/api/user/username/">...</a>{username}
     */
    // @GetMapping("/user/username/{username}")
    @GetMapping("/user/username/{username}")
    public ResponseEntity<User> findUserByUsername(@PathVariable String username) {
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{id}/habits")
    public ResponseEntity<List<HabitResponseDTO>> findUserHabits(@PathVariable Long id) {
        return userService.getUserEntityById(id)
                .map(user -> {
                    List<HabitResponseDTO> habitDTOs = user.getHabits().stream()
                            .map(HabitResponseDTO::new)
                            .toList();
                    return ResponseEntity.ok(habitDTOs);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{id}/goals")
    public ResponseEntity<List<GoalResponseDTO>> findUserGoals(@PathVariable Long id) {
        return userService.getUserEntityById(id)
                .map(user -> {
                    List<GoalResponseDTO> goalDTOs = user.getGoals().stream()
                            .map(GoalResponseDTO::new)
                            .toList();
                    return ResponseEntity.ok(goalDTOs);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crear un nuevo usuario
     *
     * POST <a href="http://localhost:8080/api/user">...</a>
     */
    @PostMapping("/user")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.createUser(userRequestDTO));
    }

    /**
     * Actualizar un usuario existente
     *
     * PUT <a href="http://localhost:8080/api/user/">...</a>{id}
     */
    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userRequestDTO));
    }

    /**
     * Eliminar un usuario por su ID
     *
     * DELETE <a href="http://localhost:8080/api/user/">...</a>{id}
     */
    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}