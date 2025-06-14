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
import trackup.dto.request.GoalRequestDTO;
import trackup.dto.response.GoalResponseDTO;
import trackup.services.GoalService;
import trackup.entity.User;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GoalControllerTest {

    @InjectMocks
    private trackup.controller.GoalController controller;

    @Mock
    private GoalService service;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /api/goals/goals -> 200 + lista")
    void getAllGoals_ok() throws Exception {
        // Crear un User para asociar con el Goal
        User user = new User();  // Crear un User con un ID
        user.setId(1L);  // Asignar ID al usuario

        GoalResponseDTO dto = new GoalResponseDTO(
                1L,                          // ID del objetivo
                "No fumar",                   // nombre del objetivo
                "Dejar de fumar",             // descripción del objetivo
                user.getId()                  // ID del usuario asociado
        );

        when(service.getAllGoals()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/goals/goals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("No fumar"))
                .andExpect(jsonPath("$[0].userId").value(user.getId()));
    }

    @Test
    @DisplayName("GET /api/goals/goals -> 204 vacía")
    void getAllGoals_noContent() throws Exception {
        when(service.getAllGoals()).thenReturn(List.of());

        mockMvc.perform(get("/api/goals/goals"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/goals/goal/{id} -> 200 encontrado")
    void getGoalById_ok() throws Exception {
        // Crear un User
        User user = new User();
        user.setId(1L);

        GoalResponseDTO dto = new GoalResponseDTO(
                1L,
                "No fumar",
                "Dejar de fumar",
                user.getId()
        );

        when(service.findGoalById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/goals/goal/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("No fumar"))
                .andExpect(jsonPath("$.userId").value(user.getId()));
    }

    @Test
    @DisplayName("GET /api/goals/goal/{id} -> 404 no encontrado")
    void getGoalById_notFound() throws Exception {
        when(service.findGoalById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/goals/goal/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/goals/goal -> 400 DTO nulo")
    void createGoal_bad() throws Exception {
        mockMvc.perform(post("/api/goals/goal")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/goals/goal -> 201 creado")
    void createGoal_ok() throws Exception {
        // Crear un User válido
        User user = new User();
        user.setId(1L);  // Asignar un ID al usuario

        // Crear el DTO de solicitud
        GoalRequestDTO req = new GoalRequestDTO(
                "No fumar",                 // Nombre del objetivo
                "Dejar de fumar",           // Descripción del objetivo
                user.getId()                // ID del usuario
        );

        // Crear el DTO de respuesta
        GoalResponseDTO res = new GoalResponseDTO(
                2L,                          // ID del nuevo objetivo
                "No fumar",                   // Nombre del objetivo
                "Dejar de fumar",             // Descripción del objetivo
                user.getId()                  // ID del usuario
        );

        // Simular el comportamiento del servicio
        when(service.createGoal(any())).thenReturn(res);

        // Realizar la petición POST y verificar el resultado
        mockMvc.perform(post("/api/goals/goal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("No fumar"))
                .andExpect(jsonPath("$.userId").value(user.getId()));
    }

    @Test
    @DisplayName("PUT /api/goals/goal/{id} -> 200 actualizado")
    void updateGoal_ok() throws Exception {
        // Crear un User
        User user = new User();
        user.setId(1L);

        GoalRequestDTO req = new GoalRequestDTO(
                "Ejercicio",               // Nombre actualizado
                "Hacer ejercicio regularmente", // Descripción actualizada
                user.getId()                // ID del usuario
        );

        GoalResponseDTO res = new GoalResponseDTO(
                1L,                         // ID del objetivo
                "Ejercicio",                // Nombre actualizado
                "Hacer ejercicio regularmente", // Descripción actualizada
                user.getId()                 // ID del usuario
        );

        when(service.updateGoal(eq(1L), any())).thenReturn(res);

        mockMvc.perform(put("/api/goals/goal/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ejercicio"))
                .andExpect(jsonPath("$.description").value("Hacer ejercicio regularmente"))
                .andExpect(jsonPath("$.userId").value(user.getId()));
    }

    @Test
    @DisplayName("DELETE /api/goals/goal/{id} -> 204 eliminado")
    void deleteGoal_ok() throws Exception {
        mockMvc.perform(delete("/api/goals/goal/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/goals/goal/{id} -> 404 no encontrado")
    void deleteGoal_notFound() throws Exception {
        mockMvc.perform(delete("/api/goals/goal/{id}", 999))
                .andExpect(status().isNotFound());
    }

}