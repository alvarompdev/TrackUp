package trackup.dto.request;

import java.time.LocalDate;

/**
 * DTO para los cuerpos de las solicitudes (Hábitos)
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class HabitRequestDTO {

    private String name; // Nombre del hábito
    private String description; // Descripción del hábito
    private String frequency; // Frecuencia del hábito
    private LocalDate startDate; // Fecha de inicio del hábito
    private LocalDate endDate; // Fecha de fin del hábito
    private Long userId; // ID del usuario al que pertenece el hábito
    private Long habitTypeId; // ID del tipo de hábito

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

    /**
     * Getters y Setters
     */
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHabitTypeId() {
        return habitTypeId;
    }

    public void setHabitTypeId(Long habitTypeId) {
        this.habitTypeId = habitTypeId;
    }

}