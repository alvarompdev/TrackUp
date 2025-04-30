package trackup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trackup.dto.response.GoalResponseDTO;
import trackup.services.GoalService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalService goalService;

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
     * @return Lista de objetivos
     */
    @GetMapping("/goals")
    public ResponseEntity<List<GoalResponseDTO>> findAllGoals() {
        List<GoalResponseDTO> goalsList = goalService.getAllGoals(); // Obtiene todos los objetivos
        if (goalsList.isEmpty()) { // Si la lista está vacía, devuelve un código 204 No Content
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(goalsList); // Devuelve la lista de objetivos con un código 200 OK
    }

    /**
     * Obtiene un objetivo por su ID
     *
     * @param id ID del objetivo
     * @return Objetivo encontrado
     */
    @GetMapping("/goal/{id}")
    public ResponseEntity<GoalResponseDTO> findGoalById(@PathVariable Long id) {
        if (id < 0) { // Verifica si el ID es negativo
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo
        }

        Optional<GoalResponseDTO> goalOpt = goalService.getGoalById(id); // Obtiene el objetivo de acuerdo al ID proporcionado
        return goalOpt.map(ResponseEntity::ok) // Si el objetivo existe, devuelve el objeto GoalResponseDTO
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtiene un objetivo por su nombre
     *
     * @param name Nombre del objetivo
     * @return Objetivo encontrado
     */
    @GetMapping("/goal/name/{name}")
    public ResponseEntity<GoalResponseDTO> findGoalByName(@PathVariable String name) {
        if (name.isEmpty()) { // Verifica si el nombre está vacío
            return ResponseEntity.badRequest().build();  // Devuelve error si el nombre está vacío
        }

        Optional<GoalResponseDTO> goalOpt = goalService.getGoalByName(name); // Obtiene el objetivo de acuerdo al nombre proporcionado
        return goalOpt.map(ResponseEntity::ok) // Si el objetivo existe, devuelve el objeto GoalResponseDTO
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo objetivo
     *
     * @param goalResponseDTO Datos del objetivo que se va a crear
     * @return Objetivo creado
     */
    @PostMapping("/goal")
    public ResponseEntity<GoalResponseDTO> createGoal(@RequestBody GoalResponseDTO goalResponseDTO) {
        if (goalResponseDTO.getName().isEmpty()) { // Verifica si el nombre está vacío
            return ResponseEntity.badRequest().build();  // Devuelve error si el nombre está vacío
        }

        GoalResponseDTO createdGoal = goalService.createGoal(goalResponseDTO); // Crea un nuevo objetivo
        return ResponseEntity.ok(createdGoal); // Devuelve el objetivo creado con un código 200 OK
    }

    /**
     * Actualiza un objetivo existente
     *
     * @param id              ID del objetivo que se va a actualizar
     * @param goalResponseDTO Datos del objetivo que se va a actualizar
     * @return Objetivo actualizado
     */
    @PutMapping("/goal/{id}")
    public ResponseEntity<GoalResponseDTO> updateGoal(@PathVariable Long id, @RequestBody GoalResponseDTO goalResponseDTO) {
        if (id < 0) { // Verifica si el ID es negativo
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo
        }

        GoalResponseDTO updatedGoal = goalService.updateGoal(id, goalResponseDTO); // Actualiza el objetivo existente
        return ResponseEntity.ok(updatedGoal); // Devuelve el objetivo actualizado con un código 200 OK
    }

    /**
     * Elimina un objetivo existente
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