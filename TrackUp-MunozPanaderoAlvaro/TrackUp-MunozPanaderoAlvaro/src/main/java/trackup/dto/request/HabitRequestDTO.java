package trackup.dto.request;

import java.time.LocalDate;

public class HabitRequestDTO {
    private String name;
    private String description;
    private String frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long userId;
    private Long habitTypeId;

    public HabitRequestDTO() {
    }

    public HabitRequestDTO(String name, String description, String frequency, LocalDate startDate, LocalDate endDate, Long userId, Long habitTypeId) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.habitTypeId = habitTypeId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getHabitTypeId() {
        return habitTypeId;
    }

    public void setHabitTypeId(Long habitTypeId) {
        this.habitTypeId = habitTypeId;
    }

}