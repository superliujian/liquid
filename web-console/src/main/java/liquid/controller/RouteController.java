package liquid.controller;

import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by redbrick9 on 8/28/14.
 */
@Controller
@RequestMapping("/route")
public class RouteController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private RouteService routeService;

    @RequestMapping(method = RequestMethod.GET)
    public String findByOrder(@RequestParam(value = "o") Long orderId, Model model) {
        OrderEntity order = orderService.find(orderId);
        Iterable<RouteEntity> routes = routeService.findByOrderId(orderId);
        model.addAttribute("tab", "route");
        model.addAttribute("order", order);
        model.addAttribute("routes", routes);
        return "route/detail";
    }
}