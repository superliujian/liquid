package liquid.controller;

import liquid.domain.LocationType;
import liquid.persistence.domain.LocationEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.domain.ServiceProviderTypeEnity;
import liquid.persistence.repository.LocationRepository;
import liquid.persistence.repository.ServiceProviderRepository;
import liquid.persistence.repository.ServiceProviderTypeRepository;
import liquid.transport.web.domain.Leg;
import liquid.transport.web.domain.TransMode;
import liquid.transport.persistence.domain.LegEntity;
import liquid.transport.persistence.domain.TransportEntity;
import liquid.transport.persistence.repository.TransportRepository;
import liquid.transport.service.LegService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/26/13
 * Time: 9:54 PM
 */
@Controller
@RequestMapping("/task/{taskId}/planning/route/{routeId}")
public class LegController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(LegController.class);

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LegService legService;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private ServiceProviderTypeRepository stRepository;

    @RequestMapping(value = "/{tab}", method = RequestMethod.GET)
    public String initLeg(@PathVariable String taskId,
                          @PathVariable long routeId,
                          @PathVariable String tab,
                          Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);
        logger.debug("tab: {}", tab);

        Leg leg = new Leg();

        switch (tab) {
            case "rail":
                List<LocationEntity> stationLocs = locationRepository.findByType(LocationType.STATION.getType());
                long defaultDstLocId = computeDefaultDstLocId(stationLocs);
                leg.setDestinationId(defaultDstLocId);
                model.addAttribute("locations", stationLocs);
                break;
            case "barge":
                List<LocationEntity> portLocs = locationRepository.findByType(LocationType.PORT.getType());
                defaultDstLocId = computeDefaultDstLocId(portLocs);
                leg.setDestinationId(defaultDstLocId);
                ServiceProviderTypeEnity bargeType = stRepository.findOne(2L);
                model.addAttribute("sps", serviceProviderRepository.findByType(bargeType));
                model.addAttribute("locations", portLocs);
                break;
            case "vessel":
                portLocs = locationRepository.findByType(LocationType.PORT.getType());
                defaultDstLocId = computeDefaultDstLocId(portLocs);
                leg.setDestinationId(defaultDstLocId);
                ServiceProviderTypeEnity vesselType = stRepository.findOne(3L);
                model.addAttribute("sps", serviceProviderRepository.findByType(vesselType));
                model.addAttribute("locations", portLocs);
                break;
            case "road":
                List<LocationEntity> cityLocs = locationRepository.findByType(LocationType.CITY.getType());
                defaultDstLocId = computeDefaultDstLocId(cityLocs);
                leg.setDestinationId(defaultDstLocId);
                ServiceProviderTypeEnity roadType = stRepository.findOne(4L);
                model.addAttribute("sps", serviceProviderRepository.findByType(roadType));
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
                         @PathVariable long routeId,
                         @PathVariable String tab,
                         Leg leg) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);
        logger.debug("tab: {}", tab);

        TransportEntity route = TransportEntity.newInstance(TransportEntity.class, routeId);
        LocationEntity srcLoc = LocationEntity.newInstance(LocationEntity.class, leg.getSourceId());
        LocationEntity dstLoc = LocationEntity.newInstance(LocationEntity.class, leg.getDestinationId());

        LegEntity legEntity = new LegEntity();
        legEntity.setRoute(route);
        legEntity.setTransMode(TransMode.valueOf(tab.toUpperCase()).getType());
        if (null != leg.getServiceProviderId())
            legEntity.setSp(ServiceProviderEntity.newInstance(ServiceProviderEntity.class, leg.getServiceProviderId()));
        legEntity.setSrcLoc(srcLoc);
        legEntity.setDstLoc(dstLoc);
        legService.save(legEntity);
        return "redirect:/task/" + taskId + "/planning";
    }

    @RequestMapping(value = "/leg/{legId}", method = RequestMethod.GET)
    public String deleteLeg(@PathVariable String taskId,
                            @PathVariable long routeId,
                            @PathVariable long legId) {
        logger.debug("taskId: {}", taskId);
        logger.debug("routeId: {}", routeId);
        logger.debug("legId: {}", legId);

        legService.delete(legId);

        return "redirect:/task/" + taskId + "/planning";
    }

    private long computeDefaultDstLocId(List<LocationEntity> locationEntities) {
        int size = locationEntities.size();
        long id = 0;
        if (size < 2) {
            id = locationEntities.get(0).getId();
        } else {
            id = locationEntities.get(1).getId();
        }
        return id;
    }
}
