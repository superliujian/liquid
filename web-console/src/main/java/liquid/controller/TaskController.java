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
import liquid.task.NotCompletedException;
import liquid.task.domain.Task;
import liquid.task.domain.TaskBar;
import liquid.task.service.ActivitiEngineService;
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
        return "redirect:/task?q=all";
    }

    @RequestMapping(method = RequestMethod.GET, params = "q")
    public String list(@RequestParam("q") String tab, Model model) {
        Task[] tasks;

        switch (tab) {
            case "all":
                tasks = taskService.listTasks(SecurityContext.getInstance().getRole());
                break;
            case "my":
                tasks = taskService.listMyTasks(SecurityContext.getInstance().getUsername());
                break;
            case "warning":
                tasks = taskService.listWarningTasks();
                break;
            default:
                tasks = new Task[0];
                break;
        }
        TaskBar taskBar = taskService.calculateTaskBar(SecurityContext.getInstance().getRole(),
                SecurityContext.getInstance().getUsername());
        taskBar.setTitle("task." + tab);
        model.addAttribute("taskBar", taskBar);
        model.addAttribute("tab", tab);
        model.addAttribute("tasks", tasks);
        return "task/list";
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
        Task task = taskService.getTask(taskId);
        model.addAttribute("task", task);
        return "task/common";
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public String redirect(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);
        Task task = taskService.getTask(taskId);
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
            logger.error(String.format("Complete taskId '%s' and referer '%s'.", taskId, referer), e);
            return "redirect:" + referer;
        }

        return "redirect:/task";
    }

    @RequestMapping(value = "/{taskId}/check_amount", method = RequestMethod.GET)
    public String checkAmount(@PathVariable String taskId, Model model) {
        long orderId = taskService.getOrderIdByTaskId(taskId);
        Task task = taskService.getTask(taskId);
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
        Task task = taskService.getTask(taskId);
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
