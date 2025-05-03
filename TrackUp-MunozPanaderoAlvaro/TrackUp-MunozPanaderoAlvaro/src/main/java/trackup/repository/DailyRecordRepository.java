package trackup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trackup.entity.DailyRecord;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repositorio para la entidad 'DailyRecord'
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Repository // Indica que esta clase es un repositorio
public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {

    /**
     * Encuentra un registro diario por su fecha
     *
     * @param date Fecha del registro diario
     * @return Registro diario encontrado
     */
    Optional<DailyRecord> findByDate(LocalDate date);

    /**
     * Encuentra un registro diario por su estado de completado
     *
     * @param completed Estado de completado del registro diario
     * @return Registro diario encontrado
     */
    Optional<DailyRecord> findByCompleted(Boolean completed);

}