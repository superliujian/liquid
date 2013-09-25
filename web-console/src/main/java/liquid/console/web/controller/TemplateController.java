package liquid.console.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/25/13
 * Time: 10:45 PM
 */
@Controller
@RequestMapping("/template")
public class TemplateController {
    @RequestMapping(method = RequestMethod.GET)
    public void template(Model model, Principal principal) {}
}
