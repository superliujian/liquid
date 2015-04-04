package liquid.process.service;

import liquid.security.SecurityContext;
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
    protected TaskService taskService;

    public abstract void doBeforeComplete(String taskId, Map<String, Object> variableMap);

    public void complete(String taskId) {
        Map<String, Object> variableMap = new HashMap<>();
        doBeforeComplete(taskId, variableMap);
        taskService.complete(taskId, SecurityContext.getInstance().getUsername(), variableMap);
    }
}
