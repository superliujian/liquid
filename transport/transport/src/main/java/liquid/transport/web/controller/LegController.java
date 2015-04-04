package liquid.transport.web.controller;

import liquid.operation.domain.ServiceProvider;
import liquid.operation.service.ServiceProviderService;
import liquid.service.LocationService;
import liquid.transport.persistence.domain.LegEntity;
import liquid.transport.service.LegService;
import liquid.transport.web.domain.Leg;
import liquid.transport.domain.TransMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;

/**
 * Created by Tao Ma on 12/1/14.
 */
@Controller
@RequestMapping("/leg")
public class LegController {

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private LegService legService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Model model) {
        LegEntity entity = legService.find(id);
        Leg leg = new Leg();
        leg.setId(entity.getId());
        leg.setShipmentId(entity.getShipment().getId());
        leg.setTransMode(entity.getTransMode());
        leg.setSourceId(entity.getSrcLoc().getId());
        leg.setSource(entity.getSrcLoc().getName());
        leg.setDestinationId(entity.getDstLoc().getId());
        leg.setDestination(entity.getDstLoc().getName());
        if (entity.getSp() != null)
            leg.setServiceProviderId(entity.getSp().getId());

        Long serviceType = TransMode.toServiceType(entity.getTransMode());
        Iterable<ServiceProvider> sps = Collections.emptyList();
        if (serviceType != null) {
            sps = serviceProviderService.findByType(serviceType);
        }

        model.addAttribute("transportModeOptions", TransMode.values());
        model.addAttribute("sps", sps);
        model.addAttribute("leg", leg);
        return "leg/form";
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String deleteLeg(@PathVariable Long id) {
        LegEntity legEntity = legService.find(id);
        legService.delete(id);
        return "redirect:/task/" + legEntity.getShipment().getTaskId() + "/planning";
    }
}
