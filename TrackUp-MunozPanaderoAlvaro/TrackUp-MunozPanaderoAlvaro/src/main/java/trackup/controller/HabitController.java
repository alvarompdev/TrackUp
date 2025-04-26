package trackup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trackup.dto.request.HabitRequestDTO;
import trackup.dto.response.HabitResponseDTO;
import trackup.entity.Habit;
import trackup.services.HabitService;
import trackup.services.UserService;
import trackup.services.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

@RestController
// @RequestMapping("/api")
@RequestMapping("/habit")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping("/habits")
    public ResponseEntity<List<HabitResponseDTO>> getAllHabits() {
        List<HabitResponseDTO> habitsList = habitService.getAllHabits();
        if (habitsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(habitsList);
    }

    @GetMapping("/habit/id/{id}")
    public ResponseEntity<HabitResponseDTO> getHabitById(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }

        // Alternativa más corta, posiblemente cambie a esta para reducir las líneas de código ya que es un simple checkeo
        // if (id < 0) return ResponseEntity.badRequest().build();

        Optional<HabitResponseDTO> habitOpt = habitService.getHabitById(id);
        return habitOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/habit/name/{name}")
    public ResponseEntity<Habit> getHabitByName(@PathVariable String name) {
        if (name.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Habit> habitOpt = habitService.getHabitByName(name);
        return habitOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/habits/user/{userId}")
    public ResponseEntity<List<HabitResponseDTO>> getHabitsByUserId(@PathVariable Long userId) {
        if (userId < 0) {
            return ResponseEntity.badRequest().build();
        }

        List<HabitResponseDTO> habitsList = habitService.getAllHabitsByUserId(userId);
        if (habitsList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(habitsList);
    }

    @PostMapping("/habit")
    public ResponseEntity<HabitResponseDTO> createHabit(@RequestBody HabitRequestDTO habitRequestDTO) {
        if (habitRequestDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        HabitResponseDTO createdHabit = habitService.createHabit(habitRequestDTO);
        return ResponseEntity.status(201).body(createdHabit);
    }

    @PutMapping("/habit/{id}")
    public ResponseEntity<HabitResponseDTO> updateHabit(@PathVariable Long id, @RequestBody HabitRequestDTO habitRequestDTO) {
        if (id < 0 || habitRequestDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            HabitResponseDTO updatedHabit = habitService.updateHabit(id, habitRequestDTO);
            return ResponseEntity.ok(updatedHabit);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/habit/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            habitService.deleteHabit(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}