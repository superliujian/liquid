package liquid.task;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@DefinitionKey("planRoute")
@Service
public class PlanRouteTask extends AbstractTaskProxy {
    @Override
    public void doBeforeComplete(String taskId, Map<String, Object> variableMap) {
        Map<String, Object> transTypes = planningService.getTransTypes(taskId);
        variableMap.putAll(transTypes);
    }
}
