package liquid.controller;

import liquid.persistence.domain.*;
import liquid.persistence.repository.ContainerRepository;
import liquid.persistence.repository.TransBargeRepository;
import liquid.persistence.repository.TransRailwayRepository;
import liquid.persistence.repository.TransVesselRepository;
import liquid.service.bpm.ActivitiEngineService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
public class AllocationController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(AllocationController.class);

    // TODO: will be RailwayService instead of.
    @Autowired
    private TransRailwayRepository railwayRepository;

    @Autowired
    private TransBargeRepository bargeRepository;

    @Autowired
    private TransVesselRepository vesselRepository;

    @Autowired
    private ContainerRepository containerRepository;

    public AllocationController() {}

    @ModelAttribute("tab")
    public String populateTab(@PathVariable String tab) {
        return tab;
    }

    @RequestMapping(value = "/{tab}", method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       @PathVariable String tab,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("tab: {}", tab);
        long orderId = bpmService.getOrderIdByTaskId(taskId);
        switch (tab) {
            case "railway":
                List<TransRailway> railways = railwayRepository.findByOrderId(orderId);
                model.addAttribute("railways", railways);
                return "allocation/railway";
            case "barge":
                List<TransBarge> barges = bargeRepository.findByOrderId(orderId);
                model.addAttribute("barges", barges);
                return "allocation/barge";
            case "vessel":
                List<TransVessel> vessels = vesselRepository.findByOrderId(orderId);
                model.addAttribute("vessels", vessels);
                return "allocation/vessel";
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
                TransRailway railway = railwayRepository.findOne(transId);
                model.addAttribute("trans", railway);
                // TODO: Here is performance issue.
                model.addAttribute("containers", containerRepository.findAll());
            case "barge":
                TransBarge barge = bargeRepository.findOne(transId);
                model.addAttribute("trans", barge);
                // TODO: Here is performance issue.
                model.addAttribute("containers", containerRepository.findAll());
            case "vessel":
                TransVessel vessel = vesselRepository.findOne(transId);
                model.addAttribute("trans", vessel);
                // TODO: Here is performance issue.
                model.addAttribute("containers", containerRepository.findAll());
            default:
                break;
        }
        return "allocation/allocating";
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
