package liquid.user.db.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

/**
 * Created by Tao Ma on 3/16/15.
 */
@Controller
public class UserController {

    @RequestMapping("/login")
    public void login() {}

    @RequestMapping("/")
    public String success(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // The credential's type is String here.
        String password = String.valueOf(authentication.getCredentials());
        String authorityText = "";
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            authorityText += authority.getAuthority().toUpperCase();
        }

        model.addAttribute("username", username);
        model.addAttribute("password", password);
        model.addAttribute("authority", authorityText);
        return "success";
    }
}
