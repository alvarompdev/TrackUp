package trackup.controller.view;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import trackup.dto.request.HabitRequestDTO;
import trackup.dto.request.HabitTypeRequestDTO;
import trackup.dto.response.HabitResponseDTO;
import trackup.services.HabitService;
import trackup.services.HabitTypeService;
import trackup.services.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/habits")
public class HabitWebController {

    private final HabitService habitService;
    private final UserService userService;
    private final HabitTypeService habitTypeService;

    public HabitWebController(HabitService habitService,
                              UserService userService,
                              HabitTypeService habitTypeService) {
        this.habitService = habitService;
        this.userService = userService;
        this.habitTypeService = habitTypeService;
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if ("anonymousUser".equals(username)) {
            throw new IllegalStateException("Usuario no autenticado");
        }
        return userService.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"))
                .getId();
    }

    @GetMapping({"", "/user/{userId}"})
    public String listHabits(@PathVariable(required = false) Long userId, Model model) {
        Long current = getCurrentUserId();
        if (userId != null && !userId.equals(current)) {
            return "redirect:/habits?error=unauthorized";
        }
        Long uid = (userId == null ? current : userId);

        List<HabitResponseDTO> habits = habitService.getAllHabitsByUserId(uid);
        if (habits.isEmpty()) {
            model.addAttribute("emptyHabitsMsg", "No hay hábitos registrados. ¡Empieza hoy!");
        }
        model.addAttribute("habits", habits);

        List<?> tipos = habitTypeService.getAllHabitTypes();
        if (tipos.isEmpty()) {
            model.addAttribute("emptyTypesMsg", "No hay tipos de hábito registrados");
        }
        model.addAttribute("habitTypes", tipos);

        model.addAttribute("userId", uid);
        model.addAttribute("habitType", new HabitTypeRequestDTO());
        model.addAttribute("newHabit", new HabitRequestDTO());
        return "habit-list";
    }

    @PostMapping("/types/save")
    public String saveHabitType(@RequestParam String name, RedirectAttributes redirectAttributes) {
        try {
            HabitTypeRequestDTO dto = new HabitTypeRequestDTO();
            dto.setName(name);
            habitTypeService.createHabitType(dto);
            redirectAttributes.addFlashAttribute("successTypeMsg", "Tipo de hábito creado correctamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorTypeMsg", "No se pudo crear el tipo de hábito");
        }
        return "redirect:/habits";
    }

    @GetMapping("/types/delete/{id}")
    public String deleteHabitType(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Long currentUserId = getCurrentUserId();
        try {
            habitService.deleteAllByTypeId(id);
            habitTypeService.deleteHabitType(id);
            redirectAttributes.addFlashAttribute("successTypeMsg", "Tipo de hábito y hábitos asociados eliminados correctamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorTypeMsg", "No se pudo eliminar el tipo de hábito");
        }
        return "redirect:/habits/user/" + currentUserId;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Long uid = getCurrentUserId();
        HabitRequestDTO dto = new HabitRequestDTO();
        dto.setUserId(uid);
        model.addAttribute("habit", dto);
        model.addAttribute("userId", uid);
        model.addAttribute("habitTypes", habitTypeService.getAllHabitTypes());
        model.addAttribute("isEdit", false);
        return "habit-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<HabitResponseDTO> habitOpt = habitService.findHabitById(id);
        if (habitOpt.isEmpty()) {
            return "redirect:/habits?error=habit_not_found";
        }
        HabitResponseDTO habit = habitOpt.get();

        HabitRequestDTO dto = new HabitRequestDTO();
        dto.setId(habit.getId());
        dto.setName(habit.getName());
        dto.setDescription(habit.getDescription());
        dto.setFrequency(habit.getFrequency());
        dto.setStartDate(habit.getStartDate());
        dto.setEndDate(habit.getEndDate());
        dto.setUserId(habit.getUserId());
        dto.setHabitTypeId(habit.getHabitTypeId());

        model.addAttribute("habit", dto);
        model.addAttribute("userId", habit.getUserId());
        model.addAttribute("habitTypes", habitTypeService.getAllHabitTypes());
        model.addAttribute("isEdit", true);
        return "habit-form";
    }

    @PostMapping("/save")
    public String saveHabit(@ModelAttribute("habit") HabitRequestDTO dto, RedirectAttributes ra) {
        if (dto.getId() == null) {
            habitService.createHabit(dto);
            ra.addFlashAttribute("successMsg", "Hábito creado correctamente");
        } else {
            habitService.updateHabit(dto.getId(), dto);
            ra.addFlashAttribute("successMsg", "Hábito actualizado correctamente");
        }
        return "redirect:/habits/user/" + dto.getUserId();
    }

    @GetMapping("/delete/{id}")
    public String deleteHabit(@PathVariable Long id, RedirectAttributes ra) {
        Long currentUserId = getCurrentUserId();
        try {
            habitService.deleteHabit(id);
            ra.addFlashAttribute("successMsg", "Hábito eliminado correctamente");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMsg", "No se pudo eliminar el hábito");
        }
        return "redirect:/habits/user/" + currentUserId;
    }

}