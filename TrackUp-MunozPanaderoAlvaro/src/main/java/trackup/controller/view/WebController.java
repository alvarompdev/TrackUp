package trackup.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import trackup.dto.response.HabitResponseDTO;
import trackup.dto.response.GoalResponseDTO;
import trackup.dto.response.DailyRecordResponseDTO;
import trackup.dto.response.UserResponseDTO;
import trackup.services.DailyRecordService;
import trackup.services.GoalService;
import trackup.services.HabitService;
import trackup.services.UserService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador para manejar las vistas de la aplicación web.
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Controller
public class WebController {

    @Autowired
    private HabitService habitService;

    @Autowired
    private GoalService goalService;

    @Autowired
    private DailyRecordService dailyRecordService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        UserResponseDTO user = userService.findUserByUsername(username).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        Long userId = user.getId();

        model.addAttribute("username", username);
        model.addAttribute("userId", userId);

        List<HabitResponseDTO> habits = habitService.getAllHabitsByUserId(userId);
        model.addAttribute("habits", habits);

        List<GoalResponseDTO> goals = goalService.getAllGoalsByUserId(userId);
        model.addAttribute("goals", goals);

        List<DailyRecordResponseDTO> records = dailyRecordService.getAllDailyRecordsByUserId(userId);
        model.addAttribute("records", records);

        LocalDate today = LocalDate.now();

        long completedCount = records.stream()
                .filter(r -> Boolean.TRUE.equals(r.getCompleted()))
                .count();
        long notCompletedCount = records.size() - completedCount;
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("notCompletedCount", notCompletedCount);

        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        List<DailyRecordResponseDTO> weeklyRecords = records.stream()
                .filter(r -> {
                    LocalDate d = r.getDate();
                    return d != null && !d.isBefore(startOfWeek) && !d.isAfter(endOfWeek);
                })
                .collect(Collectors.toList());
        long weeklyTotal = weeklyRecords.size();
        long weeklyCompleted = weeklyRecords.stream()
                .filter(r -> Boolean.TRUE.equals(r.getCompleted()))
                .count();
        model.addAttribute("weeklyTotal", weeklyTotal);
        model.addAttribute("weeklyCompleted", weeklyCompleted);

        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        List<DailyRecordResponseDTO> monthlyRecords = records.stream()
                .filter(r -> {
                    LocalDate d = r.getDate();
                    return d != null && !d.isBefore(startOfMonth) && !d.isAfter(endOfMonth);
                })
                .collect(Collectors.toList());
        long monthlyTotal = monthlyRecords.size();
        long monthlyCompleted = monthlyRecords.stream()
                .filter(r -> Boolean.TRUE.equals(r.getCompleted()))
                .count();
        model.addAttribute("monthlyTotal", monthlyTotal);
        model.addAttribute("monthlyCompleted", monthlyCompleted);

        Map<Long, Double> habitCompletionPercentage = habits.stream()
                .collect(Collectors.toMap(
                        HabitResponseDTO::getId,
                        habitDto -> {
                            List<DailyRecordResponseDTO> registrosDelHabit = records.stream()
                                    .filter(r -> r.getHabitId().equals(habitDto.getId()))
                                    .collect(Collectors.toList());
                            if (registrosDelHabit.isEmpty()) {
                                return 0.0;
                            }
                            long completadosPorHabit = registrosDelHabit.stream()
                                    .filter(r -> Boolean.TRUE.equals(r.getCompleted()))
                                    .count();
                            return (100.0 * completadosPorHabit) / registrosDelHabit.size();
                        }
                ));
        model.addAttribute("habitCompletionPercentage", habitCompletionPercentage);
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        userService.findUserByUsername(username).ifPresent(user -> {
            model.addAttribute("userId", user.getId());
        });
        return "about";
    }

}