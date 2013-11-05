package liquid.controller;

import liquid.metadata.ChargeWay;
import liquid.metadata.TransMode;
import liquid.persistence.domain.*;
import liquid.service.ChargeService;
import liquid.service.PlanningService;
import liquid.service.SpService;
import liquid.utils.RoleHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 12:21 AM
 */
@Controller
public class PurchaseController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(ChargeController.class);

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private PlanningService planningService;

    @Autowired
    private SpService spService;

    @ModelAttribute("cts")
    public Map<Long, String> populateChargeTypes() {
        return chargeService.getChargeTypes();
    }

    @ModelAttribute("chargeWays")
    public ChargeWay[] populateChargeWays() {
        return ChargeWay.values();
    }

    @ModelAttribute("sps")
    public Iterable<ServiceProvider> populateSps() {
        return spService.findAll();
    }

    @RequestMapping(value = "/task/{taskId}/leg/{legId}/charge", method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       @PathVariable long legId,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("legId: {}", legId);

        Leg leg = planningService.findLeg(legId);
        Charge charge = new Charge();
        charge.setSpId(leg.getSpId());

        model.addAttribute("charge", charge);
        model.addAttribute("leg", leg);
        model.addAttribute("charges", chargeService.findByLegId(legId));
        model.addAttribute("transModes", TransMode.toMap());
        model.addAttribute("backToTask", taskService.computeTaskMainPath(taskId));
        return "charge/dashboard";
    }

    @RequestMapping(value = "/task/{taskId}/leg/{legId}/charge", method = RequestMethod.POST)
    public String addCharge(@PathVariable String taskId,
                            @PathVariable long legId,
                            Charge charge,
                            Model model,
                            HttpServletRequest request,
                            Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("legId: {}", legId);

        charge.setCreateRole(RoleHelper.getRole(principal));
        charge.setCreateUser(principal.getName());
        charge.setCreateTime(new Date());
        charge.setUpdateUser(principal.getName());
        charge.setUpdateTime(new Date());
        chargeService.addCharge(legId, charge);
        return "redirect:" + request.getServletPath();
    }

    @RequestMapping(value = "/task/{taskId}/leg/{legId}/charge/{chargeId}", method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       @PathVariable long legId,
                       @PathVariable long chargeId,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("legId: {}", legId);

        chargeService.removeCharge(chargeId);

        return "redirect:/task/" + taskId + "/leg/" + legId + "/charge";
    }
}
