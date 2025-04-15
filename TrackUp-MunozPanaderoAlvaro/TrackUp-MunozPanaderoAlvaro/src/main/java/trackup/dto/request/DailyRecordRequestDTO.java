package trackup.dto.request;

import java.time.LocalDate;

public class DailyRecordRequestDTO {
    private LocalDate date;
    private Boolean completed;
    private Long habitId;

    public DailyRecordRequestDTO() {
    }

    public DailyRecordRequestDTO(LocalDate date, Boolean completed, Long habitId) {
        this.date = date;
        this.completed = completed;
        this.habitId = habitId;
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

    public Long getHabitId() {
        return habitId;
    }

    public void setHabitId(Long habitId) {
        this.habitId = habitId;
    }

}