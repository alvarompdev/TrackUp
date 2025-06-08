package trackup.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * Clase que representa la solicitud de autenticación.
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class AuthRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    public AuthRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}