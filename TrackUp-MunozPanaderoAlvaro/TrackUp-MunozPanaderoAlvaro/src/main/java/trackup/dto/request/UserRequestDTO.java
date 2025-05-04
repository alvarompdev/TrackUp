package trackup.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para los cuerpos de las solicitudes (Usuarios)
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Schema(name = "UserRequest", description = "Datos requeridos para crear o actualizar un usuario. Incluye campos sensibles como la contraseña.")
public class UserRequestDTO {

    @Schema(description = "Nombre de usuario único (no espacios, mín. 4 caracteres)", example = "alvaromp", requiredMode = Schema.RequiredMode.REQUIRED, pattern = "^\\S+$") // añadir minLength = 4 cuando determine eso en la BBDD
    private String username; // Nombre de usuario

    @Schema(description = "Correo electrónico válido", example = "alvaro@trackup.com", requiredMode = Schema.RequiredMode.REQUIRED, format = "email")
    private String email; // Email del usuario

    @Schema(description = "Contraseña segura",example = "Passw0rd!", requiredMode = Schema.RequiredMode.REQUIRED, format = "password", pattern = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$") // añadir minLength = 8 cuando determine eso en la BBDD
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