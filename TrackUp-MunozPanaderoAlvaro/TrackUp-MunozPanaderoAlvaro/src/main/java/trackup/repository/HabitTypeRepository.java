package trackup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trackup.entity.HabitType;

@Repository
public interface HabitTypeRepository extends JpaRepository<HabitType, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar tipos de hábitos por nombre o categoría
    // List<HabitType> findByName(String name);
    // List<HabitType> findByCategory(String category);
}
