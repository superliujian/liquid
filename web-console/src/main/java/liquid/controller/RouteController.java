package liquid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by redbrick9 on 8/28/14.
 */
@Controller
@RequestMapping("/order/{orderId}/route")
public class RouteController {
    public String list(@PathVariable Long orderId) {
        return "list";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String newRoute(@PathVariable Long orderId) {
        return "redirect:/route";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, params = "delete")
    public String deleteRoute(@PathVariable Long id) {
        return "redirect:/route";
    }
}