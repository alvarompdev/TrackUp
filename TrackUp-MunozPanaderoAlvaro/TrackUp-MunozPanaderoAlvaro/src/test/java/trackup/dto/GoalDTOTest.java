package trackup.dto;

import org.junit.jupiter.api.Test;
import trackup.dto.request.GoalRequestDTO;
import trackup.dto.response.GoalResponseDTO;
import trackup.entity.Goal;
import trackup.entity.User;

import static org.junit.jupiter.api.Assertions.*;

class GoalDtoTest {

    @Test
    void requestDto_GettersSettersAndConstructor() {
        // Constructor con parámetros
        GoalRequestDTO req = new GoalRequestDTO("Aprender Java", "Completar curso avanzado", 42L);
        assertEquals("Aprender Java", req.getName());
        assertEquals("Completar curso avanzado", req.getDescription());
        assertEquals(42L, req.getUserId());

        // Setters
        req.setName("Dominar Spring");
        req.setDescription("Crear proyecto real");
        req.setUserId(99L);
        assertEquals("Dominar Spring", req.getName());
        assertEquals("Crear proyecto real", req.getDescription());
        assertEquals(99L, req.getUserId());
    }

    @Test
    void responseDto_GettersSettersAndConstructor() {
        // Constructor con parámetros
        GoalResponseDTO resp = new GoalResponseDTO(7L, "Ejercicio", "30 min diarios", 12L);
        assertEquals(7L, resp.getId());
        assertEquals("Ejercicio", resp.getName());
        assertEquals("30 min diarios", resp.getDescription());
        assertEquals(12L, resp.getUserId());

        // Setters
        resp.setId(8L);
        resp.setName("Meditación");
        resp.setDescription("15 min mañanas");
        resp.setUserId(21L);
        assertEquals(8L, resp.getId());
        assertEquals("Meditación", resp.getName());
        assertEquals("15 min mañanas", resp.getDescription());
        assertEquals(21L, resp.getUserId());
    }

    @Test
    void responseDto_EntityConstructor_MapsAllFields() {
        User user = new User();
        user.setId(55L);
        Goal goal = new Goal(33L, "Leer libros", "30 páginas día", user);

        GoalResponseDTO dto = new GoalResponseDTO(goal);

        assertEquals(33L, dto.getId());
        assertEquals("Leer libros", dto.getName());
        assertEquals("30 páginas día", dto.getDescription());
        assertEquals(55L, dto.getUserId());
    }

    @Test
    void responseDto_EntityConstructor_NullUserHandled() {
        Goal goal = new Goal();
        goal.setId(44L);
        goal.setName("Aprender piano");
        goal.setDescription("Práctica diaria");
        // user queda null

        GoalResponseDTO dto = new GoalResponseDTO(goal);

        assertEquals(44L, dto.getId());
        assertEquals("Aprender piano", dto.getName());
        assertEquals("Práctica diaria", dto.getDescription());
        assertNull(dto.getUserId(), "Si user es null, userId debe ser null");
    }

}