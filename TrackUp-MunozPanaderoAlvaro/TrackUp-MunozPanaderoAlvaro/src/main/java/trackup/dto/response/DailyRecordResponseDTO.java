package trackup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * DTO para las respuestas de las solicitudes (Registros Diarios)
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Schema(name = "DailyRecordResponse", description = "DTO con información de un registro diario de hábito")
public class DailyRecordResponseDTO {

    @Schema(description = "ID único del registro diario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id; // ID del registro diario

    @Schema(description = "Fecha del registro en formato ISO (yyyy-MM-dd)", example = "2024-01-15", format = "date")
    private LocalDate date; // Fecha del registro diario

    @Schema(description = "Estado de completado del hábito", example = "true")
    private Boolean completed; // Indica si el hábito se ha completado o no

    @Schema(description = "ID del hábito asociado", example = "5")
    private Long habitId; // ID del hábito al que pertenece el registro diario

    private Long userId;
    private String habitName; // Nombre del hábito asociado

    /**
     * Constructor vacío del DTO
     */
    public DailyRecordResponseDTO() {
    }

    /**
     * Constructor con parámetros del DTO
     *
     * @param id        ID del registro diario
     * @param date      Fecha del registro diario
     * @param completed Indica si el hábito se ha completado o no
     */
    public DailyRecordResponseDTO(Long id, LocalDate date, Boolean completed, Long habitId, Long userId, String habitName) {
        this.id = id;
        this.date = date;
        this.completed = completed;
        this.habitId = habitId;
        this.userId = userId;
        this.habitName = habitName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    /**
     * Getters y Setters
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

}