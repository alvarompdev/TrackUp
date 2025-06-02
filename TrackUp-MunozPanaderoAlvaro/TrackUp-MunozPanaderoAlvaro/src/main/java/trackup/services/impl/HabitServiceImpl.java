package trackup.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trackup.dto.request.HabitRequestDTO;
import trackup.dto.response.HabitResponseDTO;
import trackup.entity.Habit;
import trackup.entity.HabitType;
import trackup.entity.User;
import trackup.repository.HabitRepository;
import trackup.services.HabitService;
import trackup.services.HabitTypeService;
import trackup.services.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de hábitos
 * Se encarga de la lógica de negocio relacionada con los hábitos,
 * incluyendo la creación, actualización, eliminación y obtención de hábitoss
 *
 * Utiliza un repositorio para acceder a la base de datos y
 * convierte las entidades a DTOs para mantener la separación de capas
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Service // Anotación que indica que esta clase es un servicio
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository; // Repositorio para acceder a la base de datos
    private final UserService userService; // Servicio para acceder a la información del usuario
    private final HabitTypeService habitTypeService; // Servicio para acceder a la información del tipo de hábito

    /**
     * Constructor con inyección de dependencias
     *
     * @param habitRepository Repositorio de hábitos
     * @param userService Servicio de usuario
     * @param habitTypeService Servicio de tipo de hábito
     */
    @Autowired
    public HabitServiceImpl(HabitRepository habitRepository, UserService userService, HabitTypeService habitTypeService) {
        this.habitRepository = habitRepository;
        this.userService = userService;
        this.habitTypeService = habitTypeService;
    }

    @Override
    public Optional<HabitResponseDTO> findHabitById(Long id) {
        return habitRepository.findById(id) // Buscar el hábito por ID, y si lo encuentra lo transforma a un DTO
                .map(this::mapToDTO);
    }

    @Override
    public Optional<Habit> findHabitEntityById(Long id) {
        return habitRepository.findById(id); // Buscar el hábito por ID
    }

    @Override
    public Optional<Habit> findHabitByName(String name) {
        return habitRepository.findByName(name); // Buscar el hábito por nombre
    }

    @Override
    public Optional<HabitResponseDTO> findHabitByNameAndUserId(String name, Long userId) {
        return habitRepository
                .findHabitByNameAndUserId(name, userId)      // Optional<Habit>
                .map(HabitResponseDTO::new);            // lo convierte en Optional<HabitResponseDTO>
    }

    @Override
    public List<HabitResponseDTO> getAllHabits() {
        List<Habit> habits = habitRepository.findAll();
        if (habits == null || habits.isEmpty()) {
            // En lugar de lanzar excepción, devolvemos lista vacía
            return Collections.emptyList();
        }
        return habits.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HabitResponseDTO> getAllHabitsByUserId(Long userId) {
        List<Habit> habits = habitRepository.findAllHabitsByUserId(userId);
        if (habits == null || habits.isEmpty()) {
            // Devuelve lista vacía si no hay ninguno para ese userId
            return Collections.emptyList();
        }
        return habits.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HabitResponseDTO createHabit(HabitRequestDTO habitRequestDTO) {
        // Buscar el HabitType por ID (Usar el método que devuelve la entidad)
        HabitType habitType = habitTypeService.findHabitTypeEntityById(habitRequestDTO.getHabitTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de hábito no encontrado con ID: " + habitRequestDTO.getHabitTypeId()));

        // Buscar el Usuario por ID (Usar el método que devuelve la entidad)
        User user = userService.findUserEntityById(habitRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + habitRequestDTO.getUserId()));

        // Crear la entidad Habit
        Habit habit = new Habit();
        habit.setName(habitRequestDTO.getName());
        habit.setDescription(habitRequestDTO.getDescription());
        habit.setFrequency(habitRequestDTO.getFrequency());
        habit.setStartDate(habitRequestDTO.getStartDate());
        habit.setEndDate(habitRequestDTO.getEndDate());
        habit.setUser(user);  // Asignar la entidad User
        habit.setHabitType(habitType);  // Asignar la entidad HabitType

        // Guardar el hábito en la base de datos
        Habit savedHabit = habitRepository.save(habit);

        // Convertir la entidad Habit guardada a HabitResponseDTO y devolverla
        return mapToDTO(savedHabit);
    }

    @Override
    public HabitResponseDTO updateHabit(Long id, HabitRequestDTO habitRequestDTO) {
        Habit habit = habitRepository.findById(id) // Buscar el hábito por ID
                .orElseThrow(() -> new RuntimeException("Habit not found")); // Si no se encuentra, lanzar RuntimeException

        // Actualizar los atributos del hábito
        habit.setName(habitRequestDTO.getName());
        habit.setDescription(habitRequestDTO.getDescription());
        habit.setFrequency(habitRequestDTO.getFrequency());
        habit.setStartDate(habitRequestDTO.getStartDate());
        habit.setEndDate(habitRequestDTO.getEndDate());

        Habit updatedHabit = habitRepository.save(habit); // Guardar el hábito actualizado en la base de datos

        return mapToDTO(updatedHabit); // Devolver el DTO con los datos del hábito actualizado
    }

    @Override
    public void deleteHabit(Long id) {
        if (!habitRepository.existsById(id)) { // Verificar si el hábito existe
            throw new RuntimeException("Usuario no encontrado"); // Si no existe, lanzar RuntimeException
        }

        habitRepository.deleteById(id); // Eliminar el hábito por ID
    }

    @Override
    @Transactional
    public void deleteAllByTypeId(Long habitTypeId) {
        habitRepository.deleteAllByHabitTypeId(habitTypeId);
    }

    /**
     * Convierte una entidad Habit a un DTO HabitResponseDTO
     *
     * @param habit Entidad Habit a convertir
     * @return DTO HabitResponseDTO
     */
    private HabitResponseDTO mapToDTO(Habit habit) {
        if (habit == null) { // Verificar si la entidad Habit es nula
            throw new RuntimeException("El hábito no puede ser nulo.");
        }

        HabitResponseDTO dto = new HabitResponseDTO(); // Crear una nueva instancia de HabitResponseDTO
        dto.setId(habit.getId());
        dto.setName(habit.getName());
        dto.setDescription(habit.getDescription());
        dto.setFrequency(habit.getFrequency());
        dto.setStartDate(habit.getStartDate());
        dto.setEndDate(habit.getEndDate());

        // Convertir las entidades de HabitType y User a sus valores necesarios en el DTO
        dto.setHabitTypeName(habit.getHabitType() != null ? habit.getHabitType().getName() : null); // Asignar solo el nombre del tipo de hábito
        dto.setUserId(habit.getUser() != null ? habit.getUser().getId() : null);  // Asignar solo el ID del usuario

        return dto;
    }


    /**
     *
     */
    /*private Habit mapToEntity(HabitResponseDTO dto) {
        Habit habit = new Habit();

        // Mapear los atributos básicos
        habit.setId(dto.getId()); // Si es un nuevo habit, normalmente no asignas ID. Pero en caso de que lo necesites.
        habit.setName(dto.getName());
        habit.setDescription(dto.getDescription());
        habit.setFrequency(dto.getFrequency());
        habit.setStartDate(dto.getStartDate());
        habit.setEndDate(dto.getEndDate());

        // Mapear HabitType, si es necesario
        if (dto.getHabitTypeName() != null) {
            // Aquí deberías recuperar el HabitType desde la base de datos
            HabitType habitType = habitTypeService.findByName(dto.getHabitTypeName());
            habit.setHabitType(habitType);
        }

        // Mapear User, si es necesario
        if (dto.getUserId() != null) {
            // Aquí debes recuperar el User usando el ID
            User user = userService.getUserById(dto.getUserId());
            habit.setUser(user);
        }

        return habit;
    }*/


}