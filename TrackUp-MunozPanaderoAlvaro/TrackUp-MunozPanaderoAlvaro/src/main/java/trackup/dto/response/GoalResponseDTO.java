package trackup.dto.response;

import trackup.entity.Goal;

public class GoalResponseDTO {
    private Long id;
    private String name;
    private String description;

    public GoalResponseDTO() {
    }

    public GoalResponseDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public GoalResponseDTO(Goal goal) {
        this.id = goal.getId();
        this.name = goal.getName();
        this.description = goal.getDescription();
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

}