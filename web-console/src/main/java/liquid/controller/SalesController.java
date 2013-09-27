package liquid.controller;

import liquid.service.bpm.ActivitiEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.HashMap;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/22/13
 * Time: 9:28 PM
 */
@Controller
@RequestMapping("/sales")
public class SalesController {
    @Autowired
    private ActivitiEngineService bpmEngineService;

    @RequestMapping(method = RequestMethod.POST)
    public void placeOrder(Model model, Principal principal) {
        bpmEngineService.startProcess(principal.getName(), new HashMap<String, Object>());
    }
}
