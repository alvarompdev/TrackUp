package trackup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para los cuerpos de las solicitudes (Tipos de Hábitos)
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Schema(name = "HabitTypeRequest", description = "DTO para crear/actualizar tipos de hábitos")
public class HabitTypeRequestDTO {

    @Schema(description = "Nombre único del tipo de hábito", example = "Productividad", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name; // Nombre del tipo de hábito

    /**
     * Constructor vacío del DTO
     */
    public HabitTypeRequestDTO() {
    }

    /**
     * Constructor con parámetros del DTO
     *
     * @param name Nombre del tipo de hábito
     */
    public HabitTypeRequestDTO(String name) {
        this.name = name;
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

}