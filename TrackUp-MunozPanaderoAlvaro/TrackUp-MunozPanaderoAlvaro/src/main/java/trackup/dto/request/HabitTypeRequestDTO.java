package trackup.dto.request;

public class HabitTypeRequestDTO {
    private String name;

    public HabitTypeRequestDTO() {
    }

    public HabitTypeRequestDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}