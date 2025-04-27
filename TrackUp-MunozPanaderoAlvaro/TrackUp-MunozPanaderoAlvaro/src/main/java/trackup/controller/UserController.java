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

/**
 * Controlador REST para gestionar usuarios
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@RestController // Indica que esta clase es un controlador REST
// @RequestMapping("/api")
@RequestMapping("/api/users") // Prefijo para todas las rutas de este controlador
public class UserController {

    private final UserService userService; // Servicio de usuario

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
        List<UserResponseDTO> usersList = userService.getAllUsers(); // Obtiene todos los usuarios
        if (usersList.isEmpty()) { // Si la lista está vacía, devuelve un código 204 No Content
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(usersList); // Devuelve la lista de usuarios con un código 200 OK
    }

    /**
     * Obtener un usuario por su ID
     *
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/user/">...</a>{id}
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id) {
        if (id < 0) { // Verifica si el ID es negativo
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo
        }

        // otra posible forma corta
        // if (id < 0) return ResponseEntity.badRequest().build();

        Optional<UserResponseDTO> userOpt = userService.getUserById(id); // Obtiene el usuario de acuerdo al ID proporcionado
        return userOpt.map(ResponseEntity::ok) // Si el usuario existe, devuelve el objeto UserResponseDTO
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
        if (username == null || username.trim().isEmpty()) { // Verifica si el nombre de usuario es nulo o vacío
            return ResponseEntity.badRequest().build(); // Devuelve error si el nombre de usuario es nulo o vacío
        }

        Optional<UserResponseDTO> userOpt = userService.getUserByUsername(username); // Obtiene el usuario de acuerdo al nombre de usuario proporcionado
        return userOpt.map(ResponseEntity::ok) // Si el usuario existe, devuelve el objeto UserResponseDTO
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los hábitos que tiene un usuario
     *
     * FUNCIONA
     *
     * @param id ID de usuario del que se quiere obtener la lista de hábitos
     * @return Lista de hábitos del usuario
     *
     * GET <a href="http://localhost:8080/api/user/7/habits">...</a>
     */
    @GetMapping("/user/{id}/habits")
    public ResponseEntity<List<HabitResponseDTO>> findUserHabits(@PathVariable Long id) {
        return userService.getUserEntityById(id) // Obtiene el usuario de acuerdo al ID proporcionado
                .map(user -> { // Si el usuario existe, transforma la lista de hábitos a una lista de HabitResponseDTO
                    List<HabitResponseDTO> habitsList = user.getHabits().stream() // Obtiene la lista de hábitos del usuario
                            .map(HabitResponseDTO::new) // Transforma cada hábito a un HabitResponseDTO
                            .toList(); // Transforma la lista de hábitos a una lista de HabitResponseDTO
                    return ResponseEntity.ok(habitsList); // Devuelve la lista de hábitos con un código 200 OK
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si el usuario no existe, devuelve un código 404 Not Found
    }

    /**
     * Obtiene todos los objetivos que tiene un usuario
     *
     * FALTA PROBAR, HAY QUE HACER ANTES LA IMPLEMENTACION DE LA CLASE Goal
     *
     * @param id ID de usuario del que se quiere obtener la lista de objetivos
     * @return Lista de objetivos del usuario
     */
    @GetMapping("/user/{id}/goals")
    public ResponseEntity<List<GoalResponseDTO>> findUserGoals(@PathVariable Long id) {
        return userService.getUserEntityById(id) // Obtiene el usuario de acuerdo al ID proporcionado
                .map(user -> { // Si el usuario existe, transforma la lista de objetivos a una lista de GoalResponseDTO
                    List<GoalResponseDTO> goalsList = user.getGoals().stream() // Obtiene la lista de objetivos del usuario
                            .map(GoalResponseDTO::new)  // Transforma cada hábito a un GoalResponseDTO
                            .toList(); // Transforma la lista de objetivos a una lista de GoalResponseDTO
                    return ResponseEntity.ok(goalsList); // Devuelve la lista de objetivos con un código 200 OK
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si el usuario no existe, devuelve un código 404 Not Found
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
        if (userRequestDTO == null) { // Verifica si el objeto UserRequestDTO es nulo
            return ResponseEntity.badRequest().build(); // Devuelve error si el objeto UserRequestDTO es nulo
        }

        try {
            UserResponseDTO createdUser = userService.createUser(userRequestDTO); // Crea un nuevo usuario a partir del objeto UserRequestDTO
            return ResponseEntity.status(201).body(createdUser); // Devuelve el usuario creado con un código 201 Created
        } catch (RuntimeException e) { // Captura cualquier excepción que se produzca al crear el usuario
            return ResponseEntity.status(409).body(e.getMessage()); // Devuelve un error 409 Conflict con el mensaje de la excepción
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
        if (id < 0 || userRequestDTO == null) { // Verifica si el ID es negativo o si el objeto UserRequestDTO es nulo
            return ResponseEntity.badRequest().build(); // Devuelve error si el ID es negativo o si el objeto UserRequestDTO es nulo
        }

        try {
            UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO); // Actualiza el usuario a partir del ID y el objeto UserRequestDTO
            return ResponseEntity.ok(updatedUser); // Devuelve el usuario actualizado con un código 200 OK
        } catch (RuntimeException e) { // Captura cualquier excepción que se produzca al actualizar el usuario
            return ResponseEntity.notFound().build(); // Devuelve un error 404 Not Found si el usuario no existe
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
        if (id < 0) { // Verifica si el ID es negativo
            return ResponseEntity.badRequest().build(); // Devuelve error si el ID es negativo
        }

        try {
            userService.deleteUser(id); // Elimina el usuario a partir del ID proporcionado
            return ResponseEntity.noContent().build(); // Devuelve un código 204 No Content si la eliminación fue exitosa
        } catch (RuntimeException e) { // Captura cualquier excepción que se produzca al eliminar el usuario
            return ResponseEntity.notFound().build(); // Devuelve un error 404 Not Found si el usuario no existe
        }
    }

}