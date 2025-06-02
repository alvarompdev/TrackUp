package trackup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import trackup.dto.request.HabitTypeRequestDTO;
import trackup.dto.response.HabitTypeResponseDTO;
import trackup.services.HabitTypeService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HabitTypeControllerTest {

    @InjectMocks
    private trackup.controller.HabitTypeController controller;

    @Mock
    private HabitTypeService service;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /api/habit-types/habit-types -> 200 + lista")
    void getAll_ok() throws Exception {
        HabitTypeResponseDTO dto = new HabitTypeResponseDTO(1L, "Salud");
        when(service.getAllHabitTypes()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/habit-types/habit-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Salud"));
    }

    @Test
    @DisplayName("GET /api/habit-types/habit-types -> 204 vacía")
    void getAll_noContent() throws Exception {
        when(service.getAllHabitTypes()).thenReturn(List.of());

        mockMvc.perform(get("/api/habit-types/habit-types"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/habit-types/habit-type/{id} -> 400 id inválido")
    void getById_bad() throws Exception {
        mockMvc.perform(get("/api/habit-types/habit-type/{id}", -1))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/habit-types/habit-type/{id} -> 200 encontrado")
    void getById_ok() throws Exception {
        HabitTypeResponseDTO dto = new HabitTypeResponseDTO(2L, "Deporte");
        when(service.findHabitTypeById(2L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/habit-types/habit-type/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Deporte"));
    }

    @Test
    @DisplayName("GET /api/habit-types/habit-type/{id} -> 404 no encontrado")
    void getById_notFound() throws Exception {
        when(service.findHabitTypeById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/habit-types/habit-type/{id}", 3))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/habit-types/habit-type/name/{name} -> 400 name inválido")
    void getByName_bad() throws Exception {
        mockMvc.perform(get("/api/habit-types/habit-type/name/{name}", "   "))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/habit-types/habit-type/name/{name} -> 200 encontrado")
    void getByName_ok() throws Exception {
        HabitTypeResponseDTO dto = new HabitTypeResponseDTO(4L, "Cultura");
        when(service.findHabitTypeByName("Cultura")).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/habit-types/habit-type/name/{name}", "Cultura"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("Cultura"));
    }

    @Test
    @DisplayName("GET /api/habit-types/habit-type/name/{name} -> 404 no encontrado")
    void getByName_notFound() throws Exception {
        when(service.findHabitTypeByName("Nada")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/habit-types/habit-type/name/{name}", "Nada"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/habit-types/habit-type -> 400 DTO nulo")
    void create_bad() throws Exception {
        mockMvc.perform(post("/api/habit-types/habit-type")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/habit-types/habit-type -> 201 creado")
    void create_ok() throws Exception {
        HabitTypeRequestDTO req = new HabitTypeRequestDTO("Salud", null);
        HabitTypeResponseDTO res = new HabitTypeResponseDTO(10L, "Salud");
        when(service.createHabitType(any())).thenReturn(res);

        mockMvc.perform(post("/api/habit-types/habit-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Salud"));
    }

    @Test
    @DisplayName("PUT /api/habit-types/habit-type/{id} -> 400 inválido")
    void update_bad() throws Exception {
        HabitTypeRequestDTO req = new HabitTypeRequestDTO("X", null);
        mockMvc.perform(put("/api/habit-types/habit-type/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/habit-types/habit-type/{id} -> 404 no existe")
    void update_notFound() throws Exception {
        HabitTypeRequestDTO req = new HabitTypeRequestDTO("X", null);
        when(service.updateHabitType(eq(5L), any()))
                .thenThrow(new RuntimeException("no existe"));

        mockMvc.perform(put("/api/habit-types/habit-type/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/habit-types/habit-type/{id} -> 200 actualizado")
    void update_ok() throws Exception {
        HabitTypeRequestDTO req = new HabitTypeRequestDTO("Bienestar", null);
        HabitTypeResponseDTO res = new HabitTypeResponseDTO(5L, "Bienestar");
        when(service.updateHabitType(eq(5L), any())).thenReturn(res);

        mockMvc.perform(put("/api/habit-types/habit-type/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Bienestar"));
    }

    @Test
    @DisplayName("DELETE /api/habit-types/habit-type/{id} -> 400 inválido")
    void delete_bad() throws Exception {
        mockMvc.perform(delete("/api/habit-types/habit-type/{id}", -3))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/habit-types/habit-type/{id} -> 404 no existe")
    void delete_notFound() throws Exception {
        doThrow(new RuntimeException()).when(service).deleteHabitType(8L);

        mockMvc.perform(delete("/api/habit-types/habit-type/{id}", 8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/habit-types/habit-type/{id} -> 204 eliminado")
    void delete_ok() throws Exception {
        mockMvc.perform(delete("/api/habit-types/habit-type/{id}", 9))
                .andExpect(status().isNoContent());
    }

}