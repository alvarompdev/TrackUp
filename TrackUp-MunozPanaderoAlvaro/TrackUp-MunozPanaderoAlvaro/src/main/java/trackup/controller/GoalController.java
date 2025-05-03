package trackup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trackup.dto.request.GoalRequestDTO;
import trackup.dto.response.GoalResponseDTO;
import trackup.services.GoalService;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar los objetivos
 *
 * Acceso: <a href="http://localhost:8080/api/goals">...</a>
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/goals") // Prefijo para todas las rutas de este controlador
public class GoalController {

    private final GoalService goalService; // Servicio de objetivos

    /**
     * Constructor con inyección de dependencias
     *
     * @param goalService Servicio de objetivos
     */
    public GoalController(GoalService goalService) {
        this.goalService = goalService; // Inyección de dependencias del servicio
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
    @GetMapping("/goals")
    public ResponseEntity<List<GoalResponseDTO>> getAllGoals() {
        List<GoalResponseDTO> goalsList = goalService.getAllGoals(); // Obtiene todos los objetivos
        if (goalsList.isEmpty()) { // Si la lista está vacía, devuelve un código 204 No Content
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(goalsList); // Devuelve la lista de objetivos con un código 200 OK
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
    @GetMapping("/goal/{id}")
    public ResponseEntity<GoalResponseDTO> getGoalById(@PathVariable Long id) {
        if (id < 0) { // Verifica si el ID es negativo
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo
        }

        Optional<GoalResponseDTO> goalOpt = goalService.findGoalById(id); // Obtiene el objetivo de acuerdo al ID proporcionado
        return goalOpt.map(ResponseEntity::ok) // Si el objetivo existe, devuelve el objeto GoalResponseDTO
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
    @GetMapping("/goal/by-name")
    public ResponseEntity<GoalResponseDTO> getGoalByName(@RequestParam String name, @RequestParam(required = false) Long userId) {
        if (name.trim().isEmpty()) { // Verifica si el nombre está vacío
            return ResponseEntity.badRequest().build(); // Devuelve error si el nombre está vacío
        }

        Optional<GoalResponseDTO> goalOpt; // Variable para almacenar el objetivo encontrado
        if (userId != null) { // Si se proporciona un ID de usuario, busca el objetivo por nombre y ID de usuario
            goalOpt = goalService.findGoalByNameAndUserId(name, userId); // Busca el objetivo por nombre y ID de usuario
        } else {
            goalOpt = goalService.findGoalByName(name); // Busca el objetivo solo por nombre
        }

        return goalOpt.map(ResponseEntity::ok) // Si el objetivo existe, devuelve el objeto GoalResponseDTO
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no existe, devuelve un código 404 Not Found
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
    @PostMapping("/goal")
    public ResponseEntity<GoalResponseDTO> createGoal(@RequestBody GoalRequestDTO goalRequestDTO) {
        if (goalRequestDTO.getName().isEmpty()) { // Verifica si el nombre está vacío
            return ResponseEntity.badRequest().build();  // Devuelve error si el nombre está vacío
        }

        GoalResponseDTO createdGoal = goalService.createGoal(goalRequestDTO); // Crea un nuevo objetivo
        return ResponseEntity.ok(createdGoal); // Devuelve el objetivo creado con un código 200 OK
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
    @PutMapping("/goal/{id}")
    public ResponseEntity<GoalResponseDTO> updateGoal(@PathVariable Long id, @RequestBody GoalRequestDTO goalRequestDTO) {
        if (id < 0) { // Verifica si el ID es negativo
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo
        }

        GoalResponseDTO updatedGoal = goalService.updateGoal(id, goalRequestDTO); // Actualiza el objetivo existente
        return ResponseEntity.ok(updatedGoal); // Devuelve el objetivo actualizado con un código 200 OK
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
    @DeleteMapping("/goal/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        if (id < 0) { // Verifica si el ID es negativo
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo
        }

        goalService.deleteGoal(id); // Elimina el objetivo existente
        return ResponseEntity.noContent().build(); // Devuelve un código 204 No Content
    }

}