package trackup.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import trackup.services.DailyRecordService;
import trackup.services.GoalService;
import trackup.services.HabitService;
import trackup.services.UserService;

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

        // Si el usuario no está autenticado, redirigir a login
        if ("anonymousUser".equals(username)) {
            return "redirect:/login";
        }

        // Obtener el usuario por username
        userService.findUserByUsername(username).ifPresent(user -> {
            model.addAttribute("username", username);
            model.addAttribute("habits", habitService.getAllHabitsByUserId(user.getId()));
            model.addAttribute("goals", goalService.getAllGoalsByUserId(user.getId()));
            model.addAttribute("records", dailyRecordService.getAllDailyRecordsByUserId(user.getId()));
        });

        return "index";
    }

    @GetMapping("/habits")
    public String habits(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        userService.findUserByUsername(username).ifPresent(user -> {
            model.addAttribute("habits", habitService.getAllHabitsByUserId(user.getId()));
        });
        
        return "habits";
    }

    @GetMapping("/goals")
    public String goals(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        userService.findUserByUsername(username).ifPresent(user -> {
            model.addAttribute("goals", goalService.getAllGoalsByUserId(user.getId()));
        });
        
        return "goals";
    }

    @GetMapping("/records")
    public String records(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        userService.findUserByUsername(username).ifPresent(user -> {
            model.addAttribute("records", dailyRecordService.getAllDailyRecordsByUserId(user.getId()));
        });
        
        return "records";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        userService.findUserByUsername(username).ifPresent(user -> {
            model.addAttribute("user", user);
        });
        
        return "profile";
    }

}