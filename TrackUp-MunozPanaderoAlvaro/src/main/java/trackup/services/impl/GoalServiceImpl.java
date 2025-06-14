package trackup.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trackup.dto.request.GoalRequestDTO;
import trackup.dto.response.GoalResponseDTO;
import trackup.entity.Goal;
import trackup.entity.User;
import trackup.repository.GoalRepository; // Ya tienes esta importación
import trackup.repository.UserRepository; // Ya tienes esta importación
import trackup.services.GoalService; // Ya tienes esta importación

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Ya tienes esta importación

@Service
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Autowired
    public GoalServiceImpl(GoalRepository goalRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<GoalResponseDTO> findGoalById(Long id) {
        return goalRepository.findById(id)
                .map(this::mapToDTO);
    }

    @Override
    public Optional<GoalResponseDTO> findGoalByName(String name) {
        // Usamos el método tal como está en tu repositorio
        return goalRepository.findByName(name)
                .map(this::mapToDTO);
    }

    @Override
    public Optional<GoalResponseDTO> findGoalByNameAndUserId(String name, Long userId) {
        // ¡¡¡AHORA COINCIDE CON EL NOMBRE EN TU REPOSITORIO!!!
        return goalRepository
                .findGoalByNameAndUserId(name, userId)
                .map(this::mapToDTO);
    }

    @Override
    public List<GoalResponseDTO> getAllGoals() {
        return goalRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<GoalResponseDTO> getAllGoalsByUserId(Long userId) {
        // ¡¡¡AHORA COINCIDE CON EL NOMBRE EN TU REPOSITORIO!!!
        List<Goal> goals = goalRepository.findAllGoalsByUserId(userId);

        return goals.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GoalResponseDTO createGoal(GoalRequestDTO goalRequestDTO) {
        // === VALIDACIÓN DE UNICIDAD ANTES DE GUARDAR ===
        // Usamos el método findGoalByNameAndUserId tal como lo tienes en el repositorio
        Optional<Goal> existingGoal = goalRepository.findGoalByNameAndUserId(goalRequestDTO.getName(), goalRequestDTO.getUserId());
        if (existingGoal.isPresent()) {
            throw new IllegalArgumentException("Ya existe una meta con el nombre '" + goalRequestDTO.getName() + "' para tu usuario.");
        }
        // ===============================================

        User user = userRepository.findById(goalRequestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + goalRequestDTO.getUserId()));

        Goal goal = new Goal();
        goal.setName(goalRequestDTO.getName());
        goal.setDescription(goalRequestDTO.getDescription());
        goal.setUser(user);

        Goal savedGoal = goalRepository.save(goal);
        return mapToDTO(savedGoal);
    }

    @Override
    public GoalResponseDTO updateGoal(Long id, GoalRequestDTO goalRequestDTO) {
        Goal goalToUpdate = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meta no encontrada con ID: " + id));

        // === VALIDACIÓN DE UNICIDAD PARA ACTUALIZACIÓN ===
        // Usamos el método findGoalByNameAndUserId tal como lo tienes en el repositorio
        Optional<Goal> duplicatedGoal = goalRepository.findGoalByNameAndUserId(goalRequestDTO.getName(), goalRequestDTO.getUserId());
        if (duplicatedGoal.isPresent() && !duplicatedGoal.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe otra meta con el nombre '" + goalRequestDTO.getName() + "' para tu usuario.");
        }
        // ===============================================

        goalToUpdate.setName(goalRequestDTO.getName());
        goalToUpdate.setDescription(goalRequestDTO.getDescription());

        // Asegúrate de que el usuario no cambie inesperadamente si no está previsto en la actualización
        // Si el userId puede cambiar en la actualización, deberías buscar y asignar el nuevo User aquí:
        // User user = userRepository.findById(goalRequestDTO.getUserId())
        //         .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + goalRequestDTO.getUserId()));
        // goalToUpdate.setUser(user);

        Goal updatedGoal = goalRepository.save(goalToUpdate);
        return mapToDTO(updatedGoal);
    }

    @Override
    public void deleteGoal(Long id) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meta no encontrada para eliminar con ID: " + id));

        goalRepository.delete(goal);
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