package liquid.controller;

import liquid.domain.Disty;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.Locale;

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
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);

        long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        Disty disty = new Disty();
        disty.setDistyCny(order.getDistyPrice());
        disty.setDistyUsd(order.getDistyUsd());
        model.addAttribute("disty", disty);
        return "order/disty_price";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String feed(@PathVariable String taskId,
                       @Valid @ModelAttribute("disty") Disty disty,
                       BindingResult bindingResult,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        if (bindingResult.hasErrors()) {
            return "order/disty_price";
        }

        long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        order.setDistyPrice(disty.getDistyCny());
        order.setDistyUsd(disty.getDistyUsd());
        order.setUpdateUser(principal.getName());
        order.setUpdateTime(new Date());
        orderService.save(order);

        model.addAttribute("disty", disty);
        model.addAttribute("alert", messageSource.getMessage("save.success", new String[]{}, Locale.CHINA));
        return "order/disty_price";
    }
}
