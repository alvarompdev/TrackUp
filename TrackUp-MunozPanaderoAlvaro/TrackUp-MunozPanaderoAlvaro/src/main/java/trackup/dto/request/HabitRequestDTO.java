package trackup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Nombre del hábito", example = "Meditación matutina", requiredMode = Schema.RequiredMode.REQUIRED) // añadir minLength = 3 cuando determine eso en la BBDD
    private String name; // Nombre del hábito

    @Schema(description = "Descripción detallada", example = "15 minutos de meditación guiada")
    private String description; // Descripción del hábito

    @Schema(description = "Frecuencia de ejecución", example = "Diario", requiredMode = Schema.RequiredMode.REQUIRED)
    private String frequency; // Frecuencia del hábito

    @Schema(description = "Fecha de inicio (formato ISO: yyyy-MM-dd)", example = "2024-01-01", requiredMode = Schema.RequiredMode.REQUIRED, format = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate; // Fecha de inicio del hábito

    @Schema(description = "Fecha de finalización (formato ISO: yyyy-MM-dd)", example = "2024-12-31", format = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate; // Fecha de fin del hábito

    @Schema(description = "ID del usuario propietario", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId; // ID del usuario al que pertenece el hábito

    @Schema(description = "ID del tipo de hábito asociado", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHabitTypeName() {
        return habitTypeName;
    }

    public void setHabitTypeName(String habitTypeName) {
        this.habitTypeName = habitTypeName;
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