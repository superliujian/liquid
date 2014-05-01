package liquid.controller;

import liquid.dto.EarningDto;
import liquid.dto.TaskBadgeDto;
import liquid.dto.TaskDto;
import liquid.metadata.ChargeStatus;
import liquid.metadata.ChargeWay;
import liquid.persistence.domain.Charge;
import liquid.persistence.domain.Order;
import liquid.service.*;
import liquid.service.bpm.ActivitiEngineService;
import liquid.service.bpm.TaskHelper;
import liquid.utils.RoleHelper;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public String tasks(Model model, Principal principal) {
        logger.debug("Role: {}", RoleHelper.getRole(principal));
        TaskDto[] tasks = taskService.listTasks(RoleHelper.getRole(principal));
        TaskBadgeDto taskBadge = taskService.calculateTaskBadge(RoleHelper.getRole(principal), principal.getName());
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
    public String myTasks(Model model, Principal principal) {
        TaskDto[] tasks = taskService.listMyTasks(principal.getName());
        TaskBadgeDto taskBadge = taskService.calculateTaskBadge(RoleHelper.getRole(principal), principal.getName());
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
    public String warningTasks(Model model, Principal principal) {
        TaskDto[] tasks = taskService.listWarningTasks();
        TaskBadgeDto taskBadge = taskService.calculateTaskBadge(RoleHelper.getRole(principal), principal.getName());
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
     * @param principal
     * @return
     */
    @RequestMapping(value = "/{taskId}/common", method = RequestMethod.GET)
    public String toCommon(@PathVariable String taskId,
                           Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        TaskDto task = taskService.getTask(taskId);
        model.addAttribute("task", task);
        return "task/common";
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public String toRealTask(@PathVariable String taskId,
                             Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        TaskDto task = taskService.getTask(taskId);
        logger.debug("task: {}", task);
        model.addAttribute("task", task);

        return "redirect:" + taskService.computeTaskMainPath(taskId);
    }

    @RequestMapping(method = RequestMethod.POST, params = "claim")
    public String claim(@RequestParam String taskId,
                        Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) {
        logger.debug("taskId: {}", taskId);
        try {
            bpmService.claimTask(taskId, principal.getName());
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
                           Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        try {
            taskService.complete(taskId, principal.getName());
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
    public String checkAmount(@PathVariable String taskId,
                              Model model, Principal principal) {
        long orderId = taskService.getOrderIdByTaskId(taskId);
        TaskDto task = taskService.getTask(taskId);
        model.addAttribute("task", task);

        if ("ROLE_COMMERCE".equals(RoleHelper.getRole(principal))) {
            Iterable<Charge> charges = chargeService.findByOrderId(orderId);
            model.addAttribute("charges", charges);
        } else {
            Iterable<Charge> charges = chargeService.findByOrderIdAndCreateRole(orderId, RoleHelper.getRole(principal));
            model.addAttribute("charges", charges);
        }

        model.addAttribute("chargeWays", ChargeWay.values());
        model.addAttribute("cts", chargeService.getChargeTypes());
        return "charge/list";
    }

    @RequestMapping(value = "/{taskId}/settlement", method = RequestMethod.GET)
    public String settle(@PathVariable String taskId,
                         Model model, Principal principal) {
        TaskDto task = taskService.getTask(taskId);
        model.addAttribute("task", task);

        Order order = orderService.findByTaskId(taskId);
        Iterable<Charge> charges = chargeService.findByOrderId(order.getId());
        model.addAttribute("charges", charges);

        model.addAttribute("chargeWays", ChargeWay.values());
        model.addAttribute("cts", chargeService.getChargeTypes());

        EarningDto earning = chargeService.calculateEarning(order, charges);
        model.addAttribute("earning", earning);
        return "charge/settlement";
    }
}
