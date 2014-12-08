package liquid.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/27/13
 * Time: 11:46 PM
 */
public class LoggingInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    private static final ThreadLocal<Long> stopwatch = new ThreadLocal<>();

    // handler is an instance of org.springframework.web.method.HandlerMethod
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        stopwatch.set(System.currentTimeMillis());

        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            logger.debug("Pre {} {}", method.getBeanType().getSimpleName(), method.getMethod().getName());
        } else {
            logger.debug("Pre {}", handler);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            logger.debug("Post {} {}", method.getBeanType().getSimpleName(), method.getMethod().getName());
        } else {
            logger.debug("Post {}", handler);
        }
        if (modelAndView != null)
            logger.debug("model: {}", modelAndView.getModel());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            logger.debug("After {} {}", method.getBeanType().getSimpleName(), method.getMethod().getName());
            logger.debug("{} {} duration is {}ms.", method.getBeanType().getSimpleName(), method.getMethod().getName(), System.currentTimeMillis() - stopwatch.get());
        } else {
            logger.debug("After {}", handler);
        }
    }
}
