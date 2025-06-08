package trackup.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad 'HabitType' que representa los tipos de hábitos
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Entity // Se indica que se trata de una entidad
public class HabitType {

    @Id // ID, clave primaria del tipo de hábito
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Se indica que esta clave primaria será generada automáticamente
    private Long id; // Identificador que tiene cada tipo de hábito

    @Column(unique = true, nullable = false, length = 50) // Campo único y obligatorio, con una longitud máxima de 50 caracteres
    private String name; // Nombre del tipo de hábito

    @OneToMany(mappedBy = "habitType", cascade = CascadeType.ALL, orphanRemoval = true) // Relación de uno a muchos; se eliminan automáticamente los hábitos si se elimina el tipo de hábito
    private List<Habit> habits = new ArrayList<>(); // Lista de hábitos del tipo de hábito

    /**
     * Constructor vacío de la entidad
     */
    public HabitType() {
    }

    /**
     * Constructor con parámetros de la entidad
     *
     * @param id ID del tipo de hábito
     * @param name Nombre del tipo de hábito
     * @param habits Lista de hábitos del tipo de hábito
     */
    public HabitType(Long id, String name, List<Habit> habits) {
        this.id = id;
        this.name = name;
        this.habits = habits;
    }

    /**
     * Getters y setters de la entidad
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

    public List<Habit> getHabits() {
        return habits;
    }

    public void setHabits(List<Habit> habits) {
        this.habits = habits;
    }

}