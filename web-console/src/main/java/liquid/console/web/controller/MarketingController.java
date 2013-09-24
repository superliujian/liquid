package liquid.console.web.controller;

import liquid.console.web.bpm.ActivitiEngineService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/22/13
 * Time: 10:19 PM
 */
@Controller
@RequestMapping("/marketing")
public class MarketingController {
    @Autowired
    private ActivitiEngineService bpmEngineService;

    @ModelAttribute("tasks")
    public List<Task> populateTasks() {
        // TODO: Use ROLE_MARKETING to replace the hardcode from principal
        return bpmEngineService.listTask("marketing");
    }

    @RequestMapping(method = RequestMethod.GET)
    public void tasks(Model model, Principal principal) {
    }
}
