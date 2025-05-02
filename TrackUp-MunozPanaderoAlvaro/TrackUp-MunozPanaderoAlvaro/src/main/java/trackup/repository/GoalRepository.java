package trackup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trackup.dto.response.GoalResponseDTO;
import trackup.entity.Goal;

import java.util.Optional;

/**
 * Repositorio para la entidad 'Goal'
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Repository // Indica que esta interfaz es un repositorio de Spring
public interface GoalRepository extends JpaRepository<Goal, Long> {

    /**
     * Encuentra un objetivo por su nombre
     *
     * @param name Nombre del objetivo
     * @return Objetivo encontrado
     */
    Goal findGoalByName(String name);

    Optional<Goal> findGoalByNameAndUserId(String name, Long userId);

}