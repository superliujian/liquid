package liquid.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/27/13
 * Time: 9:33 PM
 */
@Component
@Aspect
public class LoggingAspect {

    private Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* liquid.service..*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.debug("Before {}", joinPoint.getSignature());
    }

    @After("execution(* liquid.service..*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        logger.debug("After {}", joinPoint.getSignature());
    }
}
