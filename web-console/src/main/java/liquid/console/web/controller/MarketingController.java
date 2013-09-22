package liquid.console.web.controller;

import liquid.console.web.bpm.ActivitiEngineService;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/22/13
 * Time: 10:19 PM
 */
@Controller
@RequestMapping("/sales")
public class MarketingController {
    @RequestMapping(method = RequestMethod.GET)
    public void placeOrder(Model model, Principal principal) {
        ActivitiEngineService bpmService = new ActivitiEngineService();
        Task[] tasks = bpmService.listTask(principal.getName());
    }
}
