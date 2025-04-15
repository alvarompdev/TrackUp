package trackup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trackup.entity.Goal;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
}
