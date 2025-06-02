package trackup.services;

import trackup.dto.request.HabitRequestDTO;
import trackup.dto.response.HabitResponseDTO;
import trackup.entity.Habit;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que define todas las operaciones relacionadas con los hábitos
 * Proporciona métodos para obtener, crear, actualizar y eliminar hábitos
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public interface HabitService {

    /**
     * Se definen todos los métodos que se van a implementar posteriormente en la clase de implementación
     */

    /**
     * Busca un hábito por su ID
     *
     * @param id ID del hábito
     * @return Un objeto Optional que contiene el hábito si se encuentra, o vacío si no
     */
    Optional<HabitResponseDTO> findHabitById(Long id);

    /**
     * Busca un hábito por su ID
     *
     * @param id ID del hábito
     * @return Un objeto Optional que contiene el hábito si se encuentra, o vacío si no
     */
    Optional<Habit> findHabitEntityById(Long id);

    /**
     * Busca un hábito por su nombre
     *
     * @param name Nombre del hábito
     * @return Un objeto Optional que contiene el hábito si se encuentra, o vacío si no
     */
    Optional<Habit> findHabitByName(String name);

    /**
     * Busca un hábito por su nombre y el ID del usuario
     *
     * @param name   Nombre del hábito
     * @param userId ID del usuario
     * @return Un objeto Optional que contiene el hábito si se encuentra, o vacío si no
     */
    Optional<HabitResponseDTO> findHabitByNameAndUserId(String name, Long userId);

    /**
     * Obtiene todos los hábitos
     *
     * @return Lista que contiene todos los hábitos
     */
    List<HabitResponseDTO> getAllHabits();

    /**
     * Busca todos los hábitos de un usuario mediante su ID de usuario
     *
     * @param userId ID del usuario
     * @return Lista de hábitos del usuario
     */
    List<HabitResponseDTO> getAllHabitsByUserId(Long userId);

    /**
     * Crea un nuevo hábito
     *
     * @param habitRequestDTO Datos del hábito que se va a crear
     * @return Nuevo hábito creado con sus datos de respuesta
     */
    HabitResponseDTO createHabit(HabitRequestDTO habitRequestDTO);

    /**
     * Actualiza los datos de un hábito existente
     *
     * @param id ID del hábito a actualizar
     * @param habitRequestDTO Datos del hábito que se van a actualizar
     * @return Hábito actualizado con sus datos de respuesta
     */
    HabitResponseDTO updateHabit(Long id, HabitRequestDTO habitRequestDTO);

    /**
     * Elimina un hábito por su ID
     *
     * @param id ID del hábito a eliminar
     */
    void deleteHabit(Long id);

    void deleteAllByTypeId(Long habitTypeId);

}