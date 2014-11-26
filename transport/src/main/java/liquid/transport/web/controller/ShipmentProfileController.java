package liquid.transport.web.controller;

import liquid.persistence.domain.LocationEntity;
import liquid.transport.persistence.domain.LegEntity;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.persistence.repository.ShipmentRepository;
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
@RequestMapping("/shipment_profile")
public class ShipmentProfileController {
    @Autowired
    private ShipmentRepository shipmentRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        Iterable<ShipmentEntity> shipmentSet = shipmentRepository.findAll();
        model.addAttribute("shipmentSet", shipmentSet);
        return "shipment_profile/list";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String initForm(Model model) {
        ShipmentEntity shipment = new ShipmentEntity();
        shipment = shipmentRepository.save(shipment);
        List<Leg> legs = new ArrayList<>();
        for (int i = 0; i < 5; i++) legs.add(new Leg());
        model.addAttribute("shipment", shipment);
        model.addAttribute("legs", legs);
        return "shipment_profile/form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initForm(@PathVariable Long id, Model model) {
        ShipmentEntity shipment = shipmentRepository.findOne(id);

        Leg leg = new Leg();

        if (shipment.getLegs().size() == 0) {
            // First Line
            leg.setHead(Boolean.TRUE);
        } else {
            // Other Lines
            leg.setPrevId(shipment.getLegs().get(shipment.getLegs().size() - 1).getId());
        }

        model.addAttribute("transportOptions", TransMode.values());
        model.addAttribute("shipment", shipment);
        model.addAttribute("leg", leg);
        return "shipment_profile/form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String addLeg(@PathVariable Long id, Leg leg) {
        ShipmentEntity shipment = shipmentRepository.findOne(id);
        LegEntity entity = new LegEntity();

        entity.setShipment(shipment);
        entity.setHead(leg.getHead());
        entity.setTransMode(leg.getTransMode());
        entity.setSrcLoc(LocationEntity.newInstance(LocationEntity.class, leg.getSourceId()));
        entity.setDstLoc(LocationEntity.newInstance(LocationEntity.class, leg.getDestinationId()));
        if (null != leg.getPrevId())
            entity.setPrev(LegEntity.newInstance(LegEntity.class, leg.getPrevId()));

        shipment.getLegs().add(entity);
        shipmentRepository.save(shipment);
        return "redirect:/shipment_profile/" + id;
    }

    @RequestMapping(value = "/{id}/leg/{legId}", method = RequestMethod.POST)
    public String updateLeg(@PathVariable Long id, Model model) {
        ShipmentEntity shipment = shipmentRepository.findOne(id);
        model.addAttribute("shipment", shipment);
        model.addAttribute("leg", new LegEntity());
        return "redirect:shipment_profile/" + id;
    }
}
