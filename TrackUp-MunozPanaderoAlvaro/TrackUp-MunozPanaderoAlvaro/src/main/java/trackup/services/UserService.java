package trackup.services;

import trackup.dto.request.UserRequestDTO;
import trackup.dto.response.UserResponseDTO;
import trackup.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que define todas las operaciones relaciones con los usuarios
 * Proporciona métodos para obtener, crear, actualizar y eliminar usuarios
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public interface UserService {

    /**
     * Se definen todos los métodos que se van a implementar posteriormente en la clase de implementación
     */

    /**
     * Obtiene un usuario en base a su id
     *
     * @param id Identificador del usuario que se va a buscar
     * @return Usuario correspondiente al id proporcionado en el caso de que exista
     */
    UserResponseDTO getUserById(Long id);

    Optional<User> getUserEntityById(Long id);

    /**
     * Obtiene una lista con todos los usuarios registrados
     *
     * @return Lista de usuarios registrados
     */
    List<UserResponseDTO> getAllUsers();

    Optional<User> getUserByUsername(String username);

    /**
     * Crea un nuevo usuario
     *
     * @param userRequestDTO Datos de usuario que se va a crear
     * @return Usuario creado con sus datos de respuesta
     */
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    /**
     * Actualiza los datos de un usuario existente
     *
     * @param id Identificador del usuario que se va a actualizar
     * @param userRequestDTO Nuevos datos actualizados que tendrá el usuario
     * @return Usuario actualizado con los nuevos datos
     */
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);

    /**
     * Elimina un usuario en base a su identificador
     *
     * @param id Identificador del usuario que se va a eliminar
     */
    void deleteUser(Long id);

}