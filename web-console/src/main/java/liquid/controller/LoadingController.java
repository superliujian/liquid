package liquid.controller;

import liquid.persistence.domain.TransRailway;
import liquid.persistence.repository.TransRailwayRepository;
import liquid.service.bpm.ActivitiEngineService;
import liquid.utils.DateUtils;
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
import java.util.Date;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/3/13
 * Time: 9:42 PM
 */
@Controller
@RequestMapping("/task/{taskId}/loading")
public class LoadingController {
    private static final Logger logger = LoggerFactory.getLogger(AllocationController.class);

    @Autowired
    private ActivitiEngineService bpmService;

    // TODO: will be RailwayService instead of.
    @Autowired
    private TransRailwayRepository railwayRepository;

    @ModelAttribute("task")
    public Task populateTask(@PathVariable String taskId) {
        return bpmService.getTask(taskId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        long orderId = bpmService.getOrderIdByTaskId(taskId);
        List<TransRailway> railways = railwayRepository.findByOrderId(orderId);
        for (TransRailway railway : railways) {
            if (railway.getLoadingToc() != null) {
                railway.setLoadingTocStr(DateUtils.stringOf(railway.getLoadingToc()));
            }
            if (railway.getStationToa() != null) {
                railway.setStationToaStr(DateUtils.stringOf(railway.getStationToa()));
            }
        }
        model.addAttribute("railways", railways);
        return "loading/list";
    }

    @RequestMapping(value = "/{transId}/edit", method = RequestMethod.GET)
    public String initRecord(@PathVariable String taskId,
                             @PathVariable long transId,
                             Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("transId: {}", transId);

        TransRailway trans = railwayRepository.findOne(transId);
        logger.debug("trans: {}", trans);
        String defaultDateStr = DateUtils.stringOf(new Date());
        if (null == trans.getLoadingToc())
            trans.setLoadingTocStr(defaultDateStr);
        else
            trans.setLoadingTocStr(DateUtils.stringOf(trans.getLoadingToc()));

        if (null == trans.getStationToa())
            trans.setStationToaStr(defaultDateStr);
        else
            trans.setStationToaStr(DateUtils.stringOf(trans.getStationToa()));

        logger.debug("trans: {}", trans);
        model.addAttribute("trans", trans);
        return "loading/edit";
    }

    @RequestMapping(value = "/{transId}/edit", method = RequestMethod.POST)
    public String record(@ModelAttribute("trans") TransRailway trans,
                         @PathVariable String taskId,
                         @PathVariable long transId,
                         Principal principal) {
        logger.debug("trans: {}", trans);
        logger.debug("transId: {}", transId);

        TransRailway oldOne = railwayRepository.findOne(transId);

        if (trans.isBatch()) {
            List<TransRailway> railways = railwayRepository.findByPlanning(oldOne.getPlanning());
            for (TransRailway railway : railways) {
                railway.setLoadingToc(DateUtils.dateOf(trans.getLoadingTocStr()));
                railway.setStationToa(DateUtils.dateOf(trans.getStationToaStr()));
                railwayRepository.save(railway);
            }
        } else {
            oldOne.setLoadingToc(DateUtils.dateOf(trans.getLoadingTocStr()));
            oldOne.setStationToa(DateUtils.dateOf(trans.getStationToaStr()));
            railwayRepository.save(oldOne);
        }

        String redirect = "redirect:/task/" + taskId + "/loading";
        return redirect;
    }
}
