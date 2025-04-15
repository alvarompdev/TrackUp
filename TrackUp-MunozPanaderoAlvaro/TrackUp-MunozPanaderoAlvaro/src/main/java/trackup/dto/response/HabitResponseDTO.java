package trackup.dto.response;

import trackup.entity.Habit;

import java.time.LocalDate;

public class HabitResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private String habitTypeName;

    public HabitResponseDTO() {
    }

    public HabitResponseDTO(Long id, String name, String description, String frequency, LocalDate startDate, LocalDate endDate, String habitTypeName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.habitTypeName = habitTypeName;
    }

    public HabitResponseDTO(Habit habit) {
        this.id = habit.getId();
        this.name = habit.getName();
        this.description = habit.getDescription();
        this.frequency = habit.getFrequency();
        this.startDate = habit.getStartDate();
        this.endDate = habit.getEndDate();
        this.habitTypeName = habit.getHabitType() != null ? habit.getHabitType().getName() : null;
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

    public String getHabitTypeName() {
        return habitTypeName;
    }

    public void setHabitTypeName(String habitTypeName) {
        this.habitTypeName = habitTypeName;
    }

}