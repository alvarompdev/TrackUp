package trackup.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para las respuestas de las solicitudes (Usuarios)
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Schema(name = "UserResponse", description = "Respuesta estándar con datos de un usuario. Nunca incluye información sensible.")
public class UserResponseDTO {

    @Schema(description = "ID único del usuario en el sistema", example = "123", accessMode = Schema.AccessMode.READ_ONLY) // Solo se muestra en respuestas, no se acepta en requests
    private Long id; // ID del usuario

    @Schema(description = "Nombre de usuario único", example = "alvaromp", requiredMode = Schema.RequiredMode.REQUIRED) // añadir maxLength = 20 (por ejemplo) cuando determine eso en la BBDD
    private String username; // Nombre del usuario

    @Schema(description = "Correo electrónico válido", example = "alvaro@trackup.com", requiredMode = Schema.RequiredMode.REQUIRED, format = "email")
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