package trackup.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import trackup.entity.HabitType;
import trackup.repository.HabitTypeRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HabitTypeRepositoryTest {

    @Autowired
    private HabitTypeRepository repository;

    @Test
    @DisplayName("save & findById")
    void whenSave_thenFindById() {
        HabitType t = new HabitType();
        t.setName("Salud");

        HabitType saved = repository.saveAndFlush(t);

        Optional<HabitType> found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Salud");
    }

    @Test
    @DisplayName("findByName existente y no existente")
    void whenFindByName() {
        HabitType t = new HabitType();
        t.setName("Deporte");
        repository.saveAndFlush(t);

        assertThat(repository.findByName("Deporte")).isPresent();
        assertThat(repository.findByName("Nada")).isEmpty();
    }

}