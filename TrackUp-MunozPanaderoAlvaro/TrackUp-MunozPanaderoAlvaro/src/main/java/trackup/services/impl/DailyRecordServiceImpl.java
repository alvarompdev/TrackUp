package trackup.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trackup.dto.request.DailyRecordRequestDTO;
import trackup.dto.response.DailyRecordResponseDTO;
import trackup.entity.DailyRecord;
import trackup.entity.Habit;
import trackup.repository.DailyRecordRepository;
import trackup.services.DailyRecordService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de metas
 * Se encarga de la lógica de negocio relacionada con los metas,
 * incluyendo la creación, actualización, eliminación y obtención de metas
 *
 * Utiliza un repositorio para acceder a la base de datos y
 * convierte las entidades a DTOs para mantener la separación de capas
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Service // Anotación que indica que esta clase es un servicio
public class DailyRecordServiceImpl implements DailyRecordService {

    private final DailyRecordRepository dailyRecordRepository; // Repositorio de registros diarios
    private final HabitServiceImpl habitService; // Servicio para acceder a la información del hábito

    /**
     * Constructor de la clase
     *
     * @param dailyRecordRepository Repositorio de registros diarios
     */
    @Autowired
    public DailyRecordServiceImpl(DailyRecordRepository dailyRecordRepository, HabitServiceImpl habitService) {
        this.dailyRecordRepository = dailyRecordRepository;
        this.habitService = habitService;
    }

    @Override
    public Optional<DailyRecordResponseDTO> findDailyRecordById(Long id) {
        return dailyRecordRepository.findById(id) // Busca el registro diario por su ID
                .map(this::mapToDTO); // Si lo encuentra, lo transforma a un DTO
    }

    @Override
    public Optional<DailyRecordResponseDTO> findDailyRecordByDate(LocalDate date) {
        return dailyRecordRepository.findByDate(date) // Busca el registro diario por su fecha
                .map(this::mapToDTO); // Si lo encuentra, lo transforma a un DTO
    }

    @Override
    public Optional<DailyRecordResponseDTO> findDailyRecordByCompleted(Boolean completed) {
        return dailyRecordRepository.findByCompleted(completed) // Busca el registro diario por su estado de completado
                .map(this::mapToDTO); // Si lo encuentra, lo transforma a un DTO
    }

    @Override
    public List<DailyRecordResponseDTO> getAllDailyRecords() {
        return dailyRecordRepository.findAll() // Busca todos los registros diarios
                .stream() // Convierte la lista a un stream
                .map(this::mapToDTO) // Transforma cada registro diario a un DTO
                .toList(); // Convierte el stream de vuelta a una lista
    }

    @Override
    public DailyRecordResponseDTO createDailyRecord(DailyRecordRequestDTO dailyRecordRequestDTO) {
        // Crear nueva entidad
        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setDate(dailyRecordRequestDTO.getDate());
        dailyRecord.setCompleted(dailyRecordRequestDTO.getCompleted());

        // Buscar y asignar el hábito (usando la entidad como en HabitServiceImpl)
        Habit habit = habitService.findHabitEntityById(dailyRecordRequestDTO.getHabitId())
                .orElseThrow(() -> new RuntimeException("Habit not found with id: " + dailyRecordRequestDTO.getHabitId()));
        dailyRecord.setHabit(habit);

        DailyRecord savedDailyRecord = dailyRecordRepository.save(dailyRecord); // Guardar en base de datos

        return mapToDTO(savedDailyRecord); // Devuelve el DTO del registro diario guardado
    }


    @Override
    public DailyRecordResponseDTO updateDailyRecord(Long id, DailyRecordRequestDTO dailyRecordRequestDTO) {
        DailyRecord dailyRecord = dailyRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro diario no encontrado")); // Busca el registro diario por su ID

        // Actualiza los valores del registro diario
        dailyRecord.setDate(dailyRecordRequestDTO.getDate());
        dailyRecord.setCompleted(dailyRecordRequestDTO.getCompleted());

        DailyRecord updatedDailyRecord = dailyRecordRepository.save(dailyRecord); // Guarda el registro diario actualizado en la base de datos
        return mapToDTO(updatedDailyRecord); // Devuelve el DTO del registro diario actualizado
    }

    @Override
    public void deleteDailyRecord(Long id) {
        // Verifica si el registro diario existe
        if (!dailyRecordRepository.existsById(id)) {
            throw new RuntimeException("Registro diario no encontrado");
        }

        // Elimina el registro diario de la base de datos
        dailyRecordRepository.deleteById(id);
    }

    /**
     * Convierte una entidad DailyRecord a un DTO DailyRecordResponseDTO
     *
     * @param dailyRecord
     * @return DTO DailyRecordResponseDTO
     */
    private DailyRecordResponseDTO mapToDTO(DailyRecord dailyRecord) {
        return new DailyRecordResponseDTO(
                dailyRecord.getId(),
                dailyRecord.getDate(),
                dailyRecord.getCompleted(),
                dailyRecord.getHabit().getId()
        );
    }

}