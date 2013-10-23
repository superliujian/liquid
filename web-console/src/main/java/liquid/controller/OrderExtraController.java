package liquid.controller;

import liquid.dto.ExtraDto;
import liquid.persistence.domain.Order;
import liquid.service.OrderService;
import liquid.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/21/13
 * Time: 8:20 PM
 */
@Controller
@RequestMapping("/task/{taskId}/extra")
public class OrderExtraController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(OrderExtraController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       @RequestParam(required = false) boolean done,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        long orderId = taskService.getOrderIdByTaskId(taskId);
        Order order = orderService.find(orderId);
        ExtraDto extra = new ExtraDto();
        extra.setExtExp(order.getExtExp());
        extra.setExtExpComment(order.getExtExpComment());
        model.addAttribute("done", done);
        model.addAttribute("extra", extra);
        return "order/extra";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String feed(@PathVariable String taskId,
                       @ModelAttribute("extra") ExtraDto extra,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        long orderId = taskService.getOrderIdByTaskId(taskId);
        Order order = orderService.find(orderId);
        order.setExtExp(extra.getExtExp());
        order.setExtExpComment(extra.getExtExpComment());
        orderService.save(order);
        model.addAttribute("done", true);
        return "redirect:/task/" + taskId + "/extra";
    }
}
