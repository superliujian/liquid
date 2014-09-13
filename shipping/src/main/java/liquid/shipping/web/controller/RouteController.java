package liquid.shipping.web.controller;

import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.service.ServiceProviderService;
import liquid.shipping.persistence.domain.RailContainerEntity;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.repository.RailContainerRepository;
import liquid.shipping.service.RouteService;
import liquid.shipping.web.domain.RailTransport;
import liquid.shipping.web.domain.Route;
import liquid.shipping.web.domain.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private RailContainerRepository railContainerRepository;

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
        routes.setOrderId(orderId);
        routes.setRoutes(Route.valueOf(routeEntities));

        model.addAttribute("tab", "route");
        model.addAttribute("order", order);
        model.addAttribute("routes", routes);
        model.addAttribute("fleets", serviceProviderService.findByType(4L));
        return "route/edit";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String edit(@RequestParam(value = "o") Long orderId, @ModelAttribute Routes routes) {
        RailTransport[] railTransportSet = null;

        Collection<RailContainerEntity> entities = RailTransport.toEntities(railTransportSet);
        railContainerRepository.save(entities);

        return "redirect:/route?o=" + orderId;
    }
}