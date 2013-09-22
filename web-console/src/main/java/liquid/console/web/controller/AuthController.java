package liquid.console.web.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/22/13
 * Time: 3:13 PM
 */
@Controller
public class AuthController {
    @RequestMapping("/")
    public String auth(Model model, Principal principal) {
        System.out.println("AuthController, principal: " + principal);
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;

        Collection<? extends GrantedAuthority> authorities = token.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            System.out.println("AuthController, authority:" + authority);
            if ("ROLE_SALES".equals(authority.getAuthority())) {
                return "sales";
            }
            if ("ROLE_MARKETING".equals(authority.getAuthority())) {
                return "marketing";
            }
        }

        return "welcome";
    }
}
