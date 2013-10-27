package liquid.controller;

import liquid.metadata.LocationType;
import liquid.metadata.TransMode;
import liquid.persistence.domain.Leg;
import liquid.persistence.domain.Location;
import liquid.persistence.domain.Route;
import liquid.persistence.domain.ServiceProvider;
import liquid.persistence.repository.LegRepository;
import liquid.persistence.repository.LocationRepository;
import liquid.persistence.repository.RouteRepository;
import liquid.persistence.repository.SpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/26/13
 * Time: 9:54 PM
 */
@Controller
@RequestMapping("/task/{taskId}/planning/{planningId}/route/{routeId}")
public class LegController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(LegController.class);

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private SpRepository spRepository;

    @Autowired
    private RouteRepository routeRepository;

    @RequestMapping(value = "/{tab}", method = RequestMethod.GET)
    public String initLeg(@PathVariable String taskId,
                          @PathVariable long planningId,
                          @PathVariable long routeId,
                          @PathVariable String tab,
                          Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planningId: {}", planningId);
        logger.debug("routeId: {}", routeId);
        logger.debug("tab: {}", tab);

        Leg leg = new Leg();

        switch (tab) {
            case "rail":
                List<Location> stationLocs = locationRepository.findByType(LocationType.STATION.getType());
                long defaultDstLocId = computeDefaultDstLocId(stationLocs);
                leg.setDstLocId(defaultDstLocId);
                model.addAttribute("locations", stationLocs);
                break;
            case "barge":
                List<Location> portLocs = locationRepository.findByType(LocationType.PORT.getType());
                defaultDstLocId = computeDefaultDstLocId(portLocs);
                leg.setDstLocId(defaultDstLocId);
                model.addAttribute("sps", spRepository.findByType(2));
                model.addAttribute("locations", portLocs);
                break;
            case "vessel":
                portLocs = locationRepository.findByType(LocationType.PORT.getType());
                defaultDstLocId = computeDefaultDstLocId(portLocs);
                leg.setDstLocId(defaultDstLocId);
                model.addAttribute("sps", spRepository.findByType(3));
                model.addAttribute("locations", portLocs);
                break;
            case "road":
                List<Location> cityLocs = locationRepository.findByType(LocationType.CITY.getType());
                defaultDstLocId = computeDefaultDstLocId(cityLocs);
                leg.setDstLocId(defaultDstLocId);
                model.addAttribute("sps", spRepository.findByType(5));
                model.addAttribute("locations", cityLocs);
                break;
            default:
                break;
        }

        model.addAttribute("tab", tab);
        model.addAttribute("routeId", routeId);
        model.addAttribute("leg", leg);
        return "planning/" + tab + "_tab";
    }

    @RequestMapping(value = "/{tab}", method = RequestMethod.POST)
    public String addLeg(@PathVariable String taskId,
                         @PathVariable long planningId,
                         @PathVariable long routeId,
                         @PathVariable String tab,
                         Leg leg,
                         Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);
        logger.debug("tab: {}", tab);

        Route route = routeRepository.findOne(routeId);
        Location srcLoc = locationRepository.findOne(leg.getSrcLocId());
        Location dstLoc = locationRepository.findOne(leg.getDstLocId());

        leg.setRoute(route);
        leg.setTransMode(TransMode.valueOf(tab.toUpperCase()).getType());
        if (leg.getSpId() > 0) {
            ServiceProvider sp = spRepository.findOne(leg.getSpId());
            leg.setSp(sp);
        }
        leg.setSrcLoc(srcLoc);
        leg.setDstLoc(dstLoc);
        legRepository.save(leg);
        return "redirect:/task/" + taskId + "/planning/" + planningId;
    }

    @RequestMapping(value = "/leg/{legId}", method = RequestMethod.GET)
    public String deleteLeg(@PathVariable String taskId,
                            @PathVariable long planningId,
                            @PathVariable long routeId,
                            @PathVariable long legId,
                            Model model, Principal principal) {
        logger.debug("taskId: {}", taskId);
        logger.debug("planningId: {}", planningId);
        logger.debug("routeId: {}", routeId);
        logger.debug("legId: {}", legId);

        legRepository.delete(legId);

        return "redirect:/task/" + taskId + "/planning/" + planningId;
    }

    private long computeDefaultDstLocId(List<Location> locations) {
        int size = locations.size();
        long id = 0;
        if (size < 2) {
            id = locations.get(0).getId();
        } else {
            id = locations.get(1).getId();
        }
        return id;
    }
}
