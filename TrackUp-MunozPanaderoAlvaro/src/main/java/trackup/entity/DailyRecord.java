package trackup.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidad 'DailyRecord' que representa los registros diarios de los hábitos
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Entity // Se indica que se trata de una entidad
public class DailyRecord {

    @Id // ID, clave primaria del registro diario
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Se indica que esta clave primaria será generada automáticamente
    private Long id; // Identificador que tiene cada registro diario

    @Column(nullable = false) // Campo obligatorio
    private LocalDate date; // Fecha del registro diario

    @Column(nullable = false) // Campo obligatorio
    private Boolean completed; // Indica si el hábito se ha completado o no

    @ManyToOne(optional = false) // Relación de muchos a uno, un registro diario pertenece a un hábito
    @JoinColumn(name = "habit_id", nullable = false) // Se indica la columna de unión y que no puede ser nula
    private Habit habit; // Hábito al que pertenece el registro diario

    /**
     * Constructor vacío de la entidad
     */
    public DailyRecord() {
    }

    /**
     * Constructor con parámetros de la entidad
     *
     * @param id ID del registro diario
     * @param date Fecha del registro diario
     * @param completed Indica si el hábito se ha completado o no
     * @param habit Hábito al que pertenece el registro diario
     */
    public DailyRecord(Long id, LocalDate date, Boolean completed, Habit habit) {
        this.id = id;
        this.date = date;
        this.completed = completed;
        this.habit = habit;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

}