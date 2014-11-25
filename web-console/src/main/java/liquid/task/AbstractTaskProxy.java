package liquid.task;

import liquid.order.service.OrderService;
import liquid.security.SecurityContext;
import liquid.service.TaskService;
import liquid.transport.service.TransportService;
import liquid.transport.service.ShippingContainerService;
import liquid.task.service.ActivitiEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@Service
public abstract class AbstractTaskProxy {
    @Autowired
    protected ActivitiEngineService activitiEngineService;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected TransportService transportService;

    @Autowired
    protected ShippingContainerService shippingContainerService;

    public abstract void doBeforeComplete(String taskId, Map<String, Object> variableMap);

    public void complete(String taskId) {
        Map<String, Object> variableMap = new HashMap<>();
        doBeforeComplete(taskId, variableMap);
        activitiEngineService.complete(taskId, SecurityContext.getInstance().getUsername(), variableMap);
    }
}
