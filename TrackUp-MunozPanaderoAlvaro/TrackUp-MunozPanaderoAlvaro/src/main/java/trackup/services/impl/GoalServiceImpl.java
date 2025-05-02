package trackup.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trackup.dto.request.GoalRequestDTO;
import trackup.dto.response.GoalResponseDTO;
import trackup.entity.Goal;
import trackup.entity.User;
import trackup.repository.GoalRepository;
import trackup.repository.UserRepository;
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
    private final UserRepository userRepository; // Repositorio de usuarios

    /**
     * Constructor de la clase e inyección de dependencias
     *
     * @param goalRepository Repositorio de objetivos
     */
    @Autowired
    public GoalServiceImpl(GoalRepository goalRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository; // Inicializa el repositorio de usuarios
    }


    @Override
    public Optional<GoalResponseDTO> getGoalById(Long id) {
        return goalRepository.findById(id) // Busca el objetivo por su ID
                .map(goal -> new GoalResponseDTO( // Si lo encuentra, lo transforma a un DTO
                        goal.getId(),
                        goal.getName(),
                        goal.getDescription(),
                        goal.getUser() != null ? goal.getUser().getId() : null // Obtiene el ID del usuario asociado
                ));
    }

    @Override
    public Optional<GoalResponseDTO> getGoalByName(String name) {
        return Optional.ofNullable(goalRepository.findGoalByName(name)) // Busca el objetivo por su nombre
                .map(goal -> new GoalResponseDTO( // Si lo encuentra, lo transforma a un DTO
                        goal.getId(),
                        goal.getName(),
                        goal.getDescription(),
                        goal.getUser() != null ? goal.getUser().getId() : null // Obtiene el ID del usuario asociado
                ));
    }

    @Override
    public List<GoalResponseDTO> getAllGoals() {
        return goalRepository.findAll() // Busca todos los objetivos
                .stream() // Convierte la lista a un stream
                .map(goal -> new GoalResponseDTO( // Transforma cada objetivo a un DTO
                        goal.getId(),
                        goal.getName(),
                        goal.getDescription(),
                        goal.getUser() != null ? goal.getUser().getId() : null // Obtiene el ID del usuario asociado
                ))
                .toList(); // Convierte el stream de vuelta a una lista
    }

    @Override
    public Optional<GoalResponseDTO> getGoalByNameAndUserId(String name, Long userId) {
        return goalRepository
                .findGoalByNameAndUserId(name, userId)
                .map(GoalResponseDTO::new);
    }

    @Override
    public GoalResponseDTO createGoal(GoalRequestDTO goalRequestDTO) {
        // Buscar la entidad User por ID
        User user = userRepository.findById(goalRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + goalRequestDTO.getUserId()));

        // Crear nuevo objetivo
        Goal goal = new Goal();
        goal.setName(goalRequestDTO.getName());
        goal.setDescription(goalRequestDTO.getDescription());
        goal.setUser(user);  // Asignar entidad User

        // Guardar y devolver DTO
        Goal savedGoal = goalRepository.save(goal);
        return new GoalResponseDTO(savedGoal);
    }

    @Override
    public GoalResponseDTO updateGoal(Long id, GoalRequestDTO goalRequestDTO) {
        // Busca el objetivo por su ID
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found")); // Lanza una excepción si no lo encuentra

        // Actualiza los campos del objetivo
        goal.setName(goalRequestDTO.getName());
        goal.setDescription(goalRequestDTO.getDescription());

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