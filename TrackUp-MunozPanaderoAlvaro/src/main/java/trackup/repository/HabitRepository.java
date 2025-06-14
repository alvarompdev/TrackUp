package trackup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trackup.entity.Habit;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Habit
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Repository // Indica que esta clase es un repositorio
public interface HabitRepository extends JpaRepository<Habit, Long> {

    /**
     * Busca por el nombre del hábito
     *
     * @param name Nombre del hábito
     * @return Hábito correspondiente al nombre proporcionado en el caso de que exista
     */
    Optional<Habit> findByName(String name);

    /**
     * Busca un hábito por su nombre y el ID del usuario
     *
     * @param name Nombre del hábito
     * @param userId ID del usuario
     * @return Hábito correspondiente al nombre y ID de usuario proporcionados en el caso de que exista
     */
    Optional<Habit> findHabitByNameAndUserId(String name, Long userId);

    /**
     * Busca todos los hábitos de un usuario mediante su ID de usuario
     *
     * @param userId ID del usuario
     * @return Lista de hábitos del usuario
     */
    List<Habit> findAllHabitsByUserId(Long userId);

    void deleteAllByHabitTypeId(Long habitTypeId);

}