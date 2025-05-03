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
 * Acceso: <a href="http://localhost:8080/api/habit-types">...</a>
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/habit-types") // Prefijo para todas las rutas de este controlador
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
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/habit_types/habit_types">...</a>
     *
     * @return Lista de tipos de hábito
     */
    @GetMapping("/habit-types")
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
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/habit_types/habit_type/id/1">...</a>
     *
     * @param id ID del tipo de hábito
     * @return Tipo de hábito encontrado
     */
    @GetMapping("/habit-type/{id}")
    public ResponseEntity<HabitTypeResponseDTO> getHabitTypeById(@PathVariable Long id) {
        if (id < 0) { // Verifica que el ID sea válido
            return ResponseEntity.badRequest().build();
        }

        Optional<HabitTypeResponseDTO> habitTypeOpt = habitTypeService.findHabitTypeById(id); // Busca el tipo de hábito por ID
        return habitTypeOpt.map(ResponseEntity::ok) // Si lo encuentra, lo devuelve con un código 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no lo encuentra, devuelve un código 404 Not Found
    }

    /**
     * Obtener un tipo de hábito por su nombre
     *
     * FUNCIONA (ES CASE SENSITIVE, SEGURAMENTE HABRIA QUE CORREGIR ESO)
     *
     * GET <a href="http://localhost:8080/api/habit_types/habit_type/name/ejemplo">...</a>
     *
     * @param name Nombre del tipo de hábito
     * @return Tipo de hábito encontrado
     */
    @GetMapping("/habit-type/name/{name}")
    public ResponseEntity<HabitTypeResponseDTO> getHabitTypeByName(@PathVariable String name) {
        if (name == null || name.trim().isEmpty()) { // Verifica que el nombre no sea nulo o vacío
            return ResponseEntity.badRequest().build(); // Devuelve error si el nombre es nulo o vacío
        }

        Optional<HabitTypeResponseDTO> habitTypeOpt = habitTypeService.findHabitTypeByName(name); // Busca el tipo de hábito por nombre
        return habitTypeOpt.map(ResponseEntity::ok) // Si lo encuentra, lo devuelve con un código 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no lo encuentra, devuelve un código 404 Not Found
    }

    /**
     * Crear un nuevo tipo de hábito
     *
     * FUNCIONA
     *
     * POST <a href="http://localhost:8080/api/habit_types/habit_type">...</a>
     *
     * @param habitTypeRequest Datos del nuevo tipo de hábito
     * @return Tipo de hábito creado
     */
    @PostMapping("/habit-type")
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
     * FUNCIONA
     *
     * PUT <a href="http://localhost:8080/api/habit_types/habit_type/id/1">...</a>
     *
     * @param id ID del tipo de hábito que se va a actualizar
     * @param habitTypeRequest Datos del tipo de hábito que se va a actualizar
     * @return Tipo de hábito actualizado
     */
    @PutMapping("/habit-type/{id}")
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
     * FUNCIONA
     *
     * DELETE <a href="http://localhost:8080/api/habit_types/habit_type/id/1">...</a>
     *
     * @param id ID del tipo de hábito que se va a eliminar
     * @return Código de respuesta HTTP
     */
    @DeleteMapping("/habit-type/{id}")
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