package trackup.dto.response;

import trackup.entity.Goal;

/**
 * DTO para la respuesta de un objetivo
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class GoalResponseDTO {

    private Long id; // ID del objetivo
    private String name; // Nombre del objetivo
    private String description; // Descripción del objetivo

    /**
     * Constructor vacío del DTO
     */
    public GoalResponseDTO() {
    }

    /**
     * Constructor con parámetros del DTO
     *
     * @param id ID del objetivo
     * @param name Nombre del objetivo
     * @param description Descripción del objetivo
     */
    public GoalResponseDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Constructor que recibe un objeto Goal y lo convierte a DTO
     *
     * @param goal Objeto Goal a convertir
     */
    public GoalResponseDTO(Goal goal) {
        this.id = goal.getId();
        this.name = goal.getName();
        this.description = goal.getDescription();
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

}