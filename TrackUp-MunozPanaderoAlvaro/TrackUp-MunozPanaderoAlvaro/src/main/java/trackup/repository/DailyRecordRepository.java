package trackup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trackup.entity.DailyRecord;

@Repository
public interface DailyRecordRepository extends JpaRepository<DailyRecord, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar registros por fecha o por mascota
    // List<DailyRecord> findByDate(LocalDate date);
    // List<DailyRecord> findByPetId(Long petId);
}