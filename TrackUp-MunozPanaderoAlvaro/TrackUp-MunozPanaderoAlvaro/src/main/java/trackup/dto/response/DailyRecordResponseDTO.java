package trackup.dto.response;

import java.time.LocalDate;

/**
 * DTO para las respuestas de las solicitudes (Registros Diarios)
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class DailyRecordResponseDTO {

    private Long id; // ID del registro diario
    private LocalDate date; // Fecha del registro diario
    private Boolean completed; // Indica si el hábito se ha completado o no
    private Long habitId; // ID del hábito al que pertenece el registro diario

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
    public DailyRecordResponseDTO(Long id, LocalDate date, Boolean completed, Long habitId) {
        this.id = id;
        this.date = date;
        this.completed = completed;
        this.habitId = habitId;
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