package trackup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trackup.dto.request.GoalRequestDTO;
import trackup.dto.response.GoalResponseDTO;
import trackup.services.GoalService;

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
 * Controlador REST para gestionar los objetivos
 *
 * Acceso: <a href="http://localhost:8080/api/goals">...</a>
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@RestController
@RequestMapping("/api/goals")
@Tag(name = "Goals", description = "API para gestión de objetivos")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    /**
     * Obtiene todos los objetivos
     *
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/goals/goals">...</a>
     *
     * @return Lista de objetivos
     */
    @Operation(summary = "Obtener todos los objetivos", description = "Retorna una lista completa de objetivos registrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de objetivos encontrada",
                    content = @Content(schema = @Schema(implementation = GoalResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "No hay objetivos registrados",
                    content = @Content)
    })
    @GetMapping("/goals")
    public ResponseEntity<List<GoalResponseDTO>> getAllGoals() {
        List<GoalResponseDTO> goalsList = goalService.getAllGoals();
        if (goalsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(goalsList);
    }

    /**
     * Obtiene un objetivo por su ID
     *
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/goals/goal/id/1">...</a>
     *
     * @param id ID del objetivo
     * @return Objetivo encontrado
     */
    @Operation(summary = "Obtener objetivo por ID", description = "Busca un objetivo específico por su ID único")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Objetivo encontrado",
                    content = @Content(schema = @Schema(implementation = GoalResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Objetivo no encontrado",
                    content = @Content)
    })
    @GetMapping("/goal/{id}")
    public ResponseEntity<GoalResponseDTO> getGoalById(
            @Parameter(description = "ID del objetivo (debe ser positivo)", required = true,
                    schema = @Schema(minimum = "0"))
            @PathVariable Long id
    ) {
        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }
        Optional<GoalResponseDTO> goalOpt = goalService.findGoalById(id);
        return goalOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtiene un objetivo por su nombre o por su nombre e ID de usuario
     *
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/goals/goal/by-name?name=No%20fumar">...</a>
     * GET <a href="http://localhost:8080/api/goals/goal/by-name?name=No%20fumar&userId=1">...</a>
     *
     * @param name Nombre del objetivo
     * @param userId ID del usuario (opcional)
     * @return Objetivo encontrado
     */
    @Operation(summary = "Buscar objetivo por nombre",
            description = "Busca objetivos por nombre, opcionalmente filtrado por usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Objetivo encontrado",
                    content = @Content(schema = @Schema(implementation = GoalResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Nombre vacío o inválido",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Objetivo no encontrado",
                    content = @Content)
    })
    @GetMapping("/goal/by-name")
    public ResponseEntity<GoalResponseDTO> getGoalByName(
            @Parameter(description = "Nombre del objetivo (no vacío)", required = true)
            @RequestParam String name,
            @Parameter(description = "ID del usuario (opcional)", required = false)
            @RequestParam(required = false) Long userId
    ) {
        if (name.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<GoalResponseDTO> goalOpt = userId != null ?
                goalService.findGoalByNameAndUserId(name, userId) :
                goalService.findGoalByName(name);

        return goalOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo objetivo
     *
     * FUNCIONA
     *
     * POST <a href="http://localhost:8080/api/goals/goal">...</a>
     *
     * @param goalRequestDTO Datos del objetivo que se va a crear
     * @return Objetivo creado
     */
    @Operation(summary = "Crear nuevo objetivo",
            description = "Registra un nuevo objetivo en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Objetivo creado exitosamente",
                    content = @Content(schema = @Schema(implementation = GoalResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content)
    })
    @PostMapping("/goal")
    public ResponseEntity<GoalResponseDTO> createGoal(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo objetivo",
                    required = true,
                    content = @Content(schema = @Schema(implementation = GoalRequestDTO.class))
            )
            @RequestBody GoalRequestDTO goalRequestDTO
    ) {
        if (goalRequestDTO.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        GoalResponseDTO createdGoal = goalService.createGoal(goalRequestDTO);
        return ResponseEntity.ok(createdGoal);
    }

    /**
     * Actualiza un objetivo existente
     *
     * FUNCIONA
     *
     * PUT <a href="http://localhost:8080/api/goals/goal/id/1">...</a>
     *
     * @param id ID del objetivo que se va a actualizar
     * @param goalRequestDTO Datos del objetivo que se va a actualizar
     * @return Objetivo actualizado
     */
    @Operation(summary = "Actualizar objetivo",
            description = "Modifica los datos de un objetivo existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Objetivo actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = GoalResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido o datos incorrectos",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Objetivo no encontrado",
                    content = @Content)
    })
    @PutMapping("/goal/{id}")
    public ResponseEntity<GoalResponseDTO> updateGoal(
            @Parameter(description = "ID del objetivo a actualizar", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del objetivo",
                    required = true,
                    content = @Content(schema = @Schema(implementation = GoalRequestDTO.class))
            )
            @RequestBody GoalRequestDTO goalRequestDTO
    ) {
        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }

        GoalResponseDTO updatedGoal = goalService.updateGoal(id, goalRequestDTO);
        return ResponseEntity.ok(updatedGoal);
    }

    /**
     * Elimina un objetivo existente
     *
     * FUNCIONA
     *
     * DELETE <a href="http://localhost:8080/api/goals/goal/id/1">...</a>
     *
     * @param id ID del objetivo que se va a eliminar
     */
    @Operation(summary = "Eliminar objetivo",
            description = "Elimina permanentemente un objetivo del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Objetivo eliminado exitosamente",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "ID inválido",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Objetivo no encontrado",
                    content = @Content)
    })
    @DeleteMapping("/goal/{id}")
    public ResponseEntity<Void> deleteGoal(
            @Parameter(description = "ID del objetivo a eliminar", required = true)
            @PathVariable Long id
    ) {
        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }

        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }

}