package trackup.services;

import trackup.dto.response.DailyRecordResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que define todas las operaciones relacionadas con los registros diarios
 * Proporciona métodos para obtener, crear, actualizar y eliminar registros diarios
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public interface DailyRecordService {

    /**
     * Encuentra un registro diario por su ID
     *
     * @param id ID del registro diario
     * @return Registro diario encontrado
     */
    Optional<DailyRecordResponseDTO> findDailyRecordById(Long id);

    /**
     * Encuentra un registro diario por su fecha
     *
     * @param date Fecha del registro diario
     * @return Registro diario encontrado
     */
    Optional<DailyRecordResponseDTO> findDailyRecordByDate(LocalDate date);

    /**
     * Encuentra un registro diario por su estado de completado
     *
     * @param completed Estado de completado del registro diario
     * @return Registro diario encontrado
     */
    Optional<DailyRecordResponseDTO> findDailyRecordByCompleted(Boolean completed);

    /**
     * Crea un nuevo registro diario
     *
     * @return Registro diario creado
     */
    List<DailyRecordResponseDTO> getAllDailyRecords();

    /**
     * Crea un nuevo registro diario
     *
     * @param dailyRecordResponseDTO Datos del registro diario que se va a crear
     * @return Registro diario creado
     */
    DailyRecordResponseDTO createDailyRecord(DailyRecordResponseDTO dailyRecordResponseDTO);

    /**
     * Actualiza un registro diario existente
     *
     * @param id                      ID del registro diario que se va a actualizar
     * @param dailyRecordResponseDTO Datos del registro diario que se va a actualizar
     * @return Registro diario actualizado
     */
    DailyRecordResponseDTO updateDailyRecord(Long id, DailyRecordResponseDTO dailyRecordResponseDTO);

    /**
     * Elimina un registro diario existente
     *
     * @param id ID del registro diario que se va a eliminar
     */
    void deleteDailyRecord(Long id);

}