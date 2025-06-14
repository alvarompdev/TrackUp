package trackup.services;

import trackup.dto.request.GoalRequestDTO;
import trackup.dto.response.GoalResponseDTO;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que define todas las operaciones relacionadas con los objetivos
 * Proporciona métodos para obtener, crear, actualizar y eliminar objetivos
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public interface GoalService {

    /**
     * Encuentra un objetivo por su ID
     *
     * @param id ID del objetivo
     * @return Objetivo encontrado
     */
    Optional<GoalResponseDTO> findGoalById(Long id);

    /**
     * Encuentra un objetivo por su nombre
     *
     * @param name Nombre del objetivo
     * @return Objetivo encontrado
     */
    Optional<GoalResponseDTO> findGoalByName(String name);

    /**
     * Encuentra un objetivo por su nombre y el ID del usuario
     *
     * @param name   Nombre del objetivo
     * @param userId ID del usuario
     * @return Objetivo encontrado
     */
    Optional<GoalResponseDTO> findGoalByNameAndUserId(String name, Long userId);

    /**
     * Obtiene todos los objetivos del usuario
     *
     * @return Lista de objetivos
     */
    List<GoalResponseDTO> getAllGoals();

    /**
     * Obtiene todos los objetivos del usuario por su ID
     *
     * @param userId ID del usuario
     * @return Lista de objetivos
     */
    List<GoalResponseDTO> getAllGoalsByUserId(Long userId);

    /**
     * Crea un nuevo objetivo
     *
     * @param goalRequestDTO Datos del objetivo que se va a crear
     * @return Objetivo creado
     */
    GoalResponseDTO createGoal(GoalRequestDTO goalRequestDTO);

    /**
     * Actualiza un objetivo existente
     *
     * @param id              ID del objetivo que se va a actualizar
     * @param goalRequestDTO Datos del objetivo que se va a actualizar
     * @return Objetivo actualizado
     */
    GoalResponseDTO updateGoal(Long id, GoalRequestDTO goalRequestDTO);

    /**
     * Elimina un objetivo existente
     *
     * @param id ID del objetivo que se va a eliminar
     */
    void deleteGoal(Long id);

}