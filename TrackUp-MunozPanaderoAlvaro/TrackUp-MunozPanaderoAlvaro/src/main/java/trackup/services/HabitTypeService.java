package trackup.services;

import trackup.dto.request.HabitTypeRequestDTO;
import trackup.dto.response.HabitTypeResponseDTO;
import trackup.entity.HabitType;

import java.util.List;
import java.util.Optional;

public interface HabitTypeService {

    /**
     * Se definen todos los métodos que se van a implementar posteriormente en la clase de implementación
     */

    /**
     * Obtiene un tipo de hábito por su ID
     *
     * @param id ID del tipo de hábito que se va a buscar
     * @return Tipo de hábito correspondiente al ID proporcionado en el caso de que exista (por eso es un objeto Optional)
     */
    Optional<HabitTypeResponseDTO> getHabitTypeById(Long id);

    /**
     * Obtiene un tipo de hábito por su ID
     *
     * @param id ID del tipo de hábito que se va a buscar
     * @return Tipo de hábito correspondiente al ID proporcionado en el caso de que exista (por eso es un objeto Optional)
     */
    Optional<HabitType> getHabitTypeEntityById(Long id);

    /**
     * Obtiene un tipo de hábito por su nombre
     *
     * @param name Nombre del tipo de hábito que se va a buscar
     * @return Tipo de hábito correspondiente al nombre proporcionado en el caso de que exista (por eso es un objeto Optional)
     */
    Optional<HabitTypeResponseDTO> getHabitTypeByName(String name);

    /**
     * Obtiene todos los tipos de hábito
     *
     * @return Lista que contiene todos los tipos de hábito
     */
    List<HabitTypeResponseDTO> getAllHabitTypes();

    /**
     * Crea un nuevo tipo de hábito
     *
     * @param habitTypeRequestDTO Datos del tipo de hábito que se va a crear
     * @return Tipo de hábito creado con sus datos de respuesta
     */
    HabitTypeResponseDTO createHabitType(HabitTypeRequestDTO habitTypeRequestDTO);

    /**
     * Actualiza los datos de un tipo de hábito existente
     *
     * @param id ID del tipo de hábito a actualizar
     * @param habitTypeRequestDTO Datos del tipo de hábito que se van a actualizar
     * @return Tipo de hábito actualizado con sus datos de respuesta
     */
    HabitTypeResponseDTO updateHabitType(Long id, HabitTypeRequestDTO habitTypeRequestDTO);

    /**
     * Elimina un tipo de hábito en base a su identificador
     *
     * @param id Identificador del tipo de hábito que se va a eliminar
     */
    void deleteHabitType(Long id);

}