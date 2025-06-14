package trackup.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import trackup.entity.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DailyRecordRepositoryTest {

    @Autowired
    private DailyRecordRepository dailyRecordRepository;
    @Autowired
    private HabitRepository habitRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HabitTypeRepository habitTypeRepository;

    private User makeUser() {
        User u = new User();
        u.setUsername("u");
        u.setEmail("u@example.com");
        u.setPassword("p");
        return userRepository.saveAndFlush(u);
    }
    private HabitType makeType() {
        HabitType t = new HabitType();
        t.setName("Tipo");
        return habitTypeRepository.saveAndFlush(t);
    }
    private Habit makeHabit() {
        Habit h = new Habit();
        h.setName("H");
        h.setFrequency("D");
        h.setStartDate(LocalDate.now());
        h.setEndDate(LocalDate.now().plusDays(1));
        h.setUser(makeUser());
        h.setHabitType(makeType());
        return habitRepository.saveAndFlush(h);
    }

    @Test
    @DisplayName("save & findById")
    void whenSave_thenFindById() {
        Habit h = makeHabit();
        DailyRecord r = new DailyRecord();
        r.setDate(LocalDate.of(2025,1,1));
        r.setCompleted(true);
        r.setHabit(h);

        DailyRecord saved = dailyRecordRepository.saveAndFlush(r);
        Optional<DailyRecord> found = dailyRecordRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getCompleted()).isTrue();
    }

    @Test
    @DisplayName("findByDate returns record or empty")
    void whenFindByDate() {
        Habit h = makeHabit();
        DailyRecord r = new DailyRecord();
        r.setDate(LocalDate.of(2025,2,2));
        r.setCompleted(false);
        r.setHabit(h);
        dailyRecordRepository.saveAndFlush(r);

        assertThat(dailyRecordRepository.findByDate(LocalDate.of(2025,2,2))).isPresent();
        assertThat(dailyRecordRepository.findByDate(LocalDate.of(2025,3,3))).isEmpty();
    }

    @Test
    @DisplayName("findByCompleted returns record or empty")
    void whenFindByCompleted() {
        Habit h = makeHabit();
        DailyRecord r = new DailyRecord();
        r.setDate(LocalDate.of(2025,4,4));
        r.setCompleted(true);
        r.setHabit(h);
        dailyRecordRepository.saveAndFlush(r);

        assertThat(dailyRecordRepository.findByCompleted(true)).isPresent();
        assertThat(dailyRecordRepository.findByCompleted(false)).isEmpty();
    }

}