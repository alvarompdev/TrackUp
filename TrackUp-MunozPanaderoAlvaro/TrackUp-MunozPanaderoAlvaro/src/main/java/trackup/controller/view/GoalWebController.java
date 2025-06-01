package trackup.controller.view;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import trackup.dto.request.GoalRequestDTO;
import trackup.dto.response.GoalResponseDTO;
import trackup.services.GoalService;
import trackup.services.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/goals")
public class GoalWebController {

    private final GoalService goalService;
    private final UserService userService;

    public GoalWebController(GoalService goalService, UserService userService) {
        this.goalService = goalService;
        this.userService = userService;
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

    // LISTAR metas (vacío o con /user/{userId})
    @GetMapping({"", "/user/{userId}"})
    public String listGoals(@PathVariable(required = false) Long userId, Model model) {
        Long uid = getCurrentUserId();
        if (userId != null && !userId.equals(uid)) {
            return "redirect:/goals?error=unauthorized";
        }
        Long finalUid = (userId == null ? uid : userId);

        List<GoalResponseDTO> goals = goalService.getAllGoalsByUserId(finalUid);
        model.addAttribute("goals", goals);
        model.addAttribute("userId", finalUid);
        return "goal-list";
    }

    // FORMULARIO CREAR
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Long uid = getCurrentUserId();
        GoalRequestDTO dto = new GoalRequestDTO();
        dto.setUserId(uid);
        model.addAttribute("goal", dto);
        model.addAttribute("userId", uid);
        model.addAttribute("isEdit", false);
        return "goal-form";
    }

    // FORMULARIO EDITAR
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<GoalResponseDTO> opt = goalService.findGoalById(id);
        if (opt.isEmpty()) {
            return "redirect:/goals?error=goal_not_found";
        }
        GoalResponseDTO g = opt.get();
        GoalRequestDTO dto = new GoalRequestDTO();
        dto.setName(g.getName());
        dto.setDescription(g.getDescription());
        dto.setUserId(g.getUserId());

        model.addAttribute("goal", dto);
        model.addAttribute("goalId", g.getId());
        model.addAttribute("userId", g.getUserId());
        model.addAttribute("isEdit", true);
        return "goal-form";
    }

    // GUARDAR / ACTUALIZAR
    @PostMapping("/save")
    public String saveGoal(
            @RequestParam(required = false) Long goalId,
            @ModelAttribute GoalRequestDTO dto,
            RedirectAttributes ra
    ) {
        if (goalId == null) {
            goalService.createGoal(dto);
            ra.addFlashAttribute("successMsg", "Meta creada");
        } else {
            goalService.updateGoal(goalId, dto);
            ra.addFlashAttribute("successMsg", "Meta actualizada");
        }
        return "redirect:/goals/user/" + dto.getUserId();
    }

    // ELIMINAR
    @GetMapping("/delete/{id}")
    public String deleteGoal(@PathVariable Long id) {
        goalService.deleteGoal(id);
        return "redirect:/goals";
    }

}