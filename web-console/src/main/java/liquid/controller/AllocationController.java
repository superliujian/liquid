package liquid.controller;

import liquid.persistence.domain.BaseTrans;
import liquid.persistence.domain.Container;
import liquid.persistence.domain.TransRailway;
import liquid.persistence.repository.ContainerRepository;
import liquid.persistence.repository.TransRailwayRepository;
import liquid.service.bpm.ActivitiEngineService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Container allocation controller.
 * User: tao
 * Date: 10/3/13
 * Time: 2:42 PM
 */
@Controller
@RequestMapping("/task/{taskId}/allocation")
public class AllocationController {
    private static final Logger logger = LoggerFactory.getLogger(AllocationController.class);

    @Autowired
    private ActivitiEngineService bpmService;

    // TODO: will be RailwayService instead of.
    @Autowired
    private TransRailwayRepository railwayRepository;

    @Autowired
    private ContainerRepository containerRepository;

    @ModelAttribute("tab")
    public String populateTab(@PathVariable String tab) {
        return tab;
    }

    @ModelAttribute("taskId")
    public String populateTaskId(@PathVariable String taskId) {
        return taskId;
    }

    @ModelAttribute("task")
    public Task populateTask(@PathVariable String taskId) {
        return bpmService.getTask(taskId);
    }

    @RequestMapping(value = "/{tab}", method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       @PathVariable String tab,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("tab: {}", tab);

        switch (tab) {
            case "railway":
                long orderId = bpmService.getOrderIdByTaskId(taskId);
                List<TransRailway> railways = railwayRepository.findByOrderId(orderId);
                model.addAttribute("railways", railways);
                return "allocation/railway";
            default:
                return "allocation/railway";
        }
    }

    /**
     * Note: Here tab means transType.
     *
     * @param taskId
     * @param transType
     * @param transId
     * @param model
     * @param principal
     * @return
     */
    @RequestMapping(value = "/{tab}/{transId}/edit", method = RequestMethod.GET)
    public String initAllocating(@PathVariable String taskId,
                                 @PathVariable("tab") String transType,
                                 @PathVariable long transId,
                                 Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("transType: {}", transType);
        logger.debug("transId: {}", transId);

        switch (transType) {
            case "railway":
                BaseTrans trans = railwayRepository.findOne(transId);
                model.addAttribute("trans", trans);
                // TODO: Here is performance issue.
                model.addAttribute("containers", containerRepository.findAll());
                return "allocation/allocating";
            default:
                return "allocation/allocating";
        }
    }

    @RequestMapping(value = "/{tab}/{transId}/edit", method = RequestMethod.POST)
    public String allocate(@ModelAttribute("trans") BaseTrans trans,
                           @PathVariable String taskId,
                           @PathVariable("tab") String transType,
                           @PathVariable long transId,
                           Principal principal) {
        logger.debug("trans: {}", trans);
        logger.debug("transType: {}", transType);
        logger.debug("transId: {}", transId);

        switch (transType) {
            case "railway":
                TransRailway oldOne = railwayRepository.findOne(transId);
                Container container = containerRepository.findOne(trans.getContainerId());
                oldOne.setContainer(container);
                railwayRepository.save(oldOne);
                break;
            default:
                break;
        }
        String redirect = "redirect:/task/" + taskId + "/allocation/" + transType;
        return redirect;
    }
}
