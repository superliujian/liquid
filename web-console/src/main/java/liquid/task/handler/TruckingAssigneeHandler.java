package liquid.task.handler;

import liquid.metadata.Role;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/22/13
 * Time: 11:55 PM
 */
@Component
public class TruckingAssigneeHandler implements TaskListener {
    private static final Logger logger = LoggerFactory.getLogger(TruckingAssigneeHandler.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        String truckingRole = String.valueOf(delegateTask.getVariable("truckingRole"));
        logger.debug("truckingRole: {}", truckingRole);
        String orderOwner = String.valueOf(delegateTask.getVariable("orderOwner"));
        logger.debug("orderOwner: {}", orderOwner);
        switch (Role.valueOf(truckingRole)) {
            case SALES:
                delegateTask.setAssignee(orderOwner);
                break;
            case MARKETING:
                delegateTask.addCandidateGroup("ROLE_" + truckingRole);
                break;
            default:
                logger.warn("Role {} is illegal.", truckingRole);
                break;
        }
    }
}
