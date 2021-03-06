package liquid.controller;

import liquid.domain.Disty;
import liquid.model.Alert;
import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.process.service.TaskService;
import liquid.security.SecurityContext;
import liquid.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/24/13
 * Time: 9:47 PM
 */
@Controller
@RequestMapping("/dp")
public class OrderDistyController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OrderDistyController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@RequestParam(value = "t") String taskId, Model model) {
        logger.debug("taskId: {}", taskId);

        long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        Disty disty = new Disty();
        disty.setDistyCny(order.getDistyCny());
        disty.setDistyUsd(order.getDistyUsd());
        model.addAttribute("disty", disty);
        model.addAttribute("task", taskService.getTask(taskId));
        return "order/disty_price";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String feed(@RequestParam(value = "t") String taskId,
                       @Valid @ModelAttribute("disty") Disty disty, BindingResult bindingResult,
                       Model model, RedirectAttributes redirectAttributes) {
        logger.debug("taskId: {}", taskId);

        if (bindingResult.hasErrors()) {
            return "order/disty_price";
        }

        long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        order.setDistyCny(disty.getDistyCny());
        order.setDistyUsd(disty.getDistyUsd());
        order.setUpdatedBy(SecurityContext.getInstance().getUsername());
        order.setUpdatedAt(new Date());
        orderService.save(order);

        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return "redirect:/dp?t=" + taskId;
    }
}
