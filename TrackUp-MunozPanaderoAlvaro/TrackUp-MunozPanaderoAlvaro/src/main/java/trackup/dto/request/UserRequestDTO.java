package trackup.dto.request;

/**
 * DTO para los cuerpos de las solicitudes (Usuarios)
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class UserRequestDTO {

    private String username; // Nombre de usuario
    private String email; // Email del usuario
    private String password; // Contraseña del usuario

    /**
     * Constructor vacío del DTO
     */
    public UserRequestDTO() {
    }

    /**
     * Constructor con parámetros del DTO
     *
     * @param username // Nombre de usuario
     * @param email // Email del usuario
     * @param password // Contraseña del usuario
     */
    public UserRequestDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Getters y Setters
     */
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}