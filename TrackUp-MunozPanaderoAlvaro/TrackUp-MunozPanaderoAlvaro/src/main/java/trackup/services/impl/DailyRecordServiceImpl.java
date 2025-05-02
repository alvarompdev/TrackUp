package trackup.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trackup.dto.response.DailyRecordResponseDTO;
import trackup.entity.DailyRecord;
import trackup.entity.Habit;
import trackup.repository.DailyRecordRepository;
import trackup.repository.HabitRepository;
import trackup.services.DailyRecordService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio 'DailyRecordService'
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Service // Indica que esta clase es un servicio de Spring
public class DailyRecordServiceImpl implements DailyRecordService {

    private final DailyRecordRepository dailyRecordRepository;
    private final HabitRepository habitRepository;
    private final HabitServiceImpl habitService; // Servicio para acceder a la información del hábito

    /**
     * Constructor de la clase
     *
     * @param dailyRecordRepository Repositorio de registros diarios
     */
    @Autowired
    public DailyRecordServiceImpl(DailyRecordRepository dailyRecordRepository, HabitRepository habitRepository, HabitServiceImpl habitService) {
        this.dailyRecordRepository = dailyRecordRepository;
        this.habitRepository = habitRepository;
        this.habitService = habitService;
    }

    @Override
    public Optional<DailyRecordResponseDTO> getDailyRecordById(Long id) {
        return dailyRecordRepository.findById(id) // Busca el registro diario por su ID
                .map(dailyRecord -> new DailyRecordResponseDTO( // Si lo encuentra, lo transforma a un DTO
                        dailyRecord.getId(),
                        dailyRecord.getDate(),
                        dailyRecord.getCompleted(),
                        dailyRecord.getHabit().getId() // Asigna el ID del hábito asociado
                ));
    }

    @Override
    public Optional<DailyRecordResponseDTO> getDailyRecordByDate(LocalDate date) {
        return dailyRecordRepository.findDailyRecordByDate(date) // Busca el registro diario por su fecha
                .map(dailyRecord -> new DailyRecordResponseDTO( // Si lo encuentra, lo transforma a un DTO
                        dailyRecord.getId(),
                        dailyRecord.getDate(),
                        dailyRecord.getCompleted(),
                        dailyRecord.getHabit().getId() // Asigna el ID del hábito asociado
                ));
    }

    @Override
    public Optional<DailyRecordResponseDTO> getDailyRecordByCompleted(Boolean completed) {
        return dailyRecordRepository.findDailyRecordByCompleted(completed) // Busca el registro diario por su estado de completado
                .map(dailyRecord -> new DailyRecordResponseDTO( // Si lo encuentra, lo transforma a un DTO
                        dailyRecord.getId(),
                        dailyRecord.getDate(),
                        dailyRecord.getCompleted(),
                        dailyRecord.getHabit().getId() // Asigna el ID del hábito asociado
                ));
    }

    @Override
    public List<DailyRecordResponseDTO> getAllDailyRecords() {
        return dailyRecordRepository.findAll() // Busca todos los registros diarios
                .stream() // Convierte la lista a un stream
                .map(dailyRecord -> new DailyRecordResponseDTO( // Transforma cada registro diario a un DTO
                        dailyRecord.getId(),
                        dailyRecord.getDate(),
                        dailyRecord.getCompleted(),
                        dailyRecord.getHabit().getId() // Asigna el ID del hábito asociado
                ))
                .toList(); // Convierte el stream de vuelta a una lista
    }

    @Override
    public DailyRecordResponseDTO createDailyRecord(DailyRecordResponseDTO dailyRecordResponseDTO) {
        // Crear nueva entidad
        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setDate(dailyRecordResponseDTO.getDate());
        dailyRecord.setCompleted(dailyRecordResponseDTO.getCompleted());

        // Buscar y asignar el hábito (usando la entidad como en HabitServiceImpl)
        Habit habit = habitService.getHabitEntityById(dailyRecordResponseDTO.getHabitId())
                .orElseThrow(() -> new RuntimeException("Habit not found with id: " + dailyRecordResponseDTO.getHabitId()));
        dailyRecord.setHabit(habit);

        // Guardar en base de datos
        DailyRecord saved = dailyRecordRepository.save(dailyRecord);

        // Devolver respuesta DTO
        return new DailyRecordResponseDTO(
                saved.getId(),
                saved.getDate(),
                saved.getCompleted(),
                saved.getHabit().getId()
        );
    }


    @Override
    public DailyRecordResponseDTO updateDailyRecord(Long id, DailyRecordResponseDTO dailyRecordResponseDTO) {
        // Busca el registro diario por su ID
        DailyRecord dailyRecord = dailyRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro diario no encontrado"));

        // Actualiza los valores del registro diario
        dailyRecord.setDate(dailyRecordResponseDTO.getDate());
        dailyRecord.setCompleted(dailyRecordResponseDTO.getCompleted());

        // Guarda el registro diario actualizado en la base de datos
        DailyRecord updatedDailyRecord = dailyRecordRepository.save(dailyRecord);

        // Devuelve el DTO del registro diario actualizado
        return new DailyRecordResponseDTO(
                updatedDailyRecord.getId(),
                updatedDailyRecord.getDate(),
                updatedDailyRecord.getCompleted(),
                updatedDailyRecord.getHabit().getId() // Asigna el ID del hábito asociado
        );
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

}