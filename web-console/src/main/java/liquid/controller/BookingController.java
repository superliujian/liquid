package liquid.controller;

import liquid.facade.BookingFacade;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.service.ServiceProviderService;
import liquid.task.domain.Task;
import liquid.transport.web.domain.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Locale;

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

        Iterable<ServiceProviderEntity> shipowners = serviceProviderService.findByType(3);
        model.addAttribute("shipowners", shipowners);
        return "booking/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String booking(@PathVariable String taskId, Booking booking, Model model) {
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        bookingFacade.save(order.getId(), booking);

        Task task = taskService.getTask(taskId);
        model.addAttribute("task", task);
        booking = bookingFacade.computeBooking(order.getId());
        model.addAttribute("booking", booking);
        Iterable<ServiceProviderEntity> shipowners = serviceProviderService.findByType(3);
        model.addAttribute("shipowners", shipowners);
        model.addAttribute("alert", messageSource.getMessage("save.success", new String[]{}, Locale.CHINA));
        return "booking/form";
    }
}
