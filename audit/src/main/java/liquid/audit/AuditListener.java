package liquid.audit;

import liquid.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;

/**
 * Created by mat on 10/11/14.
 */
@Component
public class AuditListener {
    private Logger logger = LoggerFactory.getLogger("auditing");

    @PostPersist
    public void onCreated(Object entity) {
        if (null == entity)
            logger.info("{}", entity);
        else
            // user, data type, operation, detail
            logger.info("{} {} {} {}", SecurityContext.getInstance().getUsername(), entity.getClass().getSimpleName(), "save", entity);
    }

    @PostRemove
    public void onDeleted(Object entity) {
        if (null == entity)
            logger.info("{}", entity);
        else
            // user, data type, operation, detail
            logger.info("{} {} {} {}", SecurityContext.getInstance().getUsername(), entity.getClass().getSimpleName(), "delete", entity);
    }
}
