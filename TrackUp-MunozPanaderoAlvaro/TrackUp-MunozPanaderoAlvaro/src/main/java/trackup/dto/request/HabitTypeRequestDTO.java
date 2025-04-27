package trackup.dto.request;

/**
 * DTO para la creación de un nuevo tipo de hábito
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class HabitTypeRequestDTO {

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