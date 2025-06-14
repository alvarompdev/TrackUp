/*package trackup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import trackup.dto.request.DailyRecordRequestDTO;
import trackup.dto.response.DailyRecordResponseDTO;
import trackup.services.DailyRecordService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DailyRecordControllerTest {

    @InjectMocks
    private trackup.controller.DailyRecordController controller;

    @Mock
    private DailyRecordService service;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /api/daily-records/daily-records -> 200 + list")
    void getAll_ok() throws Exception {
        DailyRecordResponseDTO dto = new DailyRecordResponseDTO(
                1L,
                LocalDate.of(2025,5,1),
                true,
                10L,
                1L // Assuming userId is also part of the DTO
        );
        when(service.getAllDailyRecords()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/daily-records/daily-records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].date").value("2025-05-01"))
                .andExpect(jsonPath("$[0].completed").value(true))
                .andExpect(jsonPath("$[0].habitId").value(10));
    }

    @Test
    @DisplayName("GET /api/daily-records/daily-records -> 204 empty")
    void getAll_noContent() throws Exception {
        when(service.getAllDailyRecords()).thenReturn(List.of());

        mockMvc.perform(get("/api/daily-records/daily-records"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/daily-records/daily-record/{id} -> 400 bad id")
    void getById_bad() throws Exception {
        mockMvc.perform(get("/api/daily-records/daily-record/{id}", -1))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/daily-records/daily-record/{id} -> 200 found")
    void getById_ok() throws Exception {
        DailyRecordResponseDTO dto = new DailyRecordResponseDTO(
                2L,
                LocalDate.of(2025,6,6),
                false,
                11L,
                1L // Assuming userId is also part of the DTO
        );
        when(service.findDailyRecordById(2L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/daily-records/daily-record/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2025-06-06"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.habitId").value(11));
    }

    @Test
    @DisplayName("GET /api/daily-records/daily-record/{id} -> 404 not found")
    void getById_notFound() throws Exception {
        when(service.findDailyRecordById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/daily-records/daily-record/{id}", 3))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/daily-records/daily-record/date/{date} -> 400 bad date")
    void getByDate_bad() throws Exception {
        mockMvc.perform(get("/api/daily-records/daily-record/date/{date}", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/daily-records/daily-record/date/{date} -> 200 found")
    void getByDate_ok() throws Exception {
        DailyRecordResponseDTO dto = new DailyRecordResponseDTO(
                4L,
                LocalDate.of(2025,7,7),
                true,
                12L,
                1L // Assuming userId is also part of the DTO
        );
        when(service.findDailyRecordByDate(LocalDate.of(2025,7,7)))
                .thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/daily-records/daily-record/date/{date}", "2025-07-07"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.habitId").value(12));
    }

    @Test
    @DisplayName("GET /api/daily-records/daily-record/completed/{completed} -> 200 found")
    void getByCompleted_ok() throws Exception {
        DailyRecordResponseDTO dto = new DailyRecordResponseDTO(
                5L,
                LocalDate.of(2025,8,8),
                false,
                13L,
                1L // Assuming userId is also part of the DTO
        );
        when(service.findDailyRecordByCompleted(false))
                .thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/daily-records/daily-record/completed/{completed}", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    @DisplayName("GET /api/daily-records/daily-record/completed/{completed} -> 404 none")
    void getByCompleted_notFound() throws Exception {
        when(service.findDailyRecordByCompleted(true))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/daily-records/daily-record/completed/{completed}", "true"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/daily-records/daily-record -> 400 bad body")
    void create_bad() throws Exception {
        mockMvc.perform(post("/api/daily-records/daily-record")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/daily-records/daily-record -> 200 created")
    void create_ok() throws Exception {
        DailyRecordRequestDTO req = new DailyRecordRequestDTO(
                LocalDate.of(2025,9,9),
                true,
                14L,
                1L // Assuming userId is also part of the DTO
        );
        DailyRecordResponseDTO res = new DailyRecordResponseDTO(
                6L,
                LocalDate.of(2025,9,9),
                true,
                14L,
                1L // Assuming userId is also part of the DTO
        );
        when(service.createDailyRecord(any())).thenReturn(res);

        mockMvc.perform(post("/api/daily-records/daily-record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.habitId").value(14));
    }

    @Test
    @DisplayName("PUT /api/daily-records/daily-record/{id} -> 400 bad")
    void update_bad() throws Exception {
        DailyRecordRequestDTO req = new DailyRecordRequestDTO(
                LocalDate.now(),
                false,
                15L,
                1L // Assuming userId is also part of the DTO
        );
        mockMvc.perform(put("/api/daily-records/daily-record/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/daily-records/daily-record/{id} -> 200 updated")
    void update_ok() throws Exception {
        DailyRecordRequestDTO req = new DailyRecordRequestDTO(
                LocalDate.of(2025,10,10),
                false,
                16L,
                1L // Assuming userId is also part of the DTO
        );
        DailyRecordResponseDTO res = new DailyRecordResponseDTO(
                7L,
                LocalDate.of(2025,10,10),
                false,
                16L,
                1L // Assuming userId is also part of the DTO
        );
        when(service.updateDailyRecord(eq(7L), any())).thenReturn(res);

        mockMvc.perform(put("/api/daily-records/daily-record/{id}", 7)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.date").value("2025-10-10"));
    }

    @Test
    @DisplayName("DELETE /api/daily-records/daily-record/{id} -> 400 bad")
    void delete_bad() throws Exception {
        mockMvc.perform(delete("/api/daily-records/daily-record/{id}", -2))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/daily-records/daily-record/{id} -> 204 deleted")
    void delete_ok() throws Exception {
        mockMvc.perform(delete("/api/daily-records/daily-record/{id}", 8))
                .andExpect(status().isNoContent());
    }

}
*/