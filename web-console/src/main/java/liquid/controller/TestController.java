package liquid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 12/2/13
 * Time: 8:12 PM
 */
@Controller
@RequestMapping("/test")
public class TestController {
    @RequestMapping(value = "/{template}", method = RequestMethod.GET)
    public String combobox(@PathVariable String template,
                           Model model, Principal principal) {
        return "test/" + template;
    }
}
