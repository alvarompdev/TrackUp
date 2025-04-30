package trackup.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trackup.dto.response.GoalResponseDTO;
import trackup.entity.Goal;
import trackup.repository.GoalRepository;
import trackup.services.GoalService;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio 'GoalService'
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Service // Indica que esta clase es un servicio de Spring
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;

    /**
     * Constructor de la clase e inyección de dependencias
     *
     * @param goalRepository Repositorio de objetivos
     */
    @Autowired
    public GoalServiceImpl(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }


    @Override
    public Optional<GoalResponseDTO> getGoalById(Long id) {
        return goalRepository.findById(id) // Busca el objetivo por su ID
                .map(goal -> new GoalResponseDTO( // Si lo encuentra, lo transforma a un DTO
                        goal.getId(),
                        goal.getName(),
                        goal.getDescription()
                ));
    }

    @Override
    public Optional<GoalResponseDTO> getGoalByName(String name) {
        return Optional.ofNullable(goalRepository.findGoalByName(name)) // Busca el objetivo por su nombre
                .map(goal -> new GoalResponseDTO( // Si lo encuentra, lo transforma a un DTO
                        goal.getId(),
                        goal.getName(),
                        goal.getDescription()
                ));
    }

    @Override
    public List<GoalResponseDTO> getAllGoals() {
        return goalRepository.findAll() // Busca todos los objetivos
                .stream() // Convierte la lista a un stream
                .map(goal -> new GoalResponseDTO( // Transforma cada objetivo a un DTO
                        goal.getId(),
                        goal.getName(),
                        goal.getDescription()
                ))
                .toList(); // Convierte el stream de vuelta a una lista
    }

    @Override
    public GoalResponseDTO createGoal(GoalResponseDTO goalResponseDTO) {
        // Crea un nuevo objetivo a partir del DTO
        Goal goal = new Goal();
        goal.setName(goalResponseDTO.getName());
        goal.setDescription(goalResponseDTO.getDescription());

        // Guarda el objetivo en la base de datos
        Goal savedGoal = goalRepository.save(goal);

        // Devuelve el DTO del objetivo guardado
        return new GoalResponseDTO(savedGoal);
    }

    @Override
    public GoalResponseDTO updateGoal(Long id, GoalResponseDTO goalResponseDTO) {
        // Busca el objetivo por su ID
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found")); // Lanza una excepción si no lo encuentra

        // Actualiza los campos del objetivo
        goal.setName(goalResponseDTO.getName());
        goal.setDescription(goalResponseDTO.getDescription());

        // Guarda el objetivo actualizado en la base de datos
        Goal updatedGoal = goalRepository.save(goal);

        // Devuelve el DTO del objetivo actualizado
        return new GoalResponseDTO(updatedGoal);
    }

    @Override
    public void deleteGoal(Long id) {
        // Busca el objetivo por su ID
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found")); // Lanza una excepción si no lo encuentra

        // Elimina el objetivo de la base de datos
        goalRepository.delete(goal);
    }

}