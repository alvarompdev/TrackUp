package trackup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 * DTO para los cuerpos de las solicitudes (Registros Diarios)
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Schema(name = "DailyRecordRequest", description = "DTO para crear/actualizar registros diarios de hábitos")
public class DailyRecordRequestDTO {

    @Schema(description = "Fecha del registro en formato ISO (yyyy-MM-dd)", example = "2024-01-15", requiredMode = Schema.RequiredMode.REQUIRED, format = "date")
    private LocalDate date; // Fecha del registro diario

    @Schema(description = "Estado de completado del hábito", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean completed; // Indica si el hábito se ha completado o no

    @Schema(description = "ID del hábito asociado (debe existir)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long habitId; // ID del hábito al que pertenece el registro diario

    private Long userId;

    /**
     * Constructor vacío del DTO
     */
    public DailyRecordRequestDTO() {
    }

    /**
     * Constructor con parámetros del DTO
     *
     * @param date      Fecha del registro diario
     * @param completed Indica si el hábito se ha completado o no
     * @param habitId   ID del hábito al que pertenece el registro diario
     */
    public DailyRecordRequestDTO(LocalDate date, Boolean completed, Long habitId, Long userId) {
        this.date = date;
        this.completed = completed;
        this.habitId = habitId;
        this.userId = userId;
    }

    /**
     * Getters y Setters
     */
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(Long habitId) {
        this.habitId = habitId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}