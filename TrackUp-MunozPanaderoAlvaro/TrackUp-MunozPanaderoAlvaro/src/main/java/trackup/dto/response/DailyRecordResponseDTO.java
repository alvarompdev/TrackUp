package trackup.dto.response;

import java.time.LocalDate;

public class DailyRecordResponseDTO {
    private Long id;
    private LocalDate date;
    private Boolean completed;

    public DailyRecordResponseDTO() {
    }

    public DailyRecordResponseDTO(Long id, LocalDate date, Boolean completed) {
        this.id = id;
        this.date = date;
        this.completed = completed;
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

}