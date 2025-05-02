package trackup.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad 'Habit' que representa los hábitos de los usuarios
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Entity // Se indica que se trata de una entidad
/*@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "name"})
        }
)*/
public class Habit {

    @Id // ID, clave primaria del hábito
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Se indica que esta clave primaria será generada automáticamente
    private Long id; // Identificador que tiene cada hábito

    @Column(nullable = false) // Campo obligatorio
    private String name; // Nombre del hábito

    private String description; // Descripoión del hábito, opcional

    @Column(nullable = false, length = 50) // Campo obligatorio y con una longitud máxima de 50 caracteres
    private String frequency; // Frequencia del hábito

    @Column(nullable = false) // Campo obligatorio
    private LocalDate startDate; // Fecha de inicio del hábito

    @Column(nullable = false) // Campo obligatorio
    private LocalDate endDate; // Fecha de fin del hábito

    @JsonBackReference  // Evita la referencia circular desde el Habit hacia el User
    @ManyToOne(optional = false) //Campo obligatorio
    @JoinColumn(name = "user_id", nullable = false) // Relación de muchos hábitos a un usuario, obligatorio
    private User user; // Usuario al que pertenece el hábito

    //@ManyToOne(optional = false) // Relación de muchos hábitos a un usuario, obligatorio
    @ManyToOne(optional = true)
    @JoinColumn(name = "habit_type_id", nullable = false)
    private HabitType habitType; // Tipo de hábito

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true) // Relación de uno a muchos; se eliminan automáticamente los registros diarios si se elimina el hábito
    private List<DailyRecord> dailyRecords = new ArrayList<>(); // Lista de registros diarios del hábito

    /**
     * Constructor vacío de la entidad
     */
    public Habit() {
    }

    /**
     * Constructor con parámetros de la entidad
     *
     * @param id ID del hábito
     * @param name Nombre del hábito
     * @param description Descripción del hábito
     * @param frequency Frecuencia del hábito
     * @param startDate Fecha de inicio del hábito
     * @param endDate Fecha de fin del hábito
     * @param user Usuario al que pertenece el hábito
     * @param habitType Tipo de hábito
     * @param dailyRecords Lista de registros diarios del hábito
     */
    public Habit(Long id, String name, String description, String frequency, LocalDate startDate, LocalDate endDate, User user, HabitType habitType, List<DailyRecord> dailyRecords) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.habitType = habitType;
        this.dailyRecords = dailyRecords;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HabitType getHabitType() {
        return habitType;
    }

    public void setHabitType(HabitType habitType) {
        this.habitType = habitType;
    }

    public List<DailyRecord> getDailyRecords() {
        return dailyRecords;
    }

    public void setDailyRecords(List<DailyRecord> dailyRecords) {
        this.dailyRecords = dailyRecords;
    }

}