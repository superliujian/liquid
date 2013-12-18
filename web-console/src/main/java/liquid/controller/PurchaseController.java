package liquid.controller;

import liquid.metadata.ChargeWay;
import liquid.metadata.TransMode;
import liquid.persistence.domain.*;
import liquid.service.ChargeService;
import liquid.service.PlanningService;
import liquid.service.RouteService;
import liquid.service.SpService;
import liquid.utils.RoleHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private RouteService routeService;

    @Autowired
    private SpService spService;

    @ModelAttribute("chargeWays")
    public ChargeWay[] populateChargeWays() {
        return ChargeWay.values();
    }

    @RequestMapping(value = "/task/{taskId}/charge", method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       @RequestParam(value = "routeId", required = false, defaultValue = "0") long routeId,
                       @RequestParam(value = "legId", required = false, defaultValue = "0") long legId,
                       Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);
        logger.debug("legId: {}", legId);

        Map<Long, String> chargeTypes = chargeService.getChargeTypes();
        Iterable<ServiceProvider> sps = spService.findByType(spService.spTypeByChargeType(chargeTypes.entrySet().iterator().next().getKey().intValue()));

        Route route = routeService.find(routeId);
        Leg leg = planningService.findLeg(legId);

        Iterable<Charge> charges = null;
        if (null != route) {
            charges = chargeService.findByRouteId(routeId);
        }
        if (null != leg) {
            charges = chargeService.findByLegId(legId);
        }

        Charge charge = new Charge();
        charge.setFormRouteId(routeId);
        charge.setFormLegId(legId);

        model.addAttribute("cts", chargeTypes);
        model.addAttribute("sps", sps);
        model.addAttribute("charge", charge);
        model.addAttribute("route", route);
        model.addAttribute("leg", leg);
        model.addAttribute("charges", charges);
        model.addAttribute("transModes", TransMode.toMap());
        model.addAttribute("backToTask", taskService.computeTaskMainPath(taskId));
        return "charge/dashboard";
    }

    @RequestMapping(value = "/task/{taskId}/charge", method = RequestMethod.POST)
    public String addCharge(@PathVariable String taskId,
                            @RequestHeader(value = "referer", required = false) final String referer,
                            Charge charge,
                            Model model,
                            HttpServletRequest request,
                            Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("charge: {}", charge);

        charge.setCreateRole(RoleHelper.getRole(principal));
        charge.setCreateUser(principal.getName());
        charge.setCreateTime(new Date());
        charge.setUpdateUser(principal.getName());
        charge.setUpdateTime(new Date());
        chargeService.addCharge(charge);

        logger.debug("referer: {}", referer);
        return "redirect:" + referer;
    }

    @RequestMapping(value = "/task/{taskId}/charge/{chargeId}", method = RequestMethod.GET)
    public String init0(@PathVariable String taskId,
                        @RequestHeader(value = "referer", required = false) final String referer,
                        @PathVariable long chargeId,
                        Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);

        chargeService.removeCharge(chargeId);

        return "redirect:" + referer;
    }
}
