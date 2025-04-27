package trackup.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trackup.dto.request.HabitTypeRequestDTO;
import trackup.dto.response.HabitTypeResponseDTO;
import trackup.services.HabitTypeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/habit_type")
public class HabitTypeController {

    private final HabitTypeService habitTypeService;

    public HabitTypeController(HabitTypeService habitTypeService) {
        this.habitTypeService = habitTypeService;
    }

    @GetMapping("habit_types")
    public ResponseEntity<List<HabitTypeResponseDTO>> getAllHabitTypes() {
        List<HabitTypeResponseDTO> habitTypesList = habitTypeService.getAllHabitTypes();
        if (habitTypesList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(habitTypesList);
    }

    @GetMapping("habit_type/id/{id}")
    public ResponseEntity<HabitTypeResponseDTO> findHabitTypeById(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }

        Optional<HabitTypeResponseDTO> habitTypeOpt = habitTypeService.getHabitTypeById(id);
        return habitTypeOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("Habit_type/name/{name}")
    public ResponseEntity<HabitTypeResponseDTO> findHabitTypeByName(@PathVariable String name) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<HabitTypeResponseDTO> habitTypeOpt = habitTypeService.getHabitTypeByName(name);
        return habitTypeOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/habit_type")
    public ResponseEntity<HabitTypeResponseDTO> createHabitType(@RequestBody HabitTypeRequestDTO habitTypeRequest) {
        if (habitTypeRequest == null) {
            return ResponseEntity.badRequest().build();
        }

        HabitTypeResponseDTO createdHabitType = habitTypeService.createHabitType(habitTypeRequest);
        return ResponseEntity.status(201).body(createdHabitType);
    }


    @PutMapping("/habit_type/{id}")
    public ResponseEntity<HabitTypeResponseDTO> updateHabitType(@PathVariable Long id, @RequestBody HabitTypeRequestDTO habitTypeRequest) {
        if (id < 0 || habitTypeRequest == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            HabitTypeResponseDTO updatedHabitType = habitTypeService.updateHabitType(id, habitTypeRequest);
            return ResponseEntity.ok(updatedHabitType);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/habit_type/{id}")
    public ResponseEntity<Void> deleteHabitType(@PathVariable Long id) {
        if (id < 0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            habitTypeService.deleteHabitType(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


}