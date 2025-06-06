package trackup.controller.view;

import jakarta.validation.Valid; // Importar esta
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Importar esta
import org.springframework.validation.ObjectError;  // Importar esta
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import trackup.dto.request.GoalRequestDTO;
import trackup.dto.response.GoalResponseDTO;
import trackup.services.GoalService;
import trackup.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Importar esta

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

        // Mensajes flash (solo si no están vacíos para evitar strings vacías en el modelo)
        if (successMsg != null && !successMsg.isEmpty()) model.addAttribute("successMsg", successMsg);
        if (errorMsg != null && !errorMsg.isEmpty()) model.addAttribute("errorMsg", errorMsg);


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
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<GoalResponseDTO> opt = goalService.findGoalById(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("errorMsg", "Meta no encontrada");
            return "redirect:/goals";
        }
        GoalResponseDTO g = opt.get();
        GoalRequestDTO dto = new GoalRequestDTO();
        // Para editar, necesitamos pasar el ID del objetivo actual para la URL de retorno
        // Esto es importante porque el saveGoal original usa goalId para diferenciar
        model.addAttribute("goalId", g.getId()); // Pasamos el ID real de la meta que estamos editando
        dto.setName(g.getName());
        dto.setDescription(g.getDescription());
        dto.setUserId(g.getUserId());

        model.addAttribute("goal", dto);
        model.addAttribute("userId", g.getUserId());
        model.addAttribute("isEdit", true);
        return "goal-form";
    }

    // GUARDAR / ACTUALIZAR
    @PostMapping("/save")
    public String saveGoal(
            @RequestParam(required = false) Long goalId,
            @Valid @ModelAttribute("goal") GoalRequestDTO dto, // <--- Añadido @Valid y "goal"
            BindingResult result, // <--- Añadido BindingResult
            RedirectAttributes ra,
            Model model // Añadido Model para pasar atributos si hay errores sin redirección
    ) {
        // Si hay errores de validación
        if (result.hasErrors()) {
            // Recolectar todos los errores del BindingResult en una sola cadena para mostrar
            String validationErrors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            model.addAttribute("errorMsg", validationErrors); // Usamos errorMsg para los errores de validación
            model.addAttribute("goal", dto); // Para que los datos ingresados persistan en el formulario
            model.addAttribute("userId", dto.getUserId()); // Necesario para la URL de cancelar
            model.addAttribute("isEdit", goalId != null); // Para renderizar correctamente el título y botón
            model.addAttribute("goalId", goalId); // Necesario para el hidden field del formulario de edición
            return "goal-form"; // No redireccionar para mostrar los errores en la misma página
        }

        try {
            if (goalId == null) {
                goalService.createGoal(dto);
                ra.addFlashAttribute("successMsg", "Meta creada correctamente.");
            } else {
                goalService.updateGoal(goalId, dto);
                ra.addFlashAttribute("successMsg", "Meta actualizada correctamente.");
            }
        } catch (IllegalArgumentException e) { // Usamos IllegalArgumentException para errores de negocio como nombre duplicado
            ra.addFlashAttribute("errorMsg", e.getMessage());
            ra.addFlashAttribute("goal", dto); // Mantener datos en caso de redirección por error
            ra.addFlashAttribute("org.springframework.validation.BindingResult.goal", result); // Persistir errores de validación (por si acaso)

            if (goalId == null) {
                return "redirect:/goals/new?userId=" + dto.getUserId();
            } else {
                return "redirect:/goals/edit/" + goalId;
            }
        } catch (Exception e) { // Capturar cualquier otra excepción inesperada
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

    // ELIMINAR
    @GetMapping("/delete/{id}")
    public String deleteGoal(@PathVariable Long id, RedirectAttributes ra) {
        Long uid = getCurrentUserId(); // Obtener el ID del usuario actual antes de eliminar
        try {
            goalService.deleteGoal(id);
            ra.addFlashAttribute("successMsg", "Meta eliminada correctamente.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Error al eliminar la meta: " + e.getMessage());
        }
        return "redirect:/goals/user/" + uid;
    }

}