package liquid.transport.web.controller;

import liquid.persistence.domain.LocationEntity;
import liquid.transport.persistence.domain.LegEntity;
import liquid.transport.persistence.domain.TransportEntity;
import liquid.transport.persistence.repository.TransportRepository;
import liquid.transport.web.domain.Leg;
import liquid.transport.web.domain.TransMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mat on 10/6/14.
 */
@Controller
@RequestMapping("/route_profile")
public class RouteProfileController {
    @Autowired
    private TransportRepository transportRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        Iterable<TransportEntity> routes = transportRepository.findAll();
        model.addAttribute("routes", routes);
        return "route_profile/list";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String initForm(Model model) {
        TransportEntity route = new TransportEntity();
        route = transportRepository.save(route);
        List<Leg> legs = new ArrayList<>();
        for (int i = 0; i < 5; i++) legs.add(new Leg());
        model.addAttribute("route", route);
        model.addAttribute("legs", legs);
        return "route_profile/form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initForm(@PathVariable Long id, Model model) {
        TransportEntity route = transportRepository.findOne(id);

        Leg leg = new Leg();

        if (route.getLegs().size() == 0) {
            // First Line
            leg.setHead(Boolean.TRUE);
        } else {
            // Other Lines
            leg.setPrevId(route.getLegs().get(route.getLegs().size() - 1).getId());
        }

        model.addAttribute("transportOptions", TransMode.values());
        model.addAttribute("route", route);
        model.addAttribute("leg", leg);
        return "route_profile/form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String addLeg(@PathVariable Long id, Leg leg) {
        TransportEntity route = transportRepository.findOne(id);
        LegEntity entity = new LegEntity();

        entity.setRoute(route);
        entity.setHead(leg.getHead());
        entity.setTransMode(leg.getTransMode());
        entity.setSrcLoc(LocationEntity.newInstance(LocationEntity.class, leg.getSourceId()));
        entity.setDstLoc(LocationEntity.newInstance(LocationEntity.class, leg.getDestinationId()));
        if (null != leg.getPrevId())
            entity.setPrev(LegEntity.newInstance(LegEntity.class, leg.getPrevId()));

        route.getLegs().add(entity);
        transportRepository.save(route);
        return "redirect:/route_profile/" + id;
    }

    @RequestMapping(value = "/{id}/leg/{legId}", method = RequestMethod.POST)
    public String updateLeg(@PathVariable Long id, Model model) {
        TransportEntity route = transportRepository.findOne(id);
        model.addAttribute("route", route);
        model.addAttribute("leg", new LegEntity());
        return "redirect:route_profile/" + id;
    }
}
