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
 * Acceso: <a href="http://localhost:8080/api/users">...</a>
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@RestController // Indica que esta clase es un controlador REST
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
     * GET <a href="http://localhost:8080/api/users/users">...</a>
     *
     * @return Lista de usuarios
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
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
     * GET <a href="http://localhost:8080/api/users/user/1">...</a>
     *
     * @param id ID del usuario
     * @return Usuario encontrado
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        if (id < 0) { // Verifica si el ID es negativo
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo
        }

        Optional<UserResponseDTO> userOpt = userService.findUserById(id); // Obtiene el usuario de acuerdo al ID proporcionado
        return userOpt.map(ResponseEntity::ok) // Si el usuario existe, devuelve el objeto UserResponseDTO
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtener un usuario por su nombre de usuario
     *
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/users/user/username/alvaromp">...</a>
     *
     * @param username Nombre de usuario
     * @return Usuario encontrado
     */
    @GetMapping("/user/username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        if (username == null || username.trim().isEmpty()) { // Verifica si el nombre de usuario es nulo o vacío
            return ResponseEntity.badRequest().build(); // Devuelve error si el nombre de usuario es nulo o vacío
        }

        Optional<UserResponseDTO> userOpt = userService.findUserByUsername(username); // Obtiene el usuario de acuerdo al nombre de usuario proporcionado
        return userOpt.map(ResponseEntity::ok) // Si el usuario existe, devuelve el objeto UserResponseDTO
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los hábitos que tiene un usuario
     *
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/users/user/1/habits">...</a>
     *
     * @param id ID de usuario del que se quiere obtener la lista de hábitos
     * @return Lista de hábitos del usuario
     */
    @GetMapping("/user/{id}/habits")
    public ResponseEntity<List<HabitResponseDTO>> getUserHabits(@PathVariable Long id) {
        return userService.findUserEntityById(id) // Obtiene el usuario de acuerdo al ID proporcionado
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
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/users/user/1/goals">...</a>
     *
     * @param id ID de usuario del que se quiere obtener la lista de objetivos
     * @return Lista de objetivos del usuario
     */
    @GetMapping("/user/{id}/goals")
    public ResponseEntity<List<GoalResponseDTO>> getUserGoals(@PathVariable Long id) {
        return userService.findUserEntityById(id) // Obtiene el usuario de acuerdo al ID proporcionado
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
     * FUNCIONA
     *
     * POST <a href="http://localhost:8080/api/users/user">...</a>
     *
     * @param userRequestDTO Datos del usuario que se va a crear
     * @return Usuario creado con sus datos de respuesta
     */
    @PostMapping("/user")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            return ResponseEntity.badRequest().build(); // Devuelve 400 si el cuerpo es nulo
        }

        UserResponseDTO createdUser = userService.createUser(userRequestDTO); // Crea el usuario
        return ResponseEntity.status(201).body(createdUser); // Devuelve 201 Created con el usuario creado
    }


    /**
     * Actualizar un usuario existente
     *
     * FUNCIONA (TAMBIEN SE PODRIA HACER A RAIZ DE USERNAME O TAL VEZ SOLO USERNAME PODRIA SER MEJOR)
     *
     * PUT <a href="http://localhost:8080/api/users/user">...</a>
     *
     * @param id ID del usuario que se va a actualizar
     * @param userRequestDTO Datos del usuario que se va a actualizar
     * @return Usuario actualizado
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
     * DELETE <a href="http://localhost:8080/api/users/user/1">...</a>
     *
     * @param id ID del usuario que se va a eliminar
     * @return Código de respuesta HTTP
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