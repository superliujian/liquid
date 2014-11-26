package liquid.transport.web.controller;

import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.service.ServiceProviderService;
import liquid.transport.persistence.domain.RailContainerEntity;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.persistence.repository.RailContainerRepository;
import liquid.transport.service.ShipmentService;
import liquid.transport.web.domain.RailTransport;
import liquid.transport.web.domain.Shipment;
import liquid.transport.web.domain.ShipmentSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

/**
 * Created by redbrick9 on 9/11/14.
 */
@Controller
@RequestMapping("/shipment")
public class ShipmentController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private RailContainerRepository railContainerRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String findByOrder(@RequestParam(value = "o") Long orderId, Model model) {
        OrderEntity order = orderService.find(orderId);
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        model.addAttribute("tab", "shipment");
        model.addAttribute("order", order);
        model.addAttribute("shipmentSet", shipmentSet);
        return "shipment/detail";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=edit")
    public String initEditForm(@RequestParam(value = "o") Long orderId, Model model) {
        OrderEntity order = orderService.find(orderId);
        Iterable<ShipmentEntity> shipmentEntities = shipmentService.findByOrderId(orderId);

        ShipmentSet shipmentSet = new ShipmentSet();
        shipmentSet.setOrderId(orderId);
        shipmentSet.setShipments(Shipment.valueOf(shipmentEntities));

        model.addAttribute("tab", "shipment");
        model.addAttribute("order", order);
        model.addAttribute("shipmentSet", shipmentSet);
        model.addAttribute("fleets", serviceProviderService.findByType(4L));
        return "shipment/edit";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String edit(@RequestParam(value = "o") Long orderId, @ModelAttribute ShipmentSet shipmentSet) {
        RailTransport[] railTransportSet = null;

        Collection<RailContainerEntity> entities = RailTransport.toEntities(railTransportSet);
        railContainerRepository.save(entities);

        return "redirect:/shipment?o=" + orderId;
    }
}