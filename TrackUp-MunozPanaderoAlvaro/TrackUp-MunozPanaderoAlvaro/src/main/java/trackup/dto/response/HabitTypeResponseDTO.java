package trackup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para las respuestas de las solicitudes (Tipos de Hábitos)
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Schema(name = "HabitTypeResponse", description = "DTO con información básica de un tipo de hábito")
public class HabitTypeResponseDTO {

    @Schema(description = "ID único del tipo de hábito", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id; // ID del tipo de hábito

    @Schema(description = "Nombre del tipo de hábito", example = "Salud")
    private String name; // Nombre del tipo de hábito

    /**
     * Constructor vacío del DTO
     */
    public HabitTypeResponseDTO() {
    }

    /**
     * Constructor con parámetros del DTO
     *
     * @param id ID del tipo de hábito
     * @param name Nombre del tipo de hábito
     */
    public HabitTypeResponseDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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

}