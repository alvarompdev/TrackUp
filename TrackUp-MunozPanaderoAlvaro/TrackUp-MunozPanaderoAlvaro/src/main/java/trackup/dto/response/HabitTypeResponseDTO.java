package trackup.dto.response;

/**
 * DTO para la respuesta de un usuario
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class HabitTypeResponseDTO {

    private Long id; // ID del tipo de hábito
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