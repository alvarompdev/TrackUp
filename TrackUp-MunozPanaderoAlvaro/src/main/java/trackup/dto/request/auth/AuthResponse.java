package trackup.dto.request.auth;

/**
 * Clase que representa la respuesta de autenticación.
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class AuthResponse {

    private String token;

    public AuthResponse() {
    }

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}