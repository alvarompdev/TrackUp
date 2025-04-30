package trackup.entity;

import jakarta.persistence.*;

/**
 * Entidad 'Goal' que representa los objetivos de los usuarios
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Entity
public class Goal {

    @Id // ID, clave primaria del objetivo
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Se indica que esta clave primaria será generada automáticamente
    private Long id; // Identificador que tiene cada objetivo

    @Column(nullable = false, length = 100) // Campo obligatorio y con longitud máxima de 100 caracteres
    private String name; // Nombre del objetivo

    @Column(nullable = false) // Campo obligatorio
    private String description; // Descripción del objetivo

    @ManyToOne(optional = false) // Relación de muchos a uno, un objetivo pertenece a un usuario
    @JoinColumn(name = "user_id", nullable = false) // Se indica la columna de unión y que no puede ser nula
    private User user; // Usuario al que pertenece el objetivo

    /**
     * Constructor vacío de la entidad
     */
    public Goal() {
    }

    /**
     * Constructor con parámetros de la entidad
     *
     * @param id ID del objetivo
     * @param name Nombre del objetivo
     * @param description Descripción del objetivo
     * @param user Usuario al que pertenece el objetivo
     */
    public Goal(Long id, String name, String description, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
    }

    /**
     * Getters y Setters de la entidad
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}