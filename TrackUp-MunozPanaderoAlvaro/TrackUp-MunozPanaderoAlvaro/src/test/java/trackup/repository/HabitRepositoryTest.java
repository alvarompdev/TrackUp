package trackup.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import trackup.entity.Habit;
import trackup.entity.User;
import trackup.entity.HabitType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HabitRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HabitRepository habitRepository;

    private User createUser() {
        User u = new User();
        u.setUsername("testuser");
        u.setEmail("test@example.com");
        u.setPassword("pass123");
        return entityManager.persistAndFlush(u);
    }

    private HabitType createHabitType() {
        HabitType ht = new HabitType();
        ht.setName("Health");
        return entityManager.persistAndFlush(ht);
    }

    @Test
    @DisplayName("guardar y recuperar hábito por ID")
    void whenSaveHabit_thenFindById() {
        User user = createUser();
        HabitType type = createHabitType();

        Habit h = new Habit();
        h.setName("Correr");
        h.setFrequency("Daily");
        h.setStartDate(LocalDate.now().minusDays(1));
        h.setEndDate(LocalDate.now().plusDays(30));
        h.setUser(user);
        h.setHabitType(type);

        Habit saved = habitRepository.saveAndFlush(h);
        Optional<Habit> found = habitRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Correr");
    }

    @Test
    @DisplayName("buscar por name existente")
    void whenFindByName_thenReturnHabit() {
        User user = createUser();
        HabitType type = createHabitType();

        Habit h = new Habit();
        h.setName("Leer");
        h.setFrequency("Weekly");
        h.setStartDate(LocalDate.now());
        h.setEndDate(LocalDate.now().plusWeeks(4));
        h.setUser(user);
        h.setHabitType(type);
        entityManager.persistAndFlush(h);

        Optional<Habit> found = habitRepository.findByName("Leer");
        assertThat(found).isPresent();
        assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("buscar por name y userId existente")
    void whenFindByNameAndUserId_thenReturnHabit() {
        User user = createUser();
        HabitType type = createHabitType();

        Habit h = new Habit();
        h.setName("Estudiar");
        h.setFrequency("Daily");
        h.setStartDate(LocalDate.now());
        h.setEndDate(LocalDate.now().plusDays(10));
        h.setUser(user);
        h.setHabitType(type);
        entityManager.persistAndFlush(h);

        Optional<Habit> found = habitRepository.findHabitByNameAndUserId("Estudiar", user.getId());
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("buscar hábitos por userId existente")
    void whenFindAllHabitsByUserId_thenReturnList() {
        User user = createUser();
        HabitType type = createHabitType();

        Habit h1 = new Habit();
        h1.setName("Yoga");
        h1.setFrequency("Daily");
        h1.setStartDate(LocalDate.now());
        h1.setEndDate(LocalDate.now().plusDays(20));
        h1.setUser(user);
        h1.setHabitType(type);
        entityManager.persistAndFlush(h1);

        List<Habit> list = habitRepository.findAllHabitsByUserId(user.getId());
        assertThat(list).hasSize(1);
    }

    @Test
    @DisplayName("buscar por name no existente devuelve vacío")
    void whenFindByUnknownName_thenEmpty() {
        Optional<Habit> found = habitRepository.findByName("Nada");
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("buscar name y userId no existente devuelve vacío")
    void whenFindByUnknownNameAndUser_thenEmpty() {
        Optional<Habit> found = habitRepository.findHabitByNameAndUserId("Nada", 99L);
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("buscar hábitos por userId no existente devuelve lista vacía")
    void whenFindAllByUnknownUser_thenEmptyList() {
        List<Habit> list = habitRepository.findAllHabitsByUserId(99L);
        assertThat(list).isEmpty();
    }

}