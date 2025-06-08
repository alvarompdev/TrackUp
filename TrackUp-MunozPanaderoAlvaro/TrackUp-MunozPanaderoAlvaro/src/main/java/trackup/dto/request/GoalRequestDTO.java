package trackup.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para los cuerpos de las solicitudes (Objetivos)
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class GoalRequestDTO {

    @NotBlank(message = "El nombre de la meta es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name; // Nombre del objetivo
    private String description; // Descripción del objetivo
    private Long userId; // ID del usuario al que pertenece el objetivo

    /**
     * Constructor vacío del DTO
     */
    public GoalRequestDTO() {
    }

    /**
     * Constructor con parámetros del DTO
     *
     * @param name        Nombre del objetivo
     * @param description Descripción del objetivo
     * @param userId      ID del usuario al que pertenece el objetivo
     */
    public GoalRequestDTO(String name, String description, Long userId) {
        this.name = name;
        this.description = description;
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}