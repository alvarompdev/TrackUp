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

/**
 * Controlador para manejar las vistas de los registros diarios de hábitos.
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
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
    public String listRecords(@PathVariable(required = false) Long userId,
                              @RequestParam(value = "filter", required = false, defaultValue = "all") String filter,
                              Model model) {
        Long current = getCurrentUserId();
        if (userId != null && !userId.equals(current)) {
            return "redirect:/daily-records?error=unauthorized";
        }
        Long uid = (userId == null ? current : userId);

        List<DailyRecordResponseDTO> allRecords = dailyRecordService.getAllDailyRecordsByUserId(uid);
        List<HabitResponseDTO> habits = habitService.getAllHabitsByUserId(uid);

        LocalDate today = LocalDate.now();

        long overallCompleted = allRecords.stream().filter(DailyRecordResponseDTO::getCompleted).count();
        long overallNotCompleted = allRecords.size() - overallCompleted;

        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
        List<DailyRecordResponseDTO> recordsThisWeek = allRecords.stream()
                .filter(r -> !r.getDate().isBefore(startOfWeek) && !r.getDate().isAfter(endOfWeek))
                .toList();
        long weeklyCompleted = recordsThisWeek.stream().filter(DailyRecordResponseDTO::getCompleted).count();
        long weeklyTotal = recordsThisWeek.size();
        long weeklyNotCompleted = weeklyTotal - weeklyCompleted;

        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        List<DailyRecordResponseDTO> recordsThisMonth = allRecords.stream()
                .filter(r -> !r.getDate().isBefore(startOfMonth) && !r.getDate().isAfter(endOfMonth))
                .toList();
        long monthlyCompleted = recordsThisMonth.stream().filter(DailyRecordResponseDTO::getCompleted).count();
        long monthlyTotal = recordsThisMonth.size();
        long monthlyNotCompleted = monthlyTotal - monthlyCompleted;

        List<DailyRecordResponseDTO> recordsForTable;

        switch (filter.toLowerCase()) {
            case "last7days":
                final LocalDate startDate7Days = today.minusDays(6);
                recordsForTable = allRecords.stream()
                        .filter(r -> !r.getDate().isBefore(startDate7Days) && !r.getDate().isAfter(today))
                        .toList();
                break;
            case "thisweek":
                final LocalDate startDateFilterThisWeek = today.with(DayOfWeek.MONDAY);
                final LocalDate endDateFilterThisWeek = today.with(DayOfWeek.SUNDAY);
                recordsForTable = allRecords.stream()
                        .filter(r -> !r.getDate().isBefore(startDateFilterThisWeek) && !r.getDate().isAfter(endDateFilterThisWeek))
                        .toList();
                break;
            case "thismonth":
                final LocalDate startDateFilterThisMonth = today.withDayOfMonth(1);
                final LocalDate endDateFilterThisMonth = today.withDayOfMonth(today.lengthOfMonth());
                recordsForTable = allRecords.stream()
                        .filter(r -> !r.getDate().isBefore(startDateFilterThisMonth) && !r.getDate().isAfter(endDateFilterThisMonth))
                        .toList();
                break;
            default:
                recordsForTable = allRecords;
                break;
        }

        Map<Long, Double> habitCompletionPercentage = new HashMap<>();
        for (HabitResponseDTO habit : habits) {
            long totalPorHabit = recordsForTable.stream()
                    .filter(r -> r.getHabitId().equals(habit.getId()))
                    .count();
            long completedPorHabit = recordsForTable.stream()
                    .filter(r -> r.getHabitId().equals(habit.getId()) && r.getCompleted())
                    .count();
            double pct = (totalPorHabit == 0) ? 0.0
                    : (completedPorHabit * 100.0 / totalPorHabit);
            habitCompletionPercentage.put(habit.getId(), Math.round(pct * 10.0) / 10.0);
        }

        model.addAttribute("userId", uid);
        model.addAttribute("filter", filter);
        model.addAttribute("records", recordsForTable);

        model.addAttribute("habits", habits);

        // Atributos para los gráficos circulares
        model.addAttribute("overallCompleted", overallCompleted);
        model.addAttribute("overallNotCompleted", overallNotCompleted);

        model.addAttribute("weeklyTotal", weeklyTotal);
        model.addAttribute("weeklyCompleted", weeklyCompleted);
        model.addAttribute("weeklyNotCompleted", weeklyNotCompleted);

        model.addAttribute("monthlyTotal", monthlyTotal);
        model.addAttribute("monthlyCompleted", monthlyCompleted);
        model.addAttribute("monthlyNotCompleted", monthlyNotCompleted);

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

        // --- INICIO DE LÍNEAS DE DEPURACIÓN (PARA COPIAR Y PEGAR) ---
        System.out.println("DEBUG: En DailyRecordWebController.showEditForm para ID: " + id);
        System.out.println("DEBUG: Valor de rec.getDate() (desde el servicio/DB): " + rec.getDate());
        // --- FIN DE LÍNEAS DE DEPURACIÓN ---

        Long uid = getCurrentUserId();
        if (!rec.getUserId().equals(uid)) {
            return "redirect:/daily-records?error=unauthorized";
        }

        DailyRecordRequestDTO dto = new DailyRecordRequestDTO();
        dto.setDate(rec.getDate());
        dto.setCompleted(rec.getCompleted());
        dto.setHabitId(rec.getHabitId());
        dto.setUserId(rec.getUserId());

        // --- INICIO DE LÍNEAS DE DEPURACIÓN (PARA COPIAR Y PEGAR) ---
        System.out.println("DEBUG: Valor de dto.getDate() (enviado al formulario): " + dto.getDate());
        // --- FIN DE LÍNEAS DE DEPURACIÓN ---

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