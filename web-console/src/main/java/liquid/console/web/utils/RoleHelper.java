package liquid.console.web.utils;

import liquid.SystemError;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/26/13
 * Time: 11:27 PM
 */
public final class RoleHelper {
    private RoleHelper() {}

    public static String getRole(Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            Collection<GrantedAuthority> authorities = token.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                return authority.getAuthority().toUpperCase();
            }
        }
        throw new SystemError("Invalid role.");
    }
}
