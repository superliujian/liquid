package liquid.controller;

import liquid.facade.BookingFacade;
import liquid.model.Alert;
import liquid.operation.domain.ServiceProvider;
import liquid.operation.service.ServiceProviderService;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.task.domain.Task;
import liquid.transport.web.domain.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by redbrick9 on 8/16/14.
 */
@Controller
@RequestMapping("/task/{taskId}/booking")
public class BookingController extends BaseTaskController {
    @Autowired
    private BookingFacade bookingFacade;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET)
    public String booking(@PathVariable String taskId, Model model) {
        Task task = taskService.getTask(taskId);
        task = taskService.getTask(taskId);
        model.addAttribute("task", task);
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);

        Booking booking = bookingFacade.computeBooking(order.getId());
        model.addAttribute("booking", booking);

        Iterable<ServiceProvider> shipowners = serviceProviderService.findByType(3L);
        model.addAttribute("shipowners", shipowners);
        return "booking/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String booking(@PathVariable String taskId, Booking booking, Model model, RedirectAttributes redirectAttributes) {
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        bookingFacade.save(order.getId(), booking);

        redirectAttributes.addFlashAttribute("alert", new Alert("save.success"));
        return "redirect:/task/" + taskId + "booking";
    }
}
