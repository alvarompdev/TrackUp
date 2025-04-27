package trackup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trackup.entity.HabitType;

import java.util.Optional;

/**
 * Repositorio para la entidad HabitType
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Repository
public interface HabitTypeRepository extends JpaRepository<HabitType, Long> {

    /**
     * Se busca el tipo de hábito por su nombre
     *
     * @param name Nombre del tipo de hábito
     * @return Tipo de hábito correspondiente al nombre proporcionado en el caso de que exista
     */
    Optional<HabitType> findByName(String name);

}