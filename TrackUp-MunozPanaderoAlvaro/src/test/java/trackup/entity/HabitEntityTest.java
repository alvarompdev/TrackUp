package trackup.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HabitEntityTest {

    @Test
    void defaultConstructor_InitializesDailyRecordsList() {
        Habit h = new Habit();
        assertNotNull(h.getDailyRecords(), "dailyRecords no debe ser null");
        assertTrue(h.getDailyRecords().isEmpty(), "dailyRecords debe comenzar vacío");
    }

    @Test
    void allArgsConstructor_AssignsAllFields() {
        User user = new User(); user.setId(1L);
        HabitType type = new HabitType(); type.setId(2L);
        LocalDate sd = LocalDate.of(2024, 6, 1);
        LocalDate ed = LocalDate.of(2024, 6, 30);
        List<DailyRecord> records = new ArrayList<>();

        Habit h = new Habit(
                5L, "Yoga", "Vinyasa", "Semanal",
                sd, ed,
                user, type,
                records
        );

        assertEquals(5L, h.getId());
        assertEquals("Yoga", h.getName());
        assertEquals("Vinyasa", h.getDescription());
        assertEquals("Semanal", h.getFrequency());
        assertEquals(sd, h.getStartDate());
        assertEquals(ed, h.getEndDate());
        assertSame(user, h.getUser());
        assertSame(type, h.getHabitType());
        assertSame(records, h.getDailyRecords());
    }

    @Test
    void gettersAndSetters_WorkAsExpected() {
        Habit h = new Habit();
        h.setId(9L);
        h.setName("Correr");
        h.setDescription("5K diario");
        h.setFrequency("Diario");
        LocalDate sd = LocalDate.of(2024, 7, 1);
        LocalDate ed = LocalDate.of(2024, 7, 31);
        h.setStartDate(sd);
        h.setEndDate(ed);

        User u = new User(); u.setId(3L);
        HabitType t = new HabitType(); t.setId(4L);
        List<DailyRecord> dr = Collections.emptyList();
        h.setUser(u);
        h.setHabitType(t);
        h.setDailyRecords(dr);

        assertEquals(9L, h.getId());
        assertEquals("Correr", h.getName());
        assertEquals("5K diario", h.getDescription());
        assertEquals("Diario", h.getFrequency());
        assertEquals(sd, h.getStartDate());
        assertEquals(ed, h.getEndDate());
        assertSame(u, h.getUser());
        assertSame(t, h.getHabitType());
        assertSame(dr, h.getDailyRecords());
    }

}