package trackup.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad 'User' que representa los usuarios
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Entity // Se indica que se trata de una entidad
@Table(name = "users") // La tabla no puede llamarse 'user' ya que es un nombre reservado
// @JsonIgnoreProperties({"habits"})  // Ignora la propiedad 'habits' cuando se serializa el objeto User
public class User {

    @Id // ID, clave primaria del usuario
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Se indica que esta clave primaria será generada automáticamente
    private Long id; // Identificador que tiene cada hábito

    @Column(unique = true, nullable = false) // Campo único y obligatorio
    private String username; // Nombre del usuario

    @Column(unique = true, nullable = false) // Campo único y obligatorio
    @Email // Validación, el email debe de tener un formato correcto
    private String email; // Email del usuario

    @Column(nullable = false) // Campo obligatorio
    private String password; // Contraseña del usuario

    @JsonManagedReference // Se utiliza para evitar la recursividad infinita en la serialización de JSON
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // Relación de uno a muchos, un usuario puede tener muchos hábitos; se eliminan automáticamente los hábitos diarios si se elimina el hábito
    private List<Habit> habits = new ArrayList<>(); // Lista de hábitos del usuario

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) // Relación de uno a muchos, un usuario puede tener muchos objetivos; se eliminan automáticamente las metas si se elimina el hábito
    private List<Goal> goals = new ArrayList<>(); // Lista de objetivos del usuario

    /**
     * Constructor vacío de la entidad
     */
    public User() {
    }

    /**
     * Constructor con parámetros de la entidad
     *
     * @param id ID del usuario
     * @param username Nombre del usuario
     * @param password Contraseña del usuario
     * @param email Email del usuario
     * @param habits Lista de hábitos del usuario
     * @param goals Lista de objetivos del usuario
     */
    public User(Long id, String username, String password, String email, List<Habit> habits, List<Goal> goals) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.habits = habits;
        this.goals = goals;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public List<Habit> getHabits() {
        return habits;
    }

    public void setHabits(List<Habit> habits) {
        this.habits = habits;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

}