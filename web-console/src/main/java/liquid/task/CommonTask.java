package liquid.task;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@Service
public class CommonTask extends AbstractTaskProxy {
    @Override
    public void doBeforeComplete(String taskId, Map<String, Object> variableMap) {}
}
