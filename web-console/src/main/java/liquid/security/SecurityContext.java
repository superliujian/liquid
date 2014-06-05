package liquid.security;

import liquid.SystemError;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * Created by redbrick9 on 6/3/14.
 */
public class SecurityContext {
    private static class InstanceHolder {
        private static SecurityContext instance = new SecurityContext();
    }

    private SecurityContext() {}

    public static SecurityContext getInstance() {
        return InstanceHolder.instance;
    }

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public String getRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            return authority.getAuthority().toUpperCase();
        }
        throw new SystemError("Invalid role.");
    }
}
