package trackup.controller.view;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import trackup.dto.request.DailyRecordRequestDTO;
import trackup.dto.response.DailyRecordResponseDTO;
import trackup.dto.response.HabitResponseDTO;
import trackup.services.DailyRecordService;
import trackup.services.HabitService;
import trackup.services.UserService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/daily-records")
public class DailyRecordWebController {

    private final DailyRecordService dailyRecordService;
    private final HabitService habitService;
    private final UserService userService;

    public DailyRecordWebController(DailyRecordService dailyRecordService,
                                    HabitService habitService,
                                    UserService userService) {
        this.dailyRecordService = dailyRecordService;
        this.habitService = habitService;
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
    public String listRecords(@PathVariable(required = false) Long userId, Model model) {
        Long current = getCurrentUserId();
        if (userId != null && !userId.equals(current)) {
            return "redirect:/daily-records?error=unauthorized";
        }
        Long uid = (userId == null ? current : userId);

        // 1. Recuperar todos los registros y hábitos del usuario
        List<DailyRecordResponseDTO> records = dailyRecordService.getAllDailyRecordsByUserId(uid);
        List<HabitResponseDTO> habits = habitService.getAllHabitsByUserId(uid);

        // 2. Cálculo global completados vs no completados
        long completedCount = records.stream().filter(DailyRecordResponseDTO::getCompleted).count();
        long notCompletedCount = records.size() - completedCount;

        // 3. Cálculo “Esta Semana” (desde el lunes de la semana actual)
        LocalDate today = LocalDate.now();
        LocalDate mondayThisWeek = today.with(DayOfWeek.MONDAY);
        long weeklyTotal = records.stream()
                .filter(r -> !r.getDate().isBefore(mondayThisWeek))
                .count();
        long weeklyCompleted = records.stream()
                .filter(r -> !r.getDate().isBefore(mondayThisWeek) && r.getCompleted())
                .count();

        // 4. Cálculo “Este Mes” (desde el primer día del mes actual)
        LocalDate firstOfMonth = today.withDayOfMonth(1);
        long monthlyTotal = records.stream()
                .filter(r -> !r.getDate().isBefore(firstOfMonth))
                .count();
        long monthlyCompleted = records.stream()
                .filter(r -> !r.getDate().isBefore(firstOfMonth) && r.getCompleted())
                .count();

        // 5. Porcentaje de cumplimiento por hábito
        Map<Long, Double> habitCompletionPercentage = new HashMap<>();
        for (HabitResponseDTO habit : habits) {
            long totalPorHabit = records.stream()
                    .filter(r -> r.getHabitId().equals(habit.getId()))
                    .count();
            long completedPorHabit = records.stream()
                    .filter(r -> r.getHabitId().equals(habit.getId()) && r.getCompleted())
                    .count();
            double pct = (totalPorHabit == 0) ? 0.0
                    : (completedPorHabit * 100.0 / totalPorHabit);
            pct = Math.round(pct * 10.0) / 10.0; // redondeo a un decimal
            habitCompletionPercentage.put(habit.getId(), pct);
        }

        // 6. Inyectar atributos en el modelo
        model.addAttribute("userId", uid);
        model.addAttribute("records", records);
        model.addAttribute("habits", habits);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("notCompletedCount", notCompletedCount);
        model.addAttribute("weeklyTotal", weeklyTotal);
        model.addAttribute("weeklyCompleted", weeklyCompleted);
        model.addAttribute("monthlyTotal", monthlyTotal);
        model.addAttribute("monthlyCompleted", monthlyCompleted);
        model.addAttribute("habitCompletionPercentage", habitCompletionPercentage);

        return "dailyrecord-list";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long habitId, Model model) {
        Long uid = getCurrentUserId();
        DailyRecordRequestDTO dto = new DailyRecordRequestDTO();
        dto.setUserId(uid);
        if (habitId != null) {
            dto.setHabitId(habitId);
        }

        model.addAttribute("record", dto);
        model.addAttribute("userId", uid);
        model.addAttribute("habits", habitService.getAllHabitsByUserId(uid));
        model.addAttribute("isEdit", false);
        return "dailyrecord-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<DailyRecordResponseDTO> recordOpt = dailyRecordService.findDailyRecordById(id);
        if (recordOpt.isEmpty()) {
            return "redirect:/daily-records?error=not_found";
        }
        DailyRecordResponseDTO rec = recordOpt.get();
        Long uid = getCurrentUserId();
        if (!rec.getUserId().equals(uid)) {
            return "redirect:/daily-records?error=unauthorized";
        }

        DailyRecordRequestDTO dto = new DailyRecordRequestDTO();
        dto.setDate(rec.getDate());
        dto.setCompleted(rec.getCompleted());
        dto.setHabitId(rec.getHabitId());
        dto.setUserId(rec.getUserId());

        model.addAttribute("record", dto);
        model.addAttribute("recordId", rec.getId());
        model.addAttribute("userId", uid);
        model.addAttribute("habits", habitService.getAllHabitsByUserId(uid));
        model.addAttribute("isEdit", true);
        return "dailyrecord-form";
    }

    @PostMapping("/save")
    public String saveRecord(@RequestParam(required = false) Long recordId,
                             @ModelAttribute("record") DailyRecordRequestDTO dto,
                             RedirectAttributes ra) {
        Long uid = getCurrentUserId();
        dto.setUserId(uid);

        if (recordId == null) {
            dailyRecordService.createDailyRecord(dto);
            ra.addFlashAttribute("successMsg", "Registro creado correctamente.");
        } else {
            dailyRecordService.updateDailyRecord(recordId, dto);
            ra.addFlashAttribute("successMsg", "Registro actualizado correctamente.");
        }

        return "redirect:/daily-records/user/" + uid;
    }

    @GetMapping("/delete/{id}")
    public String deleteRecord(@PathVariable Long id, RedirectAttributes ra) {
        Long uid = getCurrentUserId();
        Optional<DailyRecordResponseDTO> recOpt = dailyRecordService.findDailyRecordById(id);
        if (recOpt.isEmpty() || !recOpt.get().getUserId().equals(uid)) {
            return "redirect:/daily-records?error=unauthorized";
        }
        dailyRecordService.deleteDailyRecord(id);
        ra.addFlashAttribute("successMsg", "Registro eliminado.");
        return "redirect:/daily-records/user/" + uid;
    }

}