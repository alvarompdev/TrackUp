package trackup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trackup.entity.Habit;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar hábitos por fecha o por mascota
    // List<Habit> findByDate(LocalDate date);
    // List<Habit> findByPetId(Long petId);
}
