package liquid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/7/13
 * Time: 7:17 PM
 */
@Controller
@RequestMapping("/diagram")
public class DiagramController {
    @RequestMapping(method = RequestMethod.GET)
    public String init(Model model, Principal principal) {
        return "diagram";
    }
}
