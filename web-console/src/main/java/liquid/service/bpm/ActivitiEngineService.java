package liquid.service.bpm;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/22/13
 * Time: 9:35 PM
 */
@Service
public class ActivitiEngineService {
    private static final Logger logger = LoggerFactory.getLogger(ActivitiEngineService.class);

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private SimpleDriverDataSource dataSource;

    public void startProcess(String uid, Map<String, Object> variableMap) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("processes/liquid.poc.bpmn20.xml").deploy();

        variableMap.put("employeeName", uid);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("liquidPoc", variableMap);
        runtimeService.addUserIdentityLink(processInstance.getId(), uid, IdentityLinkType.STARTER);
        System.out.println(dataSource);
    }

    public List<Task> listTask(String candidateGid) {
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskCandidateGroup(candidateGid).list();
        if (logger.isDebugEnabled()) {
            for (Task task : taskList) {
                logger.debug("task: {}; owner: {}", task, task.getOwner());
            }
        }

        return taskList;
    }

    public Task getTask(String taskId) {
        TaskService taskService = processEngine.getTaskService();
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    public void claimTask(String taskId, String uid) {
        TaskService taskService = processEngine.getTaskService();
        taskService.claim(taskId, uid);
    }
}
