package trackup.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GoalEntityTest {

    @Test
    void allArgsConstructor_AssignsAllFields() {
        User user = new User();
        user.setId(77L);
        Goal g = new Goal(5L, "Viajar", "Visitar 5 países", user);

        assertEquals(5L, g.getId());
        assertEquals("Viajar", g.getName());
        assertEquals("Visitar 5 países", g.getDescription());
        assertSame(user, g.getUser());
    }

    @Test
    void gettersAndSetters_WorkAsExpected() {
        Goal g = new Goal();
        g.setId(9L);
        g.setName("Escribir");
        g.setDescription("Blog semanal");
        User u = new User();
        u.setId(88L);
        g.setUser(u);

        assertEquals(9L, g.getId());
        assertEquals("Escribir", g.getName());
        assertEquals("Blog semanal", g.getDescription());
        assertSame(u, g.getUser());
    }

}