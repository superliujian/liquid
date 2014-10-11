package liquid.audit;

import liquid.security.SecurityContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mat on 10/11/14.
 */
//TODO: Alternative auditing solution.
// @Component
@Aspect
public class AuditAspect {
    private Logger logger = LoggerFactory.getLogger("auditing");

    @Before("execution(* *.charge.persistence.repository.ChargeRepository.save(..)) || " +
            "execution(* *.charge.persistence.repository.ChargeRepository.delete(..))")
    public void logChange(JoinPoint joinPoint) {
        // user, data type, operation, detail
        logger.info("{} {} {} {}", SecurityContext.getInstance().getUsername(), joinPoint.getSignature().getName(), joinPoint.getArgs()[0]);
    }
}
