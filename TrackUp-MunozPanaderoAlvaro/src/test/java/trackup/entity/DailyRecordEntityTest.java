package trackup.entity;

import org.junit.jupiter.api.Test;
import trackup.entity.DailyRecord;
import trackup.entity.Habit;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DailyRecordEntityTest {

    @Test
    void defaultConstructor_FieldValuesAndHabitNull() {
        DailyRecord dr = new DailyRecord();
        // Por defecto, id null, date null, completed null, habit null
        assertNull(dr.getId());
        assertNull(dr.getDate());
        assertNull(dr.getCompleted());
        assertNull(dr.getHabit());
    }

    @Test
    void allArgsConstructor_AssignsAllFields() {
        Habit habit = new Habit();
        habit.setId(3L);
        LocalDate date = LocalDate.of(2024, 5, 20);
        DailyRecord dr = new DailyRecord(11L, date, true, habit);

        assertEquals(11L, dr.getId());
        assertEquals(date, dr.getDate());
        assertTrue(dr.getCompleted());
        assertSame(habit, dr.getHabit());
    }

    @Test
    void gettersAndSetters_WorkAsExpected() {
        DailyRecord dr = new DailyRecord();
        dr.setId(17L);
        LocalDate date = LocalDate.of(2024, 6, 1);
        dr.setDate(date);
        dr.setCompleted(false);
        Habit h = new Habit();
        h.setId(5L);
        dr.setHabit(h);

        assertEquals(17L, dr.getId());
        assertEquals(date, dr.getDate());
        assertFalse(dr.getCompleted());
        assertSame(h, dr.getHabit());
    }

}