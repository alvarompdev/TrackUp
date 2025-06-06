package trackup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO para los cuerpos de las solicitudes (Hábitos)
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Schema(name = "HabitRequest", description = "DTO para creación/actualización de hábitos")
public class HabitRequestDTO {

    private Long id;

    private String habitTypeName;

    @Schema(description = "Nombre del hábito", example = "Meditación matutina", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del hábito es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;

    @Schema(description = "Descripción detallada", example = "15 minutos de meditación guiada")
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String description;

    @Schema(description = "Frecuencia de ejecución", example = "Diario", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La frecuencia es obligatoria")
    @Size(max = 50, message = "La frecuencia no puede superar los 50 caracteres")
    private String frequency;

    @Schema(description = "Fecha de inicio (formato ISO: YYYY-MM-dd)", example = "2024-01-01", requiredMode = Schema.RequiredMode.REQUIRED, format = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser anterior a hoy")
    private LocalDate startDate;

    @Schema(description = "Fecha de finalización (formato ISO: YYYY-MM-dd)", example = "2024-12-31", format = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    // ****** IMPORTANTE: He quitado @NotNull y @Future para que endDate sea opcional
    // ****** y la validación personalizada de fechas tenga prioridad.
    private LocalDate endDate;

    @Schema(description = "ID del usuario propietario", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @Schema(description = "ID del tipo de hábito asociado", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El tipo de hábito es obligatorio")
    private Long habitTypeId;

    /**
     * Constructor vacío del DTO
     */
    public HabitRequestDTO() {
    }

    /**
     * Constructor con parámetros del DTO
     *
     * @param name Nombre del hábito
     * @param description Descripción del hábito
     * @param frequency Frecuencia del hábito
     * @param startDate Fecha de inicio del hábito
     * @param endDate Fecha de fin del hábito
     * @param userId ID del usuario al que pertenece el hábito
     * @param habitTypeId ID del tipo de hábito
     */
    public HabitRequestDTO(String name, String description, String frequency, LocalDate startDate, LocalDate endDate, Long userId, Long habitTypeId) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.habitTypeId = habitTypeId;
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getHabitTypeName() { return habitTypeName; }
    public void setHabitTypeName(String habitTypeName) { this.habitTypeName = habitTypeName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getHabitTypeId() { return habitTypeId; }
    public void setHabitTypeId(Long habitTypeId) { this.habitTypeId = habitTypeId; }

    // --- MÉTODO DE VALIDACIÓN DE FECHAS PERSONALIZADO ---
    // Este método es CRUCIAL para la validación de que endDate no sea anterior a startDate.
    @AssertTrue(message = "La fecha de fin no puede ser anterior a la fecha de inicio.")
    public boolean isEndDateAfterStartDate() {
        // Si endDate es nula, significa que el hábito no tiene fecha de fin, lo cual es válido.
        // Si startDate es nula (aunque ya tenemos @NotNull, esta validación de contingencia es segura),
        // también consideramos que no hay problema en esta validación cruzada.
        if (startDate == null || endDate == null) {
            return true;
        }
        // Retorna true si endDate es igual o posterior a startDate.
        // Si endDate es anterior a startDate, retorna false, disparando el mensaje de error.
        return !endDate.isBefore(startDate);
    }

}