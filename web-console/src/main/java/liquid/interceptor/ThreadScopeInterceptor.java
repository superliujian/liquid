package liquid.interceptor;

import liquid.context.BusinessContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/30/13
 * Time: 2:39 PM
 */
@Component
public class ThreadScopeInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ThreadScopeInterceptor.class);

    @Autowired
    private BusinessContext businessContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Principal principal = request.getUserPrincipal();
        logger.debug("principal: {}", principal);
        businessContext.setUsername(principal.getName());
        return true;
    }
}
