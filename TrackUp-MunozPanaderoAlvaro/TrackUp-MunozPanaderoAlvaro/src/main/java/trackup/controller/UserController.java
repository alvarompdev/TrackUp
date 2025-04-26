package trackup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trackup.dto.request.UserRequestDTO;
import trackup.dto.response.GoalResponseDTO;
import trackup.dto.response.HabitResponseDTO;
import trackup.dto.response.UserResponseDTO;
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
     * FUNCIONA
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
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/user/">...</a>{id}
     */
    @GetMapping("/user/id/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo
        }

        Optional<UserResponseDTO> userOpt = userService.getUserById(id);
        return userOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * Obtener un usuario por su nombre de usuario
     *
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/user/username/">...</a>{username}
     */
    // @GetMapping("/user/username/{username}")
    @GetMapping("/user/username/{username}")
    public ResponseEntity<UserResponseDTO> findUserByUsername(@PathVariable String username) {
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<UserResponseDTO> userOpt = userService.getUserByUsername(username);
        return userOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{id}/habits")
    public ResponseEntity<List<HabitResponseDTO>> findUserHabits(@PathVariable Long id) {
        return userService.getUserEntityById(id)
                .map(user -> {
                    List<HabitResponseDTO> habitsList = user.getHabits().stream()
                            .map(HabitResponseDTO::new)
                            .toList();
                    return ResponseEntity.ok(habitsList);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{id}/goals")
    public ResponseEntity<List<GoalResponseDTO>> findUserGoals(@PathVariable Long id) {
        return userService.getUserEntityById(id)
                .map(user -> {
                    List<GoalResponseDTO> goalsList = user.getGoals().stream()
                            .map(GoalResponseDTO::new)
                            .toList();
                    return ResponseEntity.ok(goalsList);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crear un nuevo usuario
     *
     * POST <a href="http://localhost:8080/api/user">...</a>
     */
    /*@PostMapping("/user")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        UserResponseDTO createdUser = userService.createUser(userRequestDTO);
        return ResponseEntity.status(201).body(createdUser);
    }*/

    /**
     * FUNCIONA
     *
     *
     * @param userRequestDTO
     * @return
     */
    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            UserResponseDTO createdUser = userService.createUser(userRequestDTO);
            return ResponseEntity.status(201).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    /**
     * Actualizar un usuario existente
     *
     * FUNCIONA (TAMBIEN SE PODRIA HACER A RAIZ DE USERNAME O TAL VEZ SOLO USERNAME PODRIA SER MEJOR)
     *
     * PUT <a href="http://localhost:8080/api/user/">...</a>{id}
     */
    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO) {
        if (id < 0 || userRequestDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Eliminar un usuario por su ID
     *
     * FUNCIONA
     *
     * DELETE <a href="http://localhost:8080/api/user/">...</a>{id}
     */
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}