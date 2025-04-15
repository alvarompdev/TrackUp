package trackup.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Entity
@Table(name = "users") // la tabla no puede llamarse 'user' ya que es un nombre reservado
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Habit> habits = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Goal> goals = new ArrayList<>();

    public User() {
    }

    public User(Long id, String username, String password, String email, List<Habit> habits, List<Goal> goals) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.habits = habits;
        this.goals = goals;
    }

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

    public @Email String getEmail() {
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