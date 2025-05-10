package trackup.dto;

import org.junit.jupiter.api.Test;
import trackup.dto.request.HabitTypeRequestDTO;
import trackup.dto.response.HabitTypeResponseDTO;

import static org.junit.jupiter.api.Assertions.*;

class HabitTypeDtoTest {

    @Test
    void requestDto_GettersSettersAndConstructor() {
        // Constructor con parámetro
        HabitTypeRequestDTO req = new HabitTypeRequestDTO("Productividad");
        assertEquals("Productividad", req.getName());

        // Setter
        req.setName("Salud");
        assertEquals("Salud", req.getName());
    }

    @Test
    void responseDto_GettersSettersAndConstructor() {
        // Constructor con parámetros
        HabitTypeResponseDTO resp = new HabitTypeResponseDTO(1L, "Bienestar");
        assertEquals(1L, resp.getId());
        assertEquals("Bienestar", resp.getName());

        // Setters
        resp.setId(2L);
        resp.setName("Fitness");
        assertEquals(2L, resp.getId());
        assertEquals("Fitness", resp.getName());
    }

}