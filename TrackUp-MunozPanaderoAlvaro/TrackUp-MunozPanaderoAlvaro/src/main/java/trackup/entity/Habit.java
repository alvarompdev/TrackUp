package trackup.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Entity
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false, length = 50)
    private String frequency;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "habit_type_id", nullable = false)
    private HabitType habitType;

    @OneToMany(mappedBy = "habit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyRecord> dailyRecords = new ArrayList<>();

    public Habit() {
    }

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