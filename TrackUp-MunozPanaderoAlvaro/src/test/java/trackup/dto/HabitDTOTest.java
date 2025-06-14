package trackup.dto;

import org.junit.jupiter.api.Test;
import trackup.dto.request.HabitRequestDTO;
import trackup.dto.response.HabitResponseDTO;
import trackup.entity.Habit;
import trackup.entity.HabitType;
import trackup.entity.User;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class HabitDtoTest {

    @Test
    void requestDto_GettersSettersAndConstructor() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end   = LocalDate.of(2024, 12, 31);

        // Constructor con parámetros
        HabitRequestDTO req = new HabitRequestDTO(
                "Meditación",
                "15 min diaria",
                "Diario",
                start,
                end,
                5L,
                2L
        );
        assertEquals("Meditación", req.getName());
        assertEquals("15 min diaria", req.getDescription());
        assertEquals("Diario", req.getFrequency());
        assertEquals(start, req.getStartDate());
        assertEquals(end, req.getEndDate());
        assertEquals(5L, req.getUserId());
        assertEquals(2L, req.getHabitTypeId());

        // Setters
        req.setName("Ejercicio");
        req.setDescription("30 min cardio");
        req.setFrequency("Semanal");
        LocalDate newStart = LocalDate.of(2025, 2, 1);
        LocalDate newEnd   = LocalDate.of(2025, 2, 28);
        req.setStartDate(newStart);
        req.setEndDate(newEnd);
        req.setUserId(7L);
        req.setHabitTypeId(3L);
        assertEquals("Ejercicio", req.getName());
        assertEquals("30 min cardio", req.getDescription());
        assertEquals("Semanal", req.getFrequency());
        assertEquals(newStart, req.getStartDate());
        assertEquals(newEnd, req.getEndDate());
        assertEquals(7L, req.getUserId());
        assertEquals(3L, req.getHabitTypeId());
    }

    @Test
    void responseDto_GettersSettersAndConstructor() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end   = LocalDate.of(2024, 12, 31);
        String typeName = "Salud";

        // Constructor con parámetros
        HabitResponseDTO resp = new HabitResponseDTO(
                10L, "Yoga", "Vinyasa diaria", "Diario",
                start, end, typeName, 5L
        );
        assertEquals(10L, resp.getId());
        assertEquals("Yoga", resp.getName());
        assertEquals("Vinyasa diaria", resp.getDescription());
        assertEquals("Diario", resp.getFrequency());
        assertEquals(start, resp.getStartDate());
        assertEquals(end, resp.getEndDate());
        assertEquals(typeName, resp.getHabitTypeName());
        assertEquals(5L, resp.getUserId());

        // Setters
        resp.setId(20L);
        resp.setName("Running");
        resp.setDescription("5K diario");
        resp.setFrequency("Diario");
        LocalDate s2 = LocalDate.of(2025, 3, 1);
        LocalDate e2 = LocalDate.of(2025, 3, 31);
        resp.setStartDate(s2);
        resp.setEndDate(e2);
        resp.setHabitTypeName("Fitness");
        resp.setUserId(8L);
        assertEquals(20L, resp.getId());
        assertEquals("Running", resp.getName());
        assertEquals("5K diario", resp.getDescription());
        assertEquals("Diario", resp.getFrequency());
        assertEquals(s2, resp.getStartDate());
        assertEquals(e2, resp.getEndDate());
        assertEquals("Fitness", resp.getHabitTypeName());
        assertEquals(8L, resp.getUserId());
    }

    @Test
    void responseDto_EntityConstructor_MapsAllFields() {
        // Preparamos entidad Habit
        LocalDate start = LocalDate.of(2024, 4, 1);
        LocalDate end   = LocalDate.of(2024, 4, 30);
        User user = new User();
        user.setId(99L);
        HabitType type = new HabitType();
        type.setId(3L);
        type.setName("Bienestar");

        Habit h = new Habit(
                7L,
                "Meditación",
                "Mindfulness",
                "Diario",
                start,
                end,
                user,
                type,
                Collections.emptyList()
        );

        HabitResponseDTO dto = new HabitResponseDTO(h);

        assertEquals(7L, dto.getId());
        assertEquals("Meditación", dto.getName());
        assertEquals("Mindfulness", dto.getDescription());
        assertEquals("Diario", dto.getFrequency());
        assertEquals(start, dto.getStartDate());
        assertEquals(end, dto.getEndDate());
        assertEquals("Bienestar", dto.getHabitTypeName());
        assertEquals(99L, dto.getUserId());
    }

    @Test
    void responseDto_EntityConstructor_NullsHandledGracefully() {
        // Habit sin user ni habitType
        Habit h = new Habit();
        h.setId(11L);
        h.setName("Leer");
        h.setDescription("30 min libro");
        h.setFrequency("Diario");
        h.setStartDate(LocalDate.of(2024,5,1));
        h.setEndDate(LocalDate.of(2024,5,31));
        // user y habitType quedan null

        HabitResponseDTO dto = new HabitResponseDTO(h);

        assertEquals(11L, dto.getId());
        assertEquals("Leer", dto.getName());
        assertEquals("30 min libro", dto.getDescription());
        assertEquals("Diario", dto.getFrequency());
        assertEquals(LocalDate.of(2024,5,1), dto.getStartDate());
        assertEquals(LocalDate.of(2024,5,31), dto.getEndDate());
        assertNull(dto.getHabitTypeName(), "Cuando habitType es null, habitTypeName debe ser null");
        assertNull(dto.getUserId(), "Cuando user es null, userId debe ser null");
    }

}