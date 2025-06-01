package trackup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trackup.entity.Goal;
import trackup.entity.Habit;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad 'Goal'
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Repository // Indica que esta interfaz es un repositorio
public interface GoalRepository extends JpaRepository<Goal, Long> {

    /**
     * Encuentra un objetivo por su nombre
     *
     * @param name Nombre del objetivo
     * @return Objetivo encontrado
     */
    Optional<Goal> findByName(String name);

    /**
     * Encuentra un objetivo por su nombre y el ID del usuario
     *
     * @param name Nombre del objetivo
     * @param userId ID del usuario
     * @return Objetivo encontrado
     */
    Optional<Goal> findGoalByNameAndUserId(String name, Long userId);

    /**
     * Encuentra todos los objetivos de un usuario por su ID
     *
     * @param userId ID del usuario
     * @return Lista de objetivos encontrados
     */
    List<Goal> findAllGoalsByUserId(Long userId);

}