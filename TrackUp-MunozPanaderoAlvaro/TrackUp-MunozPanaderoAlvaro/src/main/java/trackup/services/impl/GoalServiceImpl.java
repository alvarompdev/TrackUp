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
 * Implementación del servicio de objetivos
 * Se encarga de la lógica de negocio relacionada con los objetivos,
 * incluyendo la creación, actualización, eliminación y obtención de objetivos
 *
 * Utiliza un repositorio para acceder a la base de datos y
 * convierte las entidades a DTOs para mantener la separación de capas
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Service // Anotación que indica que esta clase es un servicio
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository; // Repositorio de objetivos
    private final UserRepository userRepository; // Repositorio de usuarios

    /**
     * Constructor de la clase e inyección de dependencias
     *
     * @param goalRepository Repositorio de objetivos
     */
    @Autowired
    public GoalServiceImpl(GoalRepository goalRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Optional<GoalResponseDTO> findGoalById(Long id) {
        return goalRepository.findById(id) // Busca el objetivo por su ID
                .map(this::mapToDTO);
    }

    @Override
    public Optional<GoalResponseDTO> findGoalByName(String name) {
        return goalRepository.findByName(name)
                .map(this::mapToDTO);
    }

    @Override
    public Optional<GoalResponseDTO> findGoalByNameAndUserId(String name, Long userId) {
        return goalRepository
                .findGoalByNameAndUserId(name, userId)
                .map(this::mapToDTO);
    }

    @Override
    public List<GoalResponseDTO> getAllGoals() {
        return goalRepository.findAll() // Busca todos los objetivos
                .stream() // Convierte la lista a un stream
                .map(this::mapToDTO)
                .toList(); // Convierte el stream de vuelta a una lista
    }

    @Override
    public GoalResponseDTO createGoal(GoalRequestDTO goalRequestDTO) {
        User user = userRepository.findById(goalRequestDTO.getUserId()) // Busca la entidad User por ID
                .orElseThrow(() -> new RuntimeException("User not found with id: " + goalRequestDTO.getUserId()));

        // Crea nuevo objetivo
        Goal goal = new Goal();
        goal.setName(goalRequestDTO.getName());
        goal.setDescription(goalRequestDTO.getDescription());
        goal.setUser(user);  // Asigna entidad User

        Goal savedGoal = goalRepository.save(goal); // Guarda el objetivo
        return mapToDTO(savedGoal); // Devuelve el DTO del objetivo guardado
    }

    @Override
    public GoalResponseDTO updateGoal(Long id, GoalRequestDTO goalRequestDTO) {
        Goal goal = goalRepository.findById(id) // Busca el objetivo por su ID
                .orElseThrow(() -> new RuntimeException("Goal not found")); // Lanza una excepción si no lo encuentra

        // Actualiza los campos del objetivo
        goal.setName(goalRequestDTO.getName());
        goal.setDescription(goalRequestDTO.getDescription());

        Goal updatedGoal = goalRepository.save(goal); // Guarda el objetivo actualizado en la base de datos
        return mapToDTO(updatedGoal); // Devuelve el DTO del objetivo actualizado
    }

    @Override
    public void deleteGoal(Long id) {
        Goal goal = goalRepository.findById(id) // Busca el objetivo por su ID
                .orElseThrow(() -> new RuntimeException("Goal not found")); // Lanza una excepción si no lo encuentra

        goalRepository.delete(goal); // Elimina el objetivo de la base de datos
    }

    /**
     * Convierte una entidad Goal a un DTO GoalResponseDTO
     *
     * @param goal Entidad Goal a convertir
     * @return DTO GoalResponseDTO
     */
    private GoalResponseDTO mapToDTO(Goal goal) {
        return new GoalResponseDTO(
                goal.getId(),
                goal.getName(),
                goal.getDescription(),
                goal.getUser() != null ? goal.getUser().getId() : null
        );
    }

}