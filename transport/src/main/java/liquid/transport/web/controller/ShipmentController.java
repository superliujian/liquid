package liquid.transport.web.controller;

import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.persistence.domain.LocationEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.service.ServiceProviderService;
import liquid.transport.persistence.domain.*;
import liquid.transport.persistence.repository.RailContainerRepository;
import liquid.transport.service.LegService;
import liquid.transport.service.RouteService;
import liquid.transport.service.ShipmentService;
import liquid.transport.web.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by redbrick9 on 9/11/14.
 */
@Controller
@RequestMapping("/shipment")
public class ShipmentController {
    private static final int size = 20;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private LegService legService;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private RailContainerRepository railContainerRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(defaultValue = "0", required = false) int number, Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<ShipmentEntity> page = shipmentService.findAll(pageRequest);
        model.addAttribute("page", page);
        model.addAttribute("contextPath", "/shipment?");
        return "shipment/list";
    }

    @RequestMapping(method = RequestMethod.GET, params = "o")
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

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, params = "save_to_route")
    public String saveToRoute(@PathVariable Long id) {
        ShipmentEntity shipment = shipmentService.find(id);
        List<LegEntity> legs = shipment.getLegs();

        RouteEntity route = new RouteEntity();
        route.setName("Copy form shipment " + shipment.getId());
        List<PathEntity> paths = new ArrayList<>(legs.size());
        PathEntity prev = null;
        for (LegEntity leg : legs) {
            PathEntity path = new PathEntity();
            path.setRoute(route);
            path.setTransportMode(leg.getTransMode());
            path.setFrom(leg.getSrcLoc());
            path.setTo(leg.getDstLoc());
            paths.add(path);

            if (null == prev) {
                route.setHead(path);
                route.setFrom(path.getFrom());
            } else {
                prev.setNext(path);
                route.setTo(path.getTo());
            }
            prev = path;
        }
        route.setPaths(paths);
        RouteEntity newRoute = routeService.save(route);

        return "redirect:/route/" + newRoute.getId();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String edit(@RequestParam(value = "o") Long orderId, @ModelAttribute ShipmentSet shipmentSet) {
        RailTransport[] railTransportSet = null;

        Collection<RailContainerEntity> entities = RailTransport.toEntities(railTransportSet);
        railContainerRepository.save(entities);

        return "redirect:/shipment?o=" + orderId;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, params = "delete")
    public String deleteShipment(@PathVariable Long id) {
        ShipmentEntity shipmentEntity = shipmentService.find(id);
        shipmentService.delete(id);
        return "redirect:/task/" + shipmentEntity.getTaskId() + "/planning";
    }

    @RequestMapping(value = "/{id}/leg", method = RequestMethod.GET)
    public String get(@PathVariable Long id, Model model) {
        Leg leg = new Leg();
        leg.setShipmentId(id);

        model.addAttribute("transportModeOptions", TransMode.values());
        model.addAttribute("leg", leg);
        return "leg/form";
    }

    @RequestMapping(value = "/{id}/leg", method = RequestMethod.POST)
    public String post(@PathVariable Long id, Leg leg, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Long serviceSubtype = TransMode.toServiceType(leg.getTransMode());
            Iterable<ServiceProviderEntity> sps = Collections.emptyList();
            if (serviceSubtype != null) {
                sps = serviceProviderService.findByType(serviceSubtype);
            }

            model.addAttribute("transportModeOptions", TransMode.values());
            model.addAttribute("sps", sps);
            model.addAttribute("leg", leg);
            return "leg/form";
        }

        ShipmentEntity shipmentEntity = shipmentService.find(id);

        LegEntity legEntity = null;
        if (null == leg.getId()) {
            legEntity = new LegEntity();
            legEntity.setTransMode(leg.getTransMode());
        } else {
            legEntity = legService.find(leg.getId());
        }

        if (leg.getServiceProviderId() != null)
            legEntity.setSp(ServiceProviderEntity.newInstance(ServiceProviderEntity.class, leg.getServiceProviderId()));
        legEntity.setSrcLoc(LocationEntity.newInstance(LocationEntity.class, leg.getSourceId()));
        legEntity.setDstLoc(LocationEntity.newInstance(LocationEntity.class, leg.getDestinationId()));
        legEntity.setShipment(shipmentEntity);
        legService.save(legEntity);

        return "redirect:/task/" + shipmentEntity.getTaskId() + "/planning";
    }

    private Long computeDefaultDstLocId(List<LocationEntity> locationEntities) {
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