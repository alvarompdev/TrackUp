package trackup.dto;

import org.junit.jupiter.api.Test;
import trackup.dto.request.UserRequestDTO;
import trackup.dto.response.UserResponseDTO;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    @Test
    void requestDto_GettersSettersAndConstructor() {
        // Constructor con parámetros
        UserRequestDTO req = new UserRequestDTO("user1", "email@example.com", "Pass123!");
        assertEquals("user1", req.getUsername());
        assertEquals("email@example.com", req.getEmail());
        assertEquals("Pass123!", req.getPassword());

        // Setters
        req.setUsername("user2");
        req.setEmail("other@example.com");
        req.setPassword("NewPass456?");
        assertEquals("user2", req.getUsername());
        assertEquals("other@example.com", req.getEmail());
        assertEquals("NewPass456?", req.getPassword());
    }

    @Test
    void responseDto_GettersSettersAndConstructor() {
        // Constructor con parámetros
        UserResponseDTO resp = new UserResponseDTO(10L, "userA", "a@b.com");
        assertEquals(10L, resp.getId());
        assertEquals("userA", resp.getUsername());
        assertEquals("a@b.com", resp.getEmail());

        // Setters
        resp.setId(20L);
        resp.setUsername("userB");
        resp.setEmail("c@d.com");
        assertEquals(20L, resp.getId());
        assertEquals("userB", resp.getUsername());
        assertEquals("c@d.com", resp.getEmail());
    }

}