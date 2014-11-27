package liquid.transport.web.controller;

import liquid.transport.persistence.domain.RouteEntity;
import liquid.transport.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
}
