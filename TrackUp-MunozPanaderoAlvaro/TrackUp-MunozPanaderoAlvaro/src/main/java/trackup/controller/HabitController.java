package trackup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trackup.dto.request.HabitRequestDTO;
import trackup.dto.response.HabitResponseDTO;
import trackup.services.HabitService;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar hábitos
 *
 * Acceso: <a href="http://localhost:8080/api/habits">...</a>
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/habits") // Prefijo para todas las rutas de este controlador
public class HabitController {

    private final HabitService habitService; // Servicio de hábitos

    /**
     * Constructor con inyección de dependencias
     *
     * @param habitService Servicio de hábitos
     */
    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    /**
     * Obtener todos los hábitos
     *
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/habits/habits">...</a>
     *
     * @return Lista de hábitos
     */
    @GetMapping("/habits")
    public ResponseEntity<List<HabitResponseDTO>> getAllHabits() {
        List<HabitResponseDTO> habitsList = habitService.getAllHabits(); // Obtiene todos los hábitos
        if (habitsList.isEmpty()) { // Si la lista está vacía, devuelve un código 204 No Content
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(habitsList); // Devuelve la lista de hábitos con un código 200 OK
    }

    /**
     * Obtener un hábito por su ID
     *
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/habits/habit/1">...</a>
     *
     * @param id ID del hábito
     * @return Hábito encontrado
     */
    @GetMapping("/habit/{id}")
    public ResponseEntity<HabitResponseDTO> getHabitById(@PathVariable Long id) {
        if (id < 0) { // Validación de ID negativo
            return ResponseEntity.badRequest().build();
        }

        Optional<HabitResponseDTO> habitOpt = habitService.findHabitById(id); // Busca el hábito por ID, y si lo encuentra lo transforma a un DTO
        return habitOpt.map(ResponseEntity::ok) // Si el hábito existe, devuelve un código 200 OK con el DTO
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no existe, devuelve un código 404 Not Found
    }

    /**
     * Obtener un hábito por su nombre
     *
     * FUNCIONA
     *
     * <a href="http://localhost:8080/api/habits/habit/by-name-and-user?name=Dejar%20de%20fumar&userId=1">...</a>
     *
     * @param name Nombre del hábito
     * @param userId ID del usuario
     * @return Hábito encontrado
     */
    @GetMapping("/habit/by-name-and-user")
    public ResponseEntity<HabitResponseDTO> getHabitByNameAndUserId(@RequestParam String name, @RequestParam Long userId) {
        return habitService.findHabitByNameAndUserId(name, userId) // Busca el hábito por nombre y ID de usuario
                .map(ResponseEntity::ok) // Si el hábito existe, devuelve un código 200 OK con el DTO
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no existe, devuelve un código 404 Not Found
    }

    /**
     * Obtener todos los hábitos de un usuario por su ID
     *
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/habits/habits/user/1">...</a>
     *
     * @param userId ID del usuario
     */
    @GetMapping("/habits/user/{userId}")
    public ResponseEntity<List<HabitResponseDTO>> getHabitsByUserId(@PathVariable Long userId) {
        if (userId < 0) { // Validación de ID negativo
            return ResponseEntity.badRequest().build(); // Devuelve error si el ID es negativo
        }

        List<HabitResponseDTO> habitsList = habitService.getAllHabitsByUserId(userId); // Obtiene todos los hábitos de un usuario por su ID
        if (habitsList.isEmpty()) { // Si la lista está vacía, devuelve un código 204 No Content
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(habitsList); // Devuelve la lista de hábitos con un código 200 OK
    }

    /**
     * Crear un nuevo hábito
     *
     * FUNCIONA
     *
     * POST <a href="http://localhost:8080/api/habits/habit">...</a>
     *
     * @param habitRequestDTO Objeto DTO con los datos del nuevo hábito
     * @return Hábito creado
     */
    @PostMapping("/habit")
    public ResponseEntity<HabitResponseDTO> createHabit(@RequestBody HabitRequestDTO habitRequestDTO) {
        if (habitRequestDTO == null) { // Validación de objeto nulo
            return ResponseEntity.badRequest().build(); // Devuelve error si el objeto es nulo
        }

        HabitResponseDTO createdHabit = habitService.createHabit(habitRequestDTO); // Crea un nuevo hábito
        return ResponseEntity.status(201).body(createdHabit); // Devuelve el nuevo hábito creado con un código 201 Created
    }

    /**
     * Actualizar un hábito existente
     *
     * FUNCIONA
     *
     * PUT <a href="http://localhost:8080/api/habits/habit/1">...</a>
     *
     * @param id ID del hábito a actualizar
     * @param habitRequestDTO Objeto DTO con los datos actualizados del hábito
     * @return Hábito actualizado
     */
    @PutMapping("/habit/{id}")
    public ResponseEntity<HabitResponseDTO> updateHabit(@PathVariable Long id, @RequestBody HabitRequestDTO habitRequestDTO) {
        if (id < 0 || habitRequestDTO == null) { // Validación de ID negativo o objeto nulo
            return ResponseEntity.badRequest().build(); // Devuelve error si el ID es negativo o el objeto es nulo
        }

        try {
            HabitResponseDTO updatedHabit = habitService.updateHabit(id, habitRequestDTO); // Actualiza el hábito existente
            return ResponseEntity.ok(updatedHabit); // Devuelve el hábito actualizado con un código 200 OK
        } catch (RuntimeException e) { // Captura de excepción si el hábito no se encuentra
            return ResponseEntity.notFound().build(); // Devuelve error si el hábito no se encuentra
        }
    }

    /**
     * Eliminar un hábito por su ID
     *
     * FUNCIONA
     *
     * DELETE <a href="http://localhost:8080/api/habits/habit/1">...</a>
     *
     * @param id ID del hábito a eliminar
     * @return Código de respuesta HTTP
     */
    @DeleteMapping("/habit/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        if (id < 0) { // Validación de ID negativo
            return ResponseEntity.badRequest().build(); // Devuelve error si el ID es negativo
        }

        try {
            habitService.deleteHabit(id); // Elimina el hábito por su ID
            return ResponseEntity.noContent().build(); // Devuelve un código 204 No Content si la eliminación es exitosa
        } catch (RuntimeException e) { // Captura de excepción si el hábito no se encuentra
            return ResponseEntity.notFound().build(); // Devuelve error si el hábito no se encuentra
        }
    }

}