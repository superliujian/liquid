package liquid.controller;

import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.service.RouteService;
import liquid.shipping.persistence.domain.RouteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by redbrick9 on 9/4/14.
 */
@Controller
@RequestMapping("/rc")
public class RailwayContainerController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RouteService routeService;

    @RequestMapping(method = RequestMethod.GET, params = "o")
    public String findByOrder(@RequestParam(value = "o") Long orderId, Model model) {
        OrderEntity order = orderService.find(orderId);
        Iterable<RouteEntity> routes = routeService.findByOrderId(orderId);
        model.addAttribute("tab", "rc");
        model.addAttribute("order", order);
        model.addAttribute("routes", routes);
        return "railway/list_in_order";
    }
}
