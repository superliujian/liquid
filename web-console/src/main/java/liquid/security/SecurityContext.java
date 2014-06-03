package liquid.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
}
