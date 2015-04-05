package liquid.process.service;

import java.util.Map;

/**
 * Created by Tao Ma on 4/4/15.
 */
public interface ProcessService {
    void startProcess(String uid, BusinessKey businessKey, Map<String, Object> variableMap);

    void listProcessDefinitions(String key);
}
