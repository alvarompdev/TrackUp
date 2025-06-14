package trackup.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HabitTypeEntityTest {

    @Test
    void defaultConstructor_InitializesHabitsList() {
        HabitType ht = new HabitType();
        assertNotNull(ht.getHabits(), "La lista de habits no debe ser null");
        assertTrue(ht.getHabits().isEmpty(), "La lista de habits debe comenzar vacía");
    }

    @Test
    void allArgsConstructor_AssignsAllFields() {
        List<Habit> habits = new ArrayList<>();
        HabitType ht = new HabitType(5L, "Deporte", habits);

        assertEquals(5L, ht.getId());
        assertEquals("Deporte", ht.getName());
        assertSame(habits, ht.getHabits());
    }

    @Test
    void gettersAndSetters_WorkAsExpected() {
        HabitType ht = new HabitType();
        ht.setId(10L);
        ht.setName("Salud");
        List<Habit> list = Collections.emptyList();
        ht.setHabits(list);

        assertEquals(10L, ht.getId());
        assertEquals("Salud", ht.getName());
        assertSame(list, ht.getHabits());
    }

}