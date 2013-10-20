package liquid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/17/13
 * Time: 9:14 PM
 */
@Controller
@RequestMapping(value = "/error")
public class ErrorController {
    @RequestMapping(method = RequestMethod.GET)
    private String handle(HttpServletRequest request) {
        return "error/error";
    }

    @RequestMapping(value = "/error/403", method = RequestMethod.GET)
    public String handle403(Model model, Principal principal) {
        return "error/403";
    }
}
