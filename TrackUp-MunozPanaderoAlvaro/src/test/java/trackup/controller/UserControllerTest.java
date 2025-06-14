package trackup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import trackup.dto.request.UserRequestDTO;
import trackup.dto.response.UserResponseDTO;
import trackup.entity.Habit;
import trackup.entity.User;
import trackup.services.UserService;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @InjectMocks
    private UserController controller;

    @Mock
    private UserService service;

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /api/users/users -> 200 + lista de usuarios")
    void getAllUsers_ok() throws Exception {
        List<UserResponseDTO> list = List.of(new UserResponseDTO(1L, "ana", "ana@example.com"));
        when(service.getAllUsers()).thenReturn(list);

        mockMvc.perform(get("/api/users/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("ana"));
    }

    @Test
    @DisplayName("GET /api/users/users -> 204 cuando lista vacía")
    void getAllUsers_noContent() throws Exception {
        when(service.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users/users"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/users/user/{id} -> 400 si id negativo")
    void getById_badRequest() throws Exception {
        mockMvc.perform(get("/api/users/user/{id}", -5L))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/users/user/{id} -> 200 si existe")
    void getById_found() throws Exception {
        UserResponseDTO dto = new UserResponseDTO(2L, "pepe", "pepe@x.com");
        when(service.findUserById(2L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/users/user/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("pepe@x.com"));
    }

    @Test
    @DisplayName("GET /api/users/user/{id} -> 404 si no existe")
    void getById_notFound() throws Exception {
        when(service.findUserById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/user/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/users/user/username/{username} -> 400 si inválido")
    void getByUsername_badRequest() throws Exception {
        mockMvc.perform(get("/api/users/user/username/{username}", "   "))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/users/user/username/{username} -> 200 si existe")
    void getByUsername_found() throws Exception {
        UserResponseDTO dto = new UserResponseDTO(3L, "luis", "luis@x.com");
        when(service.findUserByUsername("luis")).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/users/user/username/{username}", "luis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    @DisplayName("GET /api/users/user/{id}/habits -> 200 lista de hábitos")
    void getUserHabits_ok() throws Exception {
        User u = new User();
        u.setId(4L);
        Habit h = new Habit(); h.setId(10L); h.setName("Correr");
        u.setHabits(List.of(h));
        when(service.findUserEntityById(4L)).thenReturn(Optional.of(u));

        mockMvc.perform(get("/api/users/user/{id}/habits", 4L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Correr"));
    }

    @Test
    @DisplayName("GET /api/users/user/{id}/habits -> 404 si no existe")
    void getUserHabits_notFound() throws Exception {
        when(service.findUserEntityById(5L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/user/{id}/habits", 5L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/users/user -> 400 si DTO nulo")
    void createUser_badRequest() throws Exception {
        mockMvc.perform(post("/api/users/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/users/user -> 201 y usuario creado")
    void createUser_created() throws Exception {
        UserRequestDTO req = new UserRequestDTO("javi", "javi@x.com", "pwd");
        UserResponseDTO res = new UserResponseDTO(6L, "javi", "javi@x.com");
        when(service.createUser(any())).thenReturn(res);

        mockMvc.perform(post("/api/users/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(6));
    }

    @Test
    @DisplayName("PUT /api/users/user/{id} -> 400 si id negativo")
    void updateUser_badRequest() throws Exception {
        UserRequestDTO req = new UserRequestDTO("x", "x@x.com", "pwd");
        mockMvc.perform(put("/api/users/user/{id}", -1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/users/user/{id} -> 404 si no existe")
    void updateUser_notFound() throws Exception {
        UserRequestDTO req = new UserRequestDTO("x", "x@x.com", "pwd");
        when(service.updateUser(eq(8L), any()))
                .thenThrow(new RuntimeException("no existe"));

        mockMvc.perform(put("/api/users/user/{id}", 8L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/users/user/{id} -> 200 si existe")
    void updateUser_ok() throws Exception {
        UserRequestDTO req = new UserRequestDTO("ana", "ana@x.com", "pwd");
        UserResponseDTO res = new UserResponseDTO(9L, "ana", "ana@x.com");
        when(service.updateUser(eq(9L), any())).thenReturn(res);

        mockMvc.perform(put("/api/users/user/{id}", 9L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("ana"));
    }

    @Test
    @DisplayName("DELETE /api/users/user/{id} -> 400 si id negativo")
    void deleteUser_badRequest() throws Exception {
        mockMvc.perform(delete("/api/users/user/{id}", -2L))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/users/user/{id} -> 404 si no existe")
    void deleteUser_notFound() throws Exception {
        doThrow(new RuntimeException("no existe"))
                .when(service).deleteUser(11L);

        mockMvc.perform(delete("/api/users/user/{id}", 11L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/users/user/{id} -> 204 si eliminado")
    void deleteUser_noContent() throws Exception {
        mockMvc.perform(delete("/api/users/user/{id}", 12L))
                .andExpect(status().isNoContent());
    }

}