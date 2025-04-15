package trackup.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 *
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Entity
public class DailyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Boolean completed;

    @ManyToOne(optional = false)
    @JoinColumn(name = "habit_id", nullable = false)
    private Habit habit;

    public DailyRecord() {
    }

    public DailyRecord(Long id, LocalDate date, Boolean completed, Habit habit) {
        this.id = id;
        this.date = date;
        this.completed = completed;
        this.habit = habit;
    }

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