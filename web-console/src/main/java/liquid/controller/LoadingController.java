package liquid.controller;

import liquid.persistence.domain.RailContainer;
import liquid.service.ShippingContainerService;
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

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/3/13
 * Time: 9:42 PM
 */
@Controller
@RequestMapping("/task/{taskId}/loading")
public class LoadingController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(AllocationController.class);

    @Autowired
    private ShippingContainerService scService;

//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    public String init0(@PathVariable String taskId,
//                        Model model, Principal principal) {
//        logger.debug("taskId: {}", taskId);
//
//        long orderId = bpmService.getOrderIdByTaskId(taskId);
//        List<TransRailway> railways = railwayRepository.findByOrderId(orderId);
//        for (TransRailway railway : railways) {
//            if (railway.getLoadingToc() != null) {
//                railway.setLoadingTocStr(DateUtils.stringOf(railway.getLoadingToc()));
//            }
//            if (railway.getStationToa() != null) {
//                railway.setStationToaStr(DateUtils.stringOf(railway.getStationToa()));
//            }
//        }
//        model.addAttribute("railways", railways);
//        return "loading/list";
//    }
//
//    @RequestMapping(value = "/{transId}/edit", method = RequestMethod.GET)
//    public String initRecord(@PathVariable String taskId,
//                             @PathVariable long transId,
//                             Model model, Principal principal) {
//        logger.debug("taskId: {}", taskId);
//        logger.debug("transId: {}", transId);
//
//        TransRailway trans = railwayRepository.findOne(transId);
//        logger.debug("trans: {}", trans);
//        String defaultDateStr = DateUtils.stringOf(new Date());
//        if (null == trans.getLoadingToc())
//            trans.setLoadingTocStr(defaultDateStr);
//        else
//            trans.setLoadingTocStr(DateUtils.stringOf(trans.getLoadingToc()));
//
//        if (null == trans.getStationToa())
//            trans.setStationToaStr(defaultDateStr);
//        else
//            trans.setStationToaStr(DateUtils.stringOf(trans.getStationToa()));
//
//        logger.debug("trans: {}", trans);
//        model.addAttribute("trans", trans);
//        return "loading/edit";
//    }
//
//    @RequestMapping(value = "/{transId}/edit", method = RequestMethod.POST)
//    public String record(@ModelAttribute("trans") TransRailway trans,
//                         @PathVariable String taskId,
//                         @PathVariable long transId,
//                         Principal principal) {
//        logger.debug("trans: {}", trans);
//        logger.debug("transId: {}", transId);
//
//        TransRailway oldOne = railwayRepository.findOne(transId);
//
//        if (trans.isBatch()) {
//            List<TransRailway> railways = railwayRepository.findByPlanning(oldOne.getPlanning());
//            for (TransRailway railway : railways) {
//                railway.setLoadingToc(DateUtils.dateOf(trans.getLoadingTocStr()));
//                railway.setStationToa(DateUtils.dateOf(trans.getStationToaStr()));
//                railwayRepository.save(railway);
//            }
//        } else {
//            oldOne.setLoadingToc(DateUtils.dateOf(trans.getLoadingTocStr()));
//            oldOne.setStationToa(DateUtils.dateOf(trans.getStationToaStr()));
//            railwayRepository.save(oldOne);
//        }
//
//        String redirect = "redirect:/task/" + taskId + "/loading";
//        return redirect;
//    }

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        model.addAttribute("containers", scService.initialize(taskId));
        return "loading/main";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.GET)
    public String initRecord(@PathVariable String taskId,
                             @PathVariable long containerId,
                             Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        RailContainer railContainer = scService.findRailContainer(containerId);
        logger.debug("railContainer: {}", railContainer);
        model.addAttribute("container", railContainer);
        return "loading/edit";
    }

    @RequestMapping(value = "/{containerId}", method = RequestMethod.POST)
    public String record(@PathVariable String taskId,
                         @PathVariable long containerId,
                         @ModelAttribute("container") RailContainer formBean,
                         Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("containerId: {}", containerId);

        scService.saveRailContainer(containerId, formBean);

        return "redirect:/task/" + taskId + "/loading";
    }
}
