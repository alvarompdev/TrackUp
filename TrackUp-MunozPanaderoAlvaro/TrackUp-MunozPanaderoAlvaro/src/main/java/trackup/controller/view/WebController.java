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
import trackup.entity.User;
import trackup.services.DailyRecordService;
import trackup.services.GoalService;
import trackup.services.HabitService;
import trackup.services.UserService;

import java.time.DayOfWeek; // Importar DayOfWeek
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        // 1) Obtener el nombre de usuario logueado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // 2) Buscar el usuario en BD para extraer el userId
        UserResponseDTO user = userService.findUserByUsername(username).orElse(null);
        if (user == null) {
            // Si no existe el usuario en sesión, redirigimos a login
            return "redirect:/login";
        }
        Long userId = user.getId();

        // 3) Añadimos al Model los atributos que usa index.html:
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);

        // --- 3.1) LISTAS PARA LA MITAD SUPERIOR ---
        // 3.1.1) Hábitos Actuales (DTOs)
        List<HabitResponseDTO> habits = habitService.getAllHabitsByUserId(userId);
        model.addAttribute("habits", habits);

        // 3.1.2) Metas Actuales (DTOs)
        List<GoalResponseDTO> goals = goalService.getAllGoalsByUserId(userId);
        model.addAttribute("goals", goals);

        // 3.1.3) Registros Recientes (lista entera de DTOs)
        List<DailyRecordResponseDTO> records = dailyRecordService.getAllDailyRecordsByUserId(userId);
        model.addAttribute("records", records);

        // --- 3.2) CONTADORES PARA LOS GRÁFICOS ---
        LocalDate today = LocalDate.now();

        // 3.2.1) “Desde Siempre”: completos / no completos
        long completedCount = records.stream()
                .filter(r -> Boolean.TRUE.equals(r.getCompleted()))
                .count();
        long notCompletedCount = records.size() - completedCount;
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("notCompletedCount", notCompletedCount);

        // 3.2.2) “Esta Semana”: Conteo de la semana calendario completa
        // Rango: desde el Lunes de la semana actual hasta el Domingo de la semana actual
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY); // Asume Sunday es el último día de la semana

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

        // 3.2.3) “Este Mes”: Conteo del mes calendario completo
        // Rango: desde el día 1 de este mes hasta el último día de este mes
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

        // --- 3.3) MAPA DE PORCENTAJE DE CUMPLIMIENTO POR HÁBITO ---
        // Thymeleaf usará habitCompletionPercentage.get(habitId) para colorear barras.
        Map<Long, Double> habitCompletionPercentage = habits.stream()
                .collect(Collectors.toMap(
                        HabitResponseDTO::getId,
                        habitDto -> {
                            // Filtrar solo los registros de este hábito (usando getHabitId() del DTO)
                            // Utilizaremos todos los registros disponibles para el cálculo de porcentaje aquí
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

        // 4) Devolver la vista "index" (index.html en templates/)
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