package trackup.dto.response;

/**
 * DTO para la respuesta de un usuario
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class UserResponseDTO {

    private Long id; // ID del usuario
    private String username; // Nombre del usuario
    private String email; // Email del usuario

    /**
     * Constructor vacío del DTO
     */
    public UserResponseDTO() {
    }

    /**
     * Constructor con parámetros del DTO
     *
     * @param id ID del usuario
     * @param username Nombre del usuario
     * @param email Email del usuario
     */
    public UserResponseDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}