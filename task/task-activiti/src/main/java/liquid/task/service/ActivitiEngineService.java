package liquid.task.service;

import liquid.task.domain.BusinessKey;
import liquid.user.model.User;
import liquid.user.service.UserService;
import liquid.util.DatePattern;
import liquid.util.DateUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Qualifier("db")
    private UserService accountService;

//    @Autowired
//    private MailNotificationService mailNotificationService;

    @Autowired
    private MessageSource messageSource;

    public void startProcess(String uid, BusinessKey businessKey, Map<String, Object> variableMap) {
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //FIXME
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        repositoryService.createDeployment().addClasspathResource("processes/liquid.poc.bpmn20.xml").deploy();

        variableMap.put("employeeName", uid);
        variableMap.put("endTime", DateUtil.stringOf(Calendar.getInstance().getTime(), DatePattern.UNTIL_SECOND));
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("liquidPoc", businessKey.getText(), variableMap);
        runtimeService.addUserIdentityLink(processInstance.getId(), uid, IdentityLinkType.STARTER);

//        Account account = accountService.find(uid);
//        mailNotificationService.send(messageSource.getMessage("process.start", null, Locale.CHINA),
//                messageSource.getMessage("process.start.content", new String[]{uid}, Locale.CHINA),
//                account.getEmail());
    }

    public void claimTask(String taskId, String uid) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        taskService.claim(taskId, uid);

        User user = accountService.find(uid);
//        mailNotificationService.send(messageSource.getMessage("claim.task", null, Locale.CHINA),
//                messageSource.getMessage("claim.task.content", new String[]{uid}, Locale.CHINA),
//                user.getEmail());
    }

    public void complete(String taskId, String uid, Map<String, Object> variableMap) {
        variableMap.put("employeeName", uid);
        variableMap.put("endTime", DateUtil.stringOf(Calendar.getInstance().getTime(), DatePattern.UNTIL_SECOND));
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        taskService.complete(taskId, variableMap);

        User user = accountService.find(uid);
//        mailNotificationService.send(messageSource.getMessage("complete.task", null, Locale.CHINA),
//                messageSource.getMessage("complete.task.content", new String[]{uid}, Locale.CHINA),
//                user.getEmail());
    }

    public List<Task> listTasks(String candidateGid) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().orderByTaskCreateTime().asc().taskCandidateGroup(candidateGid).list();
        if (logger.isDebugEnabled()) {
            for (Task task : taskList) {
                logger.debug("task: {}; owner: {}", task, task.getOwner());
            }
        }

        return taskList;
    }

    public List<Task> listWarningTasks() {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
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

    public List<Task> listMyTasks(String uid) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery().orderByTaskCreateTime().asc().taskAssignee(uid).list();
        if (logger.isDebugEnabled()) {
            for (Task task : taskList) {
                logger.debug("task: {}; owner: {}", task, task.getOwner());
            }
        }

        return taskList;
    }

    public List<HistoricTaskInstance> listCompltedTasks(String businessKey) {
        HistoryService historyService = processEngine.getHistoryService();
        return historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(businessKey).list();
    }

    public Task getTask(String taskId) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        return taskService.createTaskQuery().taskId(taskId).singleResult();
    }

    public String getBusinessKeyByTaskId(String taskId) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return getBusinessKeyByProcessInstanceId(task.getProcessInstanceId());
    }

    public String getBusinessKeyByProcessInstanceId(String processInstanceId) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        return processInstance.getBusinessKey();
    }

    public List<Task> listTasksByOrderId(long orderId) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        return taskService.createTaskQuery().processInstanceBusinessKey(String.valueOf(orderId)).list();
    }

    public Object getVariable(String taskId, String variableName) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return processEngine.getRuntimeService().getVariable(task.getProcessInstanceId(), variableName);
    }

    public void setVariable(String taskId, String variableName, Object value) {
        org.activiti.engine.TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        processEngine.getRuntimeService().setVariable(task.getProcessInstanceId(), variableName, value);
    }
}
