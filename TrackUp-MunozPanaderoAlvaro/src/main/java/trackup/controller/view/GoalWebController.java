package trackup.controller.view;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import trackup.dto.request.GoalRequestDTO;
import trackup.dto.response.GoalResponseDTO;
import trackup.services.GoalService;
import trackup.services.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador para manejar las vistas de las metas del usuario.
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
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

    @GetMapping({"", "/user/{userId}"})
    public String listGoals(@PathVariable(required = false) Long userId,
                            Model model,
                            @ModelAttribute("successMsg") String successMsg,
                            @ModelAttribute("errorMsg") String errorMsg) {
        Long uid = getCurrentUserId();
        if (userId != null && !userId.equals(uid)) {
            return "redirect:/goals?error=unauthorized";
        }
        Long finalUid = (userId == null ? uid : userId);

        List<GoalResponseDTO> goals = goalService.getAllGoalsByUserId(finalUid);
        model.addAttribute("goals", goals);
        model.addAttribute("userId", finalUid);

        if (successMsg != null && !successMsg.isEmpty()) model.addAttribute("successMsg", successMsg);
        if (errorMsg != null && !errorMsg.isEmpty()) model.addAttribute("errorMsg", errorMsg);


        return "goal-list";
    }

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

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<GoalResponseDTO> opt = goalService.findGoalById(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("errorMsg", "Meta no encontrada");
            return "redirect:/goals";
        }
        GoalResponseDTO g = opt.get();
        GoalRequestDTO dto = new GoalRequestDTO();

        model.addAttribute("goalId", g.getId());
        dto.setName(g.getName());
        dto.setDescription(g.getDescription());
        dto.setUserId(g.getUserId());

        model.addAttribute("goal", dto);
        model.addAttribute("userId", g.getUserId());
        model.addAttribute("isEdit", true);
        return "goal-form";
    }

    @PostMapping("/save")
    public String saveGoal(
            @RequestParam(required = false) Long goalId,
            @Valid @ModelAttribute("goal") GoalRequestDTO dto,
            BindingResult result,
            RedirectAttributes ra,
            Model model
    ) {

        if (result.hasErrors()) {
            String validationErrors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            model.addAttribute("errorMsg", validationErrors);
            model.addAttribute("goal", dto);
            model.addAttribute("userId", dto.getUserId());
            model.addAttribute("isEdit", goalId != null);
            model.addAttribute("goalId", goalId);
            return "goal-form";
        }

        try {
            if (goalId == null) {
                goalService.createGoal(dto);
                ra.addFlashAttribute("successMsg", "Meta creada correctamente.");
            } else {
                goalService.updateGoal(goalId, dto);
                ra.addFlashAttribute("successMsg", "Meta actualizada correctamente.");
            }
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMsg", e.getMessage());
            ra.addFlashAttribute("goal", dto);
            ra.addFlashAttribute("org.springframework.validation.BindingResult.goal", result);

            if (goalId == null) {
                return "redirect:/goals/new?userId=" + dto.getUserId();
            } else {
                return "redirect:/goals/edit/" + goalId;
            }
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error inesperado al guardar la meta: " + e.getMessage());
            ra.addFlashAttribute("goal", dto);
            if (goalId == null) {
                return "redirect:/goals/new?userId=" + dto.getUserId();
            } else {
                return "redirect:/goals/edit/" + goalId;
            }
        }
        return "redirect:/goals/user/" + dto.getUserId();
    }

    @GetMapping("/delete/{id}")
    public String deleteGoal(@PathVariable Long id, RedirectAttributes ra) {
        Long uid = getCurrentUserId();
        try {
            goalService.deleteGoal(id);
            ra.addFlashAttribute("successMsg", "Meta eliminada correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al eliminar la meta: " + e.getMessage());
        }
        return "redirect:/goals/user/" + uid;
    }

}