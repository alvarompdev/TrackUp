package trackup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trackup.dto.request.HabitTypeRequestDTO;
import trackup.dto.response.HabitTypeResponseDTO;
import trackup.services.HabitTypeService;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar tipos de hábito
 *
 * Acceso: <a href="http://localhost:8080/api/habit_types">...</a>
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/habit_types") // Prefijo para todas las rutas de este controlador
public class HabitTypeController {

    private final HabitTypeService habitTypeService; // Servicio de tipos de hábito

    /**
     * Constructor con inyección de dependencias
     *
     * @param habitTypeService Servicio de tipos de hábito
     */
    public HabitTypeController(HabitTypeService habitTypeService) {
        this.habitTypeService = habitTypeService;
    }

    /**
     * Obtener todos los tipos de hábito
     *
     *
     *
     * GET <a href="http://localhost:8080/api/habit_types/habit_types">...</a>
     */
    @GetMapping("habit_types")
    public ResponseEntity<List<HabitTypeResponseDTO>> getAllHabitTypes() {
        List<HabitTypeResponseDTO> habitTypesList = habitTypeService.getAllHabitTypes(); // Obtiene todos los tipos de hábito
        if (habitTypesList.isEmpty()) { // Si la lista está vacía, devuelve un código 204 No Content
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(habitTypesList); // Devuelve la lista de tipos de hábito con un código 200 OK
    }

    /**
     * Obtener un tipo de hábito por su ID
     *
     *
     *
     * GET <a href="http://localhost:8080/api/habit_types/habit_type/id/1">...</a>{id}
     */
    @GetMapping("habit_type/id/{id}")
    public ResponseEntity<HabitTypeResponseDTO> findHabitTypeById(@PathVariable Long id) {
        if (id < 0) { // Verifica que el ID sea válido
            return ResponseEntity.badRequest().build();
        }

        Optional<HabitTypeResponseDTO> habitTypeOpt = habitTypeService.getHabitTypeById(id); // Busca el tipo de hábito por ID
        return habitTypeOpt.map(ResponseEntity::ok) // Si lo encuentra, lo devuelve con un código 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no lo encuentra, devuelve un código 404 Not Found
    }

    /**
     * Obtener un tipo de hábito por su nombre
     *
     *
     *
     * GET <a href="http://localhost:8080/api/habit_types/habit_type/name/ejemplo">...</a>{name}
     */
    @GetMapping("Habit_type/name/{name}")
    public ResponseEntity<HabitTypeResponseDTO> findHabitTypeByName(@PathVariable String name) {
        if (name == null || name.trim().isEmpty()) { // Verifica que el nombre no sea nulo o vacío
            return ResponseEntity.badRequest().build(); // Devuelve error si el nombre es nulo o vacío
        }

        Optional<HabitTypeResponseDTO> habitTypeOpt = habitTypeService.getHabitTypeByName(name); // Busca el tipo de hábito por nombre
        return habitTypeOpt.map(ResponseEntity::ok) // Si lo encuentra, lo devuelve con un código 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no lo encuentra, devuelve un código 404 Not Found
    }

    /**
     * Crear un nuevo tipo de hábito
     *
     *
     *
     * POST <a href="http://localhost:8080/api/habit_types/habit_type">...</a>
     */
    @PostMapping("/habit_type")
    public ResponseEntity<HabitTypeResponseDTO> createHabitType(@RequestBody HabitTypeRequestDTO habitTypeRequest) {
        if (habitTypeRequest == null) { // Verifica que la solicitud no sea nula
            return ResponseEntity.badRequest().build(); // Devuelve error si la solicitud es nula
        }

        HabitTypeResponseDTO createdHabitType = habitTypeService.createHabitType(habitTypeRequest); // Crea un nuevo tipo de hábito
        return ResponseEntity.status(201).body(createdHabitType); // Devuelve el tipo de hábito creado con un código 201 Created
    }

    /**
     * Actualizar un tipo de hábito existente
     *
     *
     *
     * PUT <a href="http://localhost:8080/api/habit_types/habit_type/id/1">...</a>{id}
     */
    @PutMapping("/habit_type/{id}")
    public ResponseEntity<HabitTypeResponseDTO> updateHabitType(@PathVariable Long id, @RequestBody HabitTypeRequestDTO habitTypeRequest) {
        if (id < 0 || habitTypeRequest == null) { // Verifica que el ID sea válido y la solicitud no sea nula
            return ResponseEntity.badRequest().build(); // Devuelve error si el ID es negativo o la solicitud es nula
        }

        try {
            HabitTypeResponseDTO updatedHabitType = habitTypeService.updateHabitType(id, habitTypeRequest); // Actualiza el tipo de hábito
            return ResponseEntity.ok(updatedHabitType); // Devuelve el tipo de hábito actualizado con un código 200 OK
        } catch (RuntimeException e) { // Maneja la excepción si el tipo de hábito no se encuentra
            return ResponseEntity.notFound().build(); // Devuelve un código 404 Not Found si no se encuentra el tipo de hábito
        }
    }

    /**
     * Eliminar un tipo de hábito por su ID
     *
     *
     *
     * DELETE <a href="http://localhost:8080/api/habit_types/habit_type/id/1">...</a>{id}
     */
    @DeleteMapping("/habit_type/{id}")
    public ResponseEntity<Void> deleteHabitType(@PathVariable Long id) {
        if (id < 0) { // Verifica que el ID sea válido
            return ResponseEntity.badRequest().build(); // Devuelve error si el ID es negativo
        }

        try {
            habitTypeService.deleteHabitType(id); // Elimina el tipo de hábito por ID
            return ResponseEntity.noContent().build(); // Devuelve un código 204 No Content si la eliminación es exitosa
        } catch (RuntimeException e) { // Maneja la excepción si el tipo de hábito no se encuentra
            return ResponseEntity.notFound().build(); // Devuelve un código 404 Not Found si no se encuentra el tipo de hábito
        }
    }

}