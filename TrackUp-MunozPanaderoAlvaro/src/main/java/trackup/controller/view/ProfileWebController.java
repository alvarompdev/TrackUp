package trackup.controller.view;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import trackup.dto.response.UserResponseDTO;
import trackup.services.UserService;
import java.util.Optional;

/**
 * Controlador para manejar las vistas del perfil del usuario.
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@Controller
@RequestMapping("/profile")
public class ProfileWebController {

    private final UserService userService;

    public ProfileWebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<UserResponseDTO> userOpt = userService.findUserByUsername(userDetails.getUsername());

        if (userOpt.isEmpty()) {
            return "redirect:/login?error";
        }

        UserResponseDTO user = userOpt.get();
        model.addAttribute("user", user);
        model.addAttribute("userId", user.getId());

        return "profile";
    }


}
