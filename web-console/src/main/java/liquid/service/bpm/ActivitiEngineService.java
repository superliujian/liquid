package liquid.service.bpm;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
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

    public void startProcess(String uid, long orderId, Map<String, Object> variableMap) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("processes/liquid.poc.bpmn20.xml").deploy();

        variableMap.put("employeeName", uid);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("liquidPoc", String.valueOf(orderId), variableMap);
        runtimeService.addUserIdentityLink(processInstance.getId(), uid, IdentityLinkType.STARTER);
        System.out.println(dataSource);
    }

    public List<HistoricTaskInstance> listCompltedTasks(String businessKey) {
        HistoryService historyService = processEngine.getHistoryService();
        HistoricTaskInstance instance = null;
        return historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(businessKey).list();
    }

    public List<Task> listTasks(String candidateGid) {
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskCandidateGroup(candidateGid).list();
        if (logger.isDebugEnabled()) {
            for (Task task : taskList) {
                logger.debug("task: {}; owner: {}", task, task.getOwner());
            }
        }

        return taskList;
    }

    public List<Task> listMyTasks(String uid) {
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(uid).list();
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

    public void complete(String taskId, String uid, Map<String, Object> variableMap) {
        variableMap.put("employeeName", uid);
        TaskService taskService = processEngine.getTaskService();
        taskService.complete(taskId, variableMap);
    }

    public String getBusinessKeyByTaskId(String taskId) {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Execution execution = runtimeService.createExecutionQuery().executionId(task.getProcessInstanceId()).singleResult();
        logger.debug("process instance id: {}", task.getExecutionId());
        if (execution instanceof ExecutionEntity) {
            ExecutionEntity entity = (ExecutionEntity) execution;
            logger.debug("business key: {}", entity.getBusinessKey());
            return entity.getBusinessKey();
        }
        throw new RuntimeException("execution is not an instance of ExecutionEntity");
    }

    public List<Task> listTasksByOrderId(long orderId) {
        TaskService taskService = processEngine.getTaskService();
        return taskService.createTaskQuery().processInstanceBusinessKey(String.valueOf(orderId)).list();
    }

    public Object getVariable(String taskId, String variableName) {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return processEngine.getRuntimeService().getVariable(task.getProcessInstanceId(), variableName);
    }

    public void setVariable(String taskId, String variableName, Object value) {
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        processEngine.getRuntimeService().setVariable(task.getProcessInstanceId(), variableName, value);
    }

    public List<Task> listWarningTasks() {
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().taskDefinitionKey("recordTod").list();
        Iterator<Task> iterator = taskList.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            Date now = new Date();
            Date created = task.getCreateTime();
            if ((now.getTime() - created.getTime()) < 2 * 24 * 60 * 60 * 1000) iterator.remove();
        }
        return taskList;
    }
}
