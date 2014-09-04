package liquid.controller;

import liquid.charge.persistence.domain.ChargeEntity;
import liquid.dto.EarningDto;
import liquid.metadata.ChargeWay;
import liquid.order.persistence.domain.OrderEntity;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.security.SecurityContext;
import liquid.service.ChargeService;
import liquid.service.ServiceSubtypeService;
import liquid.service.TaskService;
import liquid.service.bpm.ActivitiEngineService;
import liquid.shipping.domain.TaskBadgeDto;
import liquid.shipping.domain.TaskDto;
import liquid.task.NotCompletedException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/26/13
 * Time: 11:25 PM
 */
@Controller
@RequestMapping("/task")
public class TaskController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Deprecated
    @Autowired
    private ActivitiEngineService bpmService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @RequestMapping(method = RequestMethod.GET)
    public String tasks(Model model) {
        logger.debug("Role: {}", SecurityContext.getInstance().getRole());
        TaskDto[] tasks = taskService.listTasks(SecurityContext.getInstance().getRole());
        TaskBadgeDto taskBadge = taskService.calculateTaskBadge(SecurityContext.getInstance().getRole(),
                SecurityContext.getInstance().getUsername());
        model.addAttribute("tasks", tasks);
        model.addAttribute("taskBadge", taskBadge);
        model.addAttribute("title", "task.queue");
        //TODO: Using js to implement the function
        model.addAttribute("queueActive", "active");
        model.addAttribute("myActive", "");
        model.addAttribute("warningActive", "");
        return "task/list";
    }

    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public String myTasks(Model model) {
        TaskDto[] tasks = taskService.listMyTasks(SecurityContext.getInstance().getUsername());
        TaskBadgeDto taskBadge = taskService.calculateTaskBadge(SecurityContext.getInstance().getRole(),
                SecurityContext.getInstance().getUsername());
        model.addAttribute("tasks", tasks);
        model.addAttribute("taskBadge", taskBadge);
        model.addAttribute("title", "task.my");
        //TODO: Using js to implement the function
        model.addAttribute("queueActive", "");
        model.addAttribute("myActive", "active");
        model.addAttribute("warningActive", "");
        return "task/list";
    }

    @RequestMapping(value = "/warning", method = RequestMethod.GET)
    public String warningTasks(Model model) {
        TaskDto[] tasks = taskService.listWarningTasks();
        TaskBadgeDto taskBadge = taskService.calculateTaskBadge(SecurityContext.getInstance().getRole(),
                SecurityContext.getInstance().getUsername());
        model.addAttribute("tasks", tasks);
        model.addAttribute("taskBadge", taskBadge);
        model.addAttribute("title", "task.my");
        //TODO: Using js to implement the function
        model.addAttribute("queueActive", "");
        model.addAttribute("myActive", "");
        model.addAttribute("warningActive", "active");
        return "task/warning";
    }

    /**
     * TODO: Move to another controller.
     *
     * @param taskId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{taskId}/common", method = RequestMethod.GET)
    public String toCommon(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);
        TaskDto task = taskService.getTask(taskId);
        model.addAttribute("task", task);
        return "task/common";
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public String toRealTask(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);
        TaskDto task = taskService.getTask(taskId);
        logger.debug("task: {}", task);
        model.addAttribute("task", task);

        return "redirect:" + taskService.computeTaskMainPath(taskId);
    }

    @RequestMapping(method = RequestMethod.POST, params = "claim")
    public String claim(@RequestParam String taskId, HttpServletRequest request) {
        logger.debug("taskId: {}", taskId);
        try {
            bpmService.claimTask(taskId, SecurityContext.getInstance().getUsername());
        } catch (ActivitiTaskAlreadyClaimedException e) {
            request.getSession().setAttribute("message",
                    messageSource.getMessage("task.claimed.by.someone.else", new String[]{}, Locale.CHINA));
            return "error/error";
        }
        return "redirect:/task/" + taskId;
    }

    @RequestMapping(method = RequestMethod.POST, params = "complete")
    public String complete(@RequestParam String taskId,
                           @RequestHeader(value = "referer") String referer,
                           Model model) {
        logger.debug("taskId: {}", taskId);

        try {
            taskService.complete(taskId);
        } catch (NotCompletedException e) {
            model.addAttribute("task_error", getMessage(e.getCode()));
            return "redirect:" + referer;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:" + referer;
        }

        return "redirect:/task";
    }

    @RequestMapping(value = "/{taskId}/check_amount", method = RequestMethod.GET)
    public String checkAmount(@PathVariable String taskId, Model model) {
        long orderId = taskService.getOrderIdByTaskId(taskId);
        TaskDto task = taskService.getTask(taskId);
        model.addAttribute("task", task);

        if ("ROLE_COMMERCE".equals(SecurityContext.getInstance().getRole())) {
            Iterable<ChargeEntity> charges = chargeService.findByOrderId(orderId);
            model.addAttribute("charges", charges);
        } else {
            Iterable<ChargeEntity> charges = chargeService.findByOrderIdAndCreateRole(orderId,
                    SecurityContext.getInstance().getRole());
            model.addAttribute("charges", charges);
        }

        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<ServiceSubtypeEntity> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);
        return "charge/list";
    }

    @RequestMapping(value = "/{taskId}/settlement", method = RequestMethod.GET)
    public String settle(@PathVariable String taskId, Model model) {
        TaskDto task = taskService.getTask(taskId);
        model.addAttribute("task", task);

        OrderEntity order = taskService.findOrderByTaskId(taskId);
        Iterable<ChargeEntity> charges = chargeService.findByOrderId(order.getId());
        model.addAttribute("charges", charges);

        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<ServiceSubtypeEntity> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);

        EarningDto earning = chargeService.calculateEarning(order, charges);
        model.addAttribute("earning", earning);
        return "charge/settlement";
    }
}
