package trackup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import trackup.dto.request.HabitRequestDTO;
import trackup.dto.response.HabitResponseDTO;
import trackup.services.HabitService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HabitControllerTest {

    @InjectMocks
    private trackup.controller.HabitController controller;

    @Mock
    private HabitService service;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /api/habits/habits -> 200 + lista")
    void getAll_ok() throws Exception {
        HabitResponseDTO dto = new HabitResponseDTO(
                1L,                          // id
                "Correr",                    // name
                "desc",                      // description
                "DIARIA",                    // frequency
                LocalDate.now(),             // startDate
                LocalDate.now().plusDays(1), // endDate
                "Salud",                     // habitTypeName (String)
                5L                           // userId (Long)
        );
        when(service.getAllHabits()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/habits/habits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].habitTypeName").value("Salud"))
                .andExpect(jsonPath("$[0].userId").value(5));
    }

    @Test
    @DisplayName("GET /api/habits/habits -> 204 vacía")
    void getAll_noContent() throws Exception {
        when(service.getAllHabits()).thenReturn(List.of());

        mockMvc.perform(get("/api/habits/habits"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/habits/habit/{id} -> 400 id inválido")
    void getById_bad() throws Exception {
        mockMvc.perform(get("/api/habits/habit/{id}", -1))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/habits/habit/{id} -> 200 encontrado")
    void getById_ok() throws Exception {
        HabitResponseDTO dto = new HabitResponseDTO(
                2L,
                "Yoga",
                null,
                "SEMANAL",
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                "Bienestar",
                5L
        );
        when(service.findHabitById(2L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/habits/habit/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.habitTypeName").value("Bienestar"))
                .andExpect(jsonPath("$.userId").value(5));
    }

    @Test
    @DisplayName("GET /api/habits/habit/{id} -> 404 no encontrado")
    void getById_notFound() throws Exception {
        when(service.findHabitById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/habits/habit/{id}", 3))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/habits/habit/by-name-and-user -> 200 encontrado")
    void getByNameAndUser_ok() throws Exception {
        HabitResponseDTO dto = new HabitResponseDTO(
                4L,
                "Leer",
                null,
                "DIARIA",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                "Cultura",
                7L
        );
        when(service.findHabitByNameAndUserId("Leer", 7L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/habits/habit/by-name-and-user")
                        .param("name", "Leer")
                        .param("userId", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.habitTypeName").value("Cultura"))
                .andExpect(jsonPath("$.userId").value(7));
    }

    @Test
    @DisplayName("GET /api/habits/habit/by-name-and-user -> 404 no encontrado")
    void getByNameAndUser_notFound() throws Exception {
        when(service.findHabitByNameAndUserId("Nada", 7L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/habits/habit/by-name-and-user")
                        .param("name", "Nada")
                        .param("userId", "7"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/habits/habits/user/{userId} -> 400 id inválido")
    void getByUser_bad() throws Exception {
        mockMvc.perform(get("/api/habits/habits/user/{userId}", -5))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/habits/habits/user/{userId} -> 200 con datos")
    void getByUser_ok() throws Exception {
        HabitResponseDTO dto = new HabitResponseDTO(
                5L,
                "Meditar",
                null,
                "DIARIA",
                LocalDate.now(),
                LocalDate.now().plusDays(3),
                "Mindfulness",
                8L
        );
        when(service.getAllHabitsByUserId(8L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/habits/habits/user/{userId}", 8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5))
                .andExpect(jsonPath("$[0].habitTypeName").value("Mindfulness"))
                .andExpect(jsonPath("$[0].userId").value(8));
    }

    @Test
    @DisplayName("GET /api/habits/habits/user/{userId} -> 204 vacía")
    void getByUser_noContent() throws Exception {
        when(service.getAllHabitsByUserId(9L)).thenReturn(List.of());

        mockMvc.perform(get("/api/habits/habits/user/{userId}", 9))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("POST /api/habits/habit -> 400 DTO nulo")
    void create_bad() throws Exception {
        mockMvc.perform(post("/api/habits/habit")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/habits/habit -> 201 creado")
    void create_ok() throws Exception {
        HabitRequestDTO req = new HabitRequestDTO(
                "Ejercicio",
                "desc",
                "DIARIA",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                10L,
                3L
        );
        HabitResponseDTO res = new HabitResponseDTO(
                20L,
                "Ejercicio",
                "desc",
                "DIARIA",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                "Deporte",
                10L
        );
        when(service.createHabit(any())).thenReturn(res);

        mockMvc.perform(post("/api/habits/habit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.habitTypeName").value("Deporte"))
                .andExpect(jsonPath("$.userId").value(10));
    }

    @Test
    @DisplayName("PUT /api/habits/habit/{id} -> 400 inválido")
    void update_bad() throws Exception {
        HabitRequestDTO req = new HabitRequestDTO(
                "X",
                null,
                "DIARIA",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                1L,
                1L
        );
        mockMvc.perform(put("/api/habits/habit/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/habits/habit/{id} -> 404 no existe")
    void update_notFound() throws Exception {
        HabitRequestDTO req = new HabitRequestDTO(
                "X",
                null,
                "DIARIA",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                1L,
                1L
        );
        when(service.updateHabit(eq(5L), any())).thenThrow(new RuntimeException("no existe"));

        mockMvc.perform(put("/api/habits/habit/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/habits/habit/{id} -> 200 actualizado")
    void update_ok() throws Exception {
        HabitRequestDTO req = new HabitRequestDTO(
                "X",
                "new desc",
                "DIARIA",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                2L,
                4L
        );
        HabitResponseDTO res = new HabitResponseDTO(
                5L,
                "X",
                "new desc",
                "DIARIA",
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                "Salud",
                2L
        );
        when(service.updateHabit(eq(5L), any())).thenReturn(res);

        mockMvc.perform(put("/api/habits/habit/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.habitTypeName").value("Salud"))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.description").value("new desc"));
    }

    @Test
    @DisplayName("DELETE /api/habits/habit/{id} -> 400 inválido")
    void delete_bad() throws Exception {
        mockMvc.perform(delete("/api/habits/habit/{id}", -3))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/habits/habit/{id} -> 404 no existe")
    void delete_notFound() throws Exception {
        doThrow(new RuntimeException()).when(service).deleteHabit(8L);

        mockMvc.perform(delete("/api/habits/habit/{id}", 8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/habits/habit/{id} -> 204 eliminado")
    void delete_ok() throws Exception {
        mockMvc.perform(delete("/api/habits/habit/{id}", 9))
                .andExpect(status().isNoContent());
    }

}