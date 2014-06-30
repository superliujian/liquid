package liquid.controller;

import liquid.dto.DistyDto;
import liquid.order.persistence.domain.OrderEntity;
import liquid.service.OrderService;
import liquid.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/24/13
 * Time: 9:47 PM
 */
@Controller
@RequestMapping("/task/{taskId}/disty")
public class OrderDistyController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(OrderDistyController.class);

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
        OrderEntity order = orderService.find(orderId);
        DistyDto disty = new DistyDto();
        disty.setDistyPrice(order.getDistyPrice());
        model.addAttribute("done", done);
        model.addAttribute("disty", disty);
        return "order/disty_price";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String feed(@PathVariable String taskId,
                       @ModelAttribute("disty") DistyDto disty,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        //TODO: Validate distributor price data.

        long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        order.setDistyPrice(disty.getDistyPrice());
        order.setUpdateUser(principal.getName());
        order.setUpdateTime(new Date());
        orderService.save(order);
        model.addAttribute("done", true);
        return "redirect:/task/" + taskId + "/disty";
    }
}
