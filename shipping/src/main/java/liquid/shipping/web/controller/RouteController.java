package liquid.shipping.web.controller;

import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.service.RouteService;
import liquid.shipping.web.domain.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by redbrick9 on 9/11/14.
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

    @RequestMapping(method = RequestMethod.GET, params = "action=edit")
    public String initEditForm(@RequestParam(value = "o") Long orderId, Model model) {
        OrderEntity order = orderService.find(orderId);
        Iterable<RouteEntity> routeEntities = routeService.findByOrderId(orderId);

        Routes routes = new Routes();

        model.addAttribute("tab", "route");
        model.addAttribute("order", order);
        model.addAttribute("routes", routes);
        return "route/edit";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String edit(@RequestParam(value = "o") Long orderId, @ModelAttribute Routes routes) {


        Collection<RouteEntity> routeEntities = new ArrayList<>();
        routeService.save(routeEntities);

        return "redirect:/route?o=" + orderId;
    }
}