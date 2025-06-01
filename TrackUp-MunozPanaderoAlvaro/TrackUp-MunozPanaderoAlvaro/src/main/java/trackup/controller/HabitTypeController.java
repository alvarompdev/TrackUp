package trackup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trackup.dto.request.HabitTypeRequestDTO;
import trackup.dto.response.HabitTypeResponseDTO;
import trackup.services.HabitTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Habit Types", description = "API para gestión de tipos de hábito") // Anotación Swagger
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
    @Operation(summary = "Obtener todos los tipos de hábito", description = "Retorna una lista completa de tipos de hábito")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de tipos encontrada", content = @Content(schema = @Schema(implementation = HabitTypeResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "No hay tipos registrados", content = @Content)
    })
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
    @Operation(summary = "Obtener tipo por ID", description = "Busca un tipo de hábito por su ID único")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo encontrado", content = @Content(schema = @Schema(implementation = HabitTypeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado", content = @Content)
    })
    @GetMapping("/habit-type/{id}")
    public ResponseEntity<HabitTypeResponseDTO> getHabitTypeById(
            @Parameter(description = "ID del tipo (debe ser positivo)", required = true, schema = @Schema(minimum = "0"))
            @PathVariable Long id
    ) {
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
    @Operation(summary = "Buscar tipo por nombre", description = "Busca un tipo de hábito por su nombre (sensible a mayúsculas)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo encontrado", content = @Content(schema = @Schema(implementation = HabitTypeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Nombre inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado", content = @Content)
    })
    @GetMapping("/habit-type/name/{name}")
    public ResponseEntity<HabitTypeResponseDTO> getHabitTypeByName(
            @Parameter(description = "Nombre del tipo de hábito", required = true)
            @PathVariable String name
    ) {
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
    @Operation(summary = "Crear nuevo tipo", description = "Registra un nuevo tipo de hábito en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo creado exitosamente", content = @Content(schema = @Schema(implementation = HabitTypeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping("/habit-type")
    public ResponseEntity<HabitTypeResponseDTO> createHabitType(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo tipo de hábito",
                    required = true,
                    content = @Content(schema = @Schema(implementation = HabitTypeRequestDTO.class))
            )
            @RequestBody HabitTypeRequestDTO habitTypeRequest
    ) {
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
    @Operation(summary = "Actualizar tipo existente", description = "Modifica los datos de un tipo de hábito")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo actualizado exitosamente", content = @Content(schema = @Schema(implementation = HabitTypeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado", content = @Content)
    })
    @PutMapping("/habit-type/{id}")
    public ResponseEntity<HabitTypeResponseDTO> updateHabitType(
            @Parameter(description = "ID del tipo a actualizar", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del tipo",
                    required = true,
                    content = @Content(schema = @Schema(implementation = HabitTypeRequestDTO.class))
            )
            @RequestBody HabitTypeRequestDTO habitTypeRequest
    ) {
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
    @Operation(summary = "Eliminar tipo", description = "Elimina un tipo de hábito del sistema por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tipo eliminado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado", content = @Content)
    })
    @DeleteMapping("/habit-type/{id}")
    public ResponseEntity<Void> deleteHabitType(
            @Parameter(description = "ID del tipo a eliminar", required = true) @PathVariable Long id
    ) {
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