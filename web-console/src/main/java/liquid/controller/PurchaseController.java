package liquid.controller;

import liquid.charge.persistence.domain.ChargeEntity;
import liquid.domain.Charge;
import liquid.facade.ChargeFacade;
import liquid.metadata.ChargeWay;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.service.ChargeService;
import liquid.service.RouteService;
import liquid.service.ServiceProviderService;
import liquid.service.ServiceSubtypeService;
import liquid.shipping.domain.TransMode;
import liquid.shipping.persistence.domain.LegEntity;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.repository.LegRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    private ChargeFacade chargeFacade;

    @Autowired
    private RouteService routeService;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @ModelAttribute("chargeWays")
    public ChargeWay[] populateChargeWays() {
        return ChargeWay.values();
    }

    @ModelAttribute("transModes")
    public Map<Integer, String> populateTransModes() {
        return TransMode.toMap();
    }

    @RequestMapping(value = "/task/{taskId}/charge", method = RequestMethod.GET)
    public String init(@PathVariable String taskId,
                       @RequestParam(value = "routeId", required = false) Long routeId,
                       @RequestParam(value = "legId", required = false) Long legId,
                       Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);
        logger.debug("legId: {}", legId);

        Iterable<ServiceSubtypeEntity> serviceSubtypes = serviceSubtypeService.findEnabled();
        Iterable<ServiceProviderEntity> sps = serviceSubtypeService.find(serviceSubtypes.iterator().next().getId()).getServiceProviders();

        Iterable<ChargeEntity> charges = null;

        RouteEntity route = null;
        if (null != routeId) {
            route = routeService.find(routeId);
            charges = chargeService.findByRouteId(routeId);
        }

        LegEntity leg = null;
        if (null != legId) {
            leg = legRepository.findOne(legId);
            charges = chargeService.findByLegId(legId);
        }

        Charge charge = new Charge();
        charge.setRouteId(routeId);
        charge.setLegId(legId);
        charge.setWay(ChargeWay.PER_CONTAINER.getValue());

        model.addAttribute("serviceSubtypes", serviceSubtypes);
        model.addAttribute("sps", sps);
        model.addAttribute("charge", charge);
        model.addAttribute("route", route);
        model.addAttribute("leg", leg);
        model.addAttribute("charges", charges);
        model.addAttribute("backToTask", taskService.computeTaskMainPath(taskId));
        return "charge/dashboard";
    }

    @RequestMapping(value = "/task/{taskId}/charge", method = RequestMethod.POST)
    public String addCharge(@PathVariable String taskId,
                            @RequestHeader(value = "referer", required = false) final String referer,
                            @Valid @ModelAttribute Charge charge, BindingResult bindingResult, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("charge: {}", charge);
        if (bindingResult.hasErrors()) {
            Iterable<ServiceSubtypeEntity> serviceSubtypes = serviceSubtypeService.findEnabled();
            Iterable<ServiceProviderEntity> sps = serviceSubtypeService.find(charge.getServiceSubtypeId()).getServiceProviders();

            Iterable<ChargeEntity> charges = null;

            RouteEntity route = null;
            if (null != charge.getRouteId()) {
                route = routeService.find(charge.getRouteId());
                charges = chargeService.findByRouteId(charge.getRouteId());
            }

            LegEntity leg = null;
            if (null != charge.getLegId()) {
                leg = legRepository.findOne(charge.getLegId());
                charges = chargeService.findByLegId(charge.getLegId());
            }

            charge.setRouteId(charge.getRouteId());
            charge.setLegId(charge.getLegId());

            model.addAttribute("serviceSubtypes", serviceSubtypes);
            model.addAttribute("sps", sps);
            model.addAttribute("charge", charge);
            model.addAttribute("route", route);
            model.addAttribute("leg", leg);
            model.addAttribute("charges", charges);
            model.addAttribute("backToTask", taskService.computeTaskMainPath(taskId));
            return "charge/dashboard";
        } else {
            chargeFacade.save(charge);
            String redirect = "/task/" + taskId + "/charge";
            if (null == charge.getRouteId() && null == charge.getLegId())
                return "redirect:" + redirect;
            else if (null == charge.getRouteId()) {
                return "redirect:" + redirect + "?legId=" + charge.getLegId();
            } else if (null == charge.getLegId()) {
                return "redirect:" + redirect + "?routeId=" + charge.getRouteId();
            } else {
                return "redirect:" + redirect + "?routeId=" + charge.getRouteId() + "&legId=" + charge.getLegId();
            }
        }
    }

    @RequestMapping(value = "/task/{taskId}/charge/{chargeId}", method = RequestMethod.GET)
    public String init0(@PathVariable String taskId,
                        @RequestHeader(value = "referer", required = false) final String referer,
                        @PathVariable long chargeId) {
        logger.debug("taskId: {}", taskId);

        chargeService.removeCharge(chargeId);

        return "redirect:" + referer;
    }
}
