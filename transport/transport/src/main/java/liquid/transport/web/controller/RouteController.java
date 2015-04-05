package liquid.transport.web.controller;

import liquid.operation.domain.Location;
import liquid.transport.persistence.domain.PathEntity;
import liquid.transport.persistence.domain.RouteEntity;
import liquid.transport.service.RouteService;
import liquid.transport.web.domain.Path;
import liquid.transport.domain.TransMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Tao Ma on 11/27/14.
 */
@Controller
@RequestMapping("/route")
public class RouteController {
    private static final int size = 20;

    @Autowired
    private RouteService routeService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(defaultValue = "0", required = false) int number, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<RouteEntity> page = routeService.findAll(pageRequest);
        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/route?");
        return "route/list";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String getNew(Model model) {
        model.addAttribute("route", new RouteEntity());
        return "route/form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String get(@PathVariable Long id, Model model) {
        RouteEntity route = routeService.findOne(id);
        model.addAttribute("route", route);
        return "route/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String post(RouteEntity route, BindingResult bindingResult, Model model) {
        RouteEntity newRoute = null;
        if (null == route.getId()) {
            newRoute = routeService.save(route);
        } else {
            RouteEntity oldRoute = routeService.findOne(route.getId());
            oldRoute.setName(route.getName());
            oldRoute.setComment(route.getComment());
            newRoute = routeService.save(oldRoute);
        }
        return "redirect:/route/" + newRoute.getId();
    }

    @RequestMapping(method = RequestMethod.POST, params = "delete")
    public String post(RouteEntity route) {
        routeService.delete(route.getId());
        return "redirect:/route";
    }

    @RequestMapping(value = "/{id}/path", method = RequestMethod.POST)
    public String postPath(@PathVariable Long id, Path path, Model model) {
        PathEntity entity = new PathEntity();
        entity.setTransportMode(path.getTransportMode());
        entity.setFrom(Location.newInstance(Location.class, path.getFromId()));
        entity.setTo(Location.newInstance(Location.class, path.getToId()));

        RouteEntity newRoute = routeService.addPath(id, entity);
        return "redirect:/route/" + newRoute.getId();
    }

    @RequestMapping(value = "/{id}/path/new", method = RequestMethod.GET)
    public String getPathNew(@PathVariable Long id, Model model) {
        model.addAttribute("transportModeOptions", TransMode.values());

        Path path = new Path();
        path.setRouteId(id);
        model.addAttribute("path", path);
        return "route/path_form";
    }
}
