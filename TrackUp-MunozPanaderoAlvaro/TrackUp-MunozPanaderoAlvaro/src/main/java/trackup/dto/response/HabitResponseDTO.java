package trackup.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import trackup.entity.Habit;
import java.time.LocalDate;

/**
 * DTO para las respuestas de las solicitudes (Hábitos)
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class HabitResponseDTO {

    private Long id; // ID del hábito
    private String name; // Nombre del hábito
    private String description; // Descripción del hábito
    private String frequency; // Frecuencia del hábito
    private LocalDate startDate; // Fecha de inicio del hábito
    private LocalDate endDate; // Fecha de fin del hábito
    private String habitTypeName; // Nombre del tipo de hábito

    @JsonInclude(JsonInclude.Include.NON_NULL) // Excluir el campo si es null
    private Long userId; // ID del usuario al que pertenece el hábito

    /**
     * Constructor vacío del DTO
     */
    public HabitResponseDTO() {
    }

    /**
     * Constructor con parámetros del DTO
     *
     * @param id ID del hábito
     * @param name Nombre del hábito
     * @param description Descripción del hábito
     * @param frequency Frecuencia del hábito
     * @param startDate Fecha de inicio del hábito
     * @param endDate Fecha de fin del hábito
     * @param habitTypeName Nombre del tipo de hábito
     * @param userId ID del usuario al que pertenece el hábito
     */
    public HabitResponseDTO(Long id, String name, String description, String frequency, LocalDate startDate, LocalDate endDate, String habitTypeName, Long userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.habitTypeName = habitTypeName;
        this.userId = userId;
    }

    /**
     * Constructor con parámetros del DTO a partir de una entidad Habit
     *
     * @param habit Entidad Habit
     */
    public HabitResponseDTO(Habit habit) {
        this.id = habit.getId();
        this.name = habit.getName();
        this.description = habit.getDescription();
        this.frequency = habit.getFrequency();
        this.startDate = habit.getStartDate();
        this.endDate = habit.getEndDate();
        this.habitTypeName = habit.getHabitType() != null ? habit.getHabitType().getName() : null;
        this.userId = habit.getUser() != null ? habit.getUser().getId() : null;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getHabitTypeName() {
        return habitTypeName;
    }

    public void setHabitTypeName(String habitTypeName) {
        this.habitTypeName = habitTypeName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}