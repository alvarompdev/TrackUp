package trackup.dto.request;

import java.time.LocalDate;

/**
 * DTO para los cuerpos de las solicitudes (Registros Diarios)
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class DailyRecordRequestDTO {

    private LocalDate date; // Fecha del registro diario
    private Boolean completed; // Indica si el hábito se ha completado o no
    private Long habitId; // ID del hábito al que pertenece el registro diario

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
    public DailyRecordRequestDTO(LocalDate date, Boolean completed, Long habitId) {
        this.date = date;
        this.completed = completed;
        this.habitId = habitId;
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

}