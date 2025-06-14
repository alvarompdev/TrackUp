package trackup.dto;

import org.junit.jupiter.api.Test;
import trackup.dto.request.DailyRecordRequestDTO;
import trackup.dto.response.DailyRecordResponseDTO;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DailyRecordDtoTest {

    @Test
    void requestDto_GettersSettersAndConstructor() {
        LocalDate date = LocalDate.of(2024, 1, 15);

        // Constructor con parámetros
        DailyRecordRequestDTO req = new DailyRecordRequestDTO(date, true, 5L, 1L);
        assertEquals(date, req.getDate());
        assertTrue(req.getCompleted());
        assertEquals(5L, req.getHabitId());

        // Setters
        LocalDate newDate = LocalDate.of(2024, 2, 1);
        req.setDate(newDate);
        req.setCompleted(false);
        req.setHabitId(8L);
        assertEquals(newDate, req.getDate());
        assertFalse(req.getCompleted());
        assertEquals(8L, req.getHabitId());
    }

    @Test
    void responseDto_GettersSettersAndConstructor() {
        LocalDate date = LocalDate.of(2024, 3, 10);

        // Constructor con parámetros
        DailyRecordResponseDTO resp = new DailyRecordResponseDTO(7L, date, true, 12L, 1L, "Habit Name");
        assertEquals(7L, resp.getId());
        assertEquals(date, resp.getDate());
        assertTrue(resp.getCompleted());
        assertEquals(12L, resp.getHabitId());

        // Setters
        resp.setId(9L);
        LocalDate d2 = LocalDate.of(2024, 4, 5);
        resp.setDate(d2);
        resp.setCompleted(false);
        resp.setHabitId(15L);
        assertEquals(9L, resp.getId());
        assertEquals(d2, resp.getDate());
        assertFalse(resp.getCompleted());
        assertEquals(15L, resp.getHabitId());
    }

}