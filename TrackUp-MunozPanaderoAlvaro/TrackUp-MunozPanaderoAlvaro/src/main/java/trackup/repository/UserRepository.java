package trackup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trackup.entity.User;

import java.util.Optional;

/**
 * Repositorio para la entidad User
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Repository // Indica que esta clase es un repositorio de Spring
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario de acuerdo a su nombre de usuario
     *
     * @param username Nombre del usuario que se busca
     * @return Un objeto Optional que contiene el usuario si se encuentra, o vacío si no
     */
    Optional<User> findByUsername(String username);

}