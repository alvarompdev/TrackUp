package trackup.entity;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void defaultConstructor_InitializesLists() {
        User u = new User();
        assertNotNull(u.getHabits(), "La lista de habits no debe ser null");
        assertNotNull(u.getGoals(), "La lista de goals no debe ser null");
        assertTrue(u.getHabits().isEmpty());
        assertTrue(u.getGoals().isEmpty());
    }

    @Test
    void allArgsConstructor_AssignsAllFields() {
        List<Habit> habits = Collections.singletonList(new Habit());
        List<Goal> goals = Collections.singletonList(new Goal());
        User u = new User(5L, "usr", "pwd", "mail@dom.com", habits, goals);

        assertEquals(5L, u.getId());
        assertEquals("usr", u.getUsername());
        assertEquals("pwd", u.getPassword());
        assertEquals("mail@dom.com", u.getEmail());
        assertSame(habits, u.getHabits());
        assertSame(goals, u.getGoals());
    }

    @Test
    void gettersAndSetters_WorkAsExpected() {
        User u = new User();
        u.setId(42L);
        u.setUsername("alpha");
        u.setPassword("betapass");
        u.setEmail("x@y.com");

        List<Habit> h = new ArrayList<>();
        List<Goal> g = new ArrayList<>();
        u.setHabits(h);
        u.setGoals(g);

        assertEquals(42L, u.getId());
        assertEquals("alpha", u.getUsername());
        assertEquals("betapass", u.getPassword());
        assertEquals("x@y.com", u.getEmail());
        assertSame(h, u.getHabits());
        assertSame(g, u.getGoals());
    }

    @Test
    void validation_InvalidEmailAndNulls() {
        User u = new User();
        u.setUsername(null);
        u.setEmail("not-an-email");
        u.setPassword(null);

        Set<ConstraintViolation<User>> violations = validator.validate(u);
        // Esperamos violaciones de null en username, null en password, y formato de email
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("username"))); //error aqui
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void validation_ValidUser_NoViolations() {
        User u = new User();
        u.setUsername("validUser");
        u.setPassword("somePass");
        u.setEmail("ok@example.com");
        Set<ConstraintViolation<User>> violations = validator.validate(u);
        assertTrue(violations.isEmpty(), "No debe haber violaciones en un usuario válido");
    }

}