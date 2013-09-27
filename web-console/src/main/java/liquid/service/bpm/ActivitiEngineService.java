package liquid.service.bpm;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
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
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private SimpleDriverDataSource dataSource;

    public void startProcess(String uid, Map<String, Object> variableMap) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("processes/liquid.poc.bpmn20.xml").deploy();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("liquidPoc");
        runtimeService.addUserIdentityLink(processInstance.getId(), uid, IdentityLinkType.STARTER);
        runtimeService.setVariable(processInstance.getId(), "employeeName", uid);
        System.out.println(dataSource);
    }

    public List<Task> listTask(String candidateGid) {
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskCandidateGroup(candidateGid).list();
        return taskList;
    }
}
