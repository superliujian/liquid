package liquid.transport.service;

import liquid.container.domain.ContainerStatus;
import liquid.container.persistence.domain.ContainerEntity;
import liquid.container.service.ContainerService;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.service.AbstractService;
import liquid.service.ServiceProviderService;
import liquid.transport.persistence.domain.*;
import liquid.transport.persistence.repository.*;
import liquid.transport.web.domain.*;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 12:36 AM
 */
@Service
public class ShippingContainerService extends AbstractService<ShippingContainerEntity, ShippingContainerRepository> {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private RailContainerRepository rcRepository;

    @Autowired
    private BargeContainerRepository bcRepository;

    @Autowired
    private VesselContainerRepository vcRepository;

    @Autowired
    private DeliveryContainerRepository dcRepository;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private ServiceProviderService serviceProviderService;

    @Override
    public void doSaveBefore(ShippingContainerEntity entity) { }

    public List<ShippingContainerEntity> findByRouteId(Long routeId) {
        return repository.findByRouteId(routeId);
    }

    @Transactional("transactionManager")
    public void initialize(Long orderId) {
        Iterable<ShipmentEntity> routes = shipmentService.findByOrderId(orderId);
        for (ShipmentEntity route : routes) {
            Collection<ShippingContainerEntity> containers = findByRouteId(route.getId());
            if (null == containers) containers = new ArrayList<>();
            if (containers.size() == 0) {
                for (int i = 0; i < route.getContainerQty(); i++) {
                    ShippingContainerEntity container = new ShippingContainerEntity();
                    container.setRoute(route);
                    container.setCreateTime(new Date());
                    container.setUpdateTime(new Date());
                    containers.add(container);
                }
                repository.save(containers);
            }
        }
    }

    public ShippingContainerEntity find(long scId) {
        return repository.findOne(scId);
    }

    @Transactional("transactionManager")
    public void allocate(long routeId, ShippingContainerEntity formBean) {
        ShippingContainerEntity oldOne = find(formBean.getId());
        formBean.setRoute(oldOne.getRoute());

        if (null != oldOne.getContainer()) {
            oldOne.getContainer().setStatus(ContainerStatus.IN_STOCK.getValue());
            containerService.save(oldOne.getContainer());
        }

        ContainerEntity containerEntity = containerService.find(formBean.getContainerId());
        formBean.setContainer(containerEntity);
        repository.save(formBean);

        containerEntity.setStatus(ContainerStatus.ALLOCATED.getValue());
        containerService.save(containerEntity);
    }

    @Transactional("transactionManager")
    public void remove(long scId) {
        ShippingContainerEntity sc = repository.findOne(scId);
        ContainerEntity containerEntity = sc.getContainer();
        repository.delete(scId);

        if (null != containerEntity) {
            containerEntity.setStatus(ContainerStatus.IN_STOCK.getValue());
            containerService.save(containerEntity);
        }
    }

    public Iterable<RailContainerEntity> initializeRailContainers(Long orderId) {
        OrderEntity order = orderService.find(orderId);
        Collection<RailContainerEntity> rcList = rcRepository.findByOrder(order);
        if (rcList.size() > 0) {
            return rcList;
        }

        rcList = new ArrayList<RailContainerEntity>();
        Iterable<ShipmentEntity> routes = shipmentService.findByOrderId(order.getId());
        for (ShipmentEntity route : routes) {
            List<LegEntity> legList = legRepository.findByShipmentAndTransMode(route, TransMode.RAIL.getType());
            if (legList.size() > 0) {
                List<ShippingContainerEntity> shippingContainers = findByRouteId(route.getId());
                for (ShippingContainerEntity sc : shippingContainers) {
                    RailContainerEntity rc = new RailContainerEntity();
                    rc.setOrder(route.getOrder());
                    rc.setRoute(route);
                    rc.setLeg(legList.get(0));
                    rc.setSc(sc);
                    rcList.add(rc);
                }
            }
        }

        return rcRepository.save(rcList);
    }

    public Truck findTruckDto(long railContainerId) {
        RailContainerEntity railContainer = rcRepository.findOne(railContainerId);
        return toTruckDto(railContainer);
    }

    public RailYard findRailYardDto(long railContainerId) {
        RailContainerEntity railContainer = rcRepository.findOne(railContainerId);
        return toRailYardDto(railContainer);
    }

    public RailPlan findRailPlanDto(long railContainerId) {
        RailContainerEntity railContainer = rcRepository.findOne(railContainerId);
        return toRailPlanDto(railContainer);
    }

    public RailShipping findRailShippingDto(long railContainerId) {
        RailContainerEntity railContainer = rcRepository.findOne(railContainerId);
        return toRailShippingDto(railContainer);
    }

    public RailArrival findRailArrivalDto(long railContainerId) {
        RailContainerEntity railContainer = rcRepository.findOne(railContainerId);
        return toRailArrivalDto(railContainer);
    }

    private Truck toTruckDto(RailContainerEntity railContainer) {
        Truck truck = new Truck();
        if (null != railContainer.getFleet())
            truck.setFleetId(railContainer.getFleet().getId());
        truck.setRailContainerId(railContainer.getId());
        if (null != railContainer.getSc().getContainer())
            truck.setBicCode(railContainer.getSc().getContainer().getBicCode());
        else
            truck.setBicCode(railContainer.getSc().getBicCode());
        truck.setPlateNo(railContainer.getPlateNo());
        truck.setTrucker(railContainer.getTrucker());
        if (null == railContainer.getLoadingToc()) {
            truck.setLoadingToc(DateUtil.stringOf(new Date()));
        } else {
            truck.setLoadingToc(DateUtil.stringOf(railContainer.getLoadingToc()));
        }
        return truck;
    }

    private RailYard toRailYardDto(RailContainerEntity railContainer) {
        RailYard railYard = new RailYard();
        railYard.setRailContainerId(railContainer.getId());
        if (null != railContainer.getSc().getContainer())
            railYard.setBicCode(railContainer.getSc().getContainer().getBicCode());
        else
            railYard.setBicCode(railContainer.getSc().getBicCode());
        if (null == railContainer.getStationToa()) {
            railYard.setRailYardToa(DateUtil.stringOf(new Date()));
        } else {
            railYard.setRailYardToa(DateUtil.stringOf(railContainer.getStationToa()));
        }
        return railYard;
    }

    private RailPlan toRailPlanDto(RailContainerEntity railContainer) {
        RailPlan railPlan = new RailPlan();
        railPlan.setRailContainerId(railContainer.getId());
        if (null != railContainer.getSc().getContainer())
            railPlan.setBicCode(railContainer.getSc().getContainer().getBicCode());
        else
            railPlan.setBicCode(railContainer.getSc().getBicCode());
        railPlan.setPlanNo(railContainer.getTransPlanNo());
        if (null == railContainer.getEts()) {
            railPlan.setEts(DateUtil.dayStrOf(new Date()));
        } else {
            railPlan.setEts(DateUtil.dayStrOf(railContainer.getEts()));
        }
        return railPlan;
    }

    private RailShipping toRailShippingDto(RailContainerEntity railContainer) {
        RailShipping railShipping = new RailShipping();
        railShipping.setRailContainerId(railContainer.getId());
        if (null != railContainer.getSc().getContainer())
            railShipping.setBicCode(railContainer.getSc().getContainer().getBicCode());
        else
            railShipping.setBicCode(railContainer.getSc().getBicCode());
        if (null == railContainer.getAts()) {
            railShipping.setAts(DateUtil.stringOf(new Date()));
        } else {
            railShipping.setAts(DateUtil.stringOf(railContainer.getAts()));
        }
        return railShipping;
    }

    private RailArrival toRailArrivalDto(RailContainerEntity railContainer) {
        RailArrival railArrivalDto = new RailArrival();
        railArrivalDto.setRailContainerId(railContainer.getId());
        if (null != railContainer.getSc().getContainer())
            railArrivalDto.setBicCode(railContainer.getSc().getContainer().getBicCode());
        else
            railArrivalDto.setBicCode(railContainer.getSc().getBicCode());
        if (null == railContainer.getAta()) {
            railArrivalDto.setAta(DateUtil.stringOf(new Date()));
        } else {
            railArrivalDto.setAta(DateUtil.stringOf(railContainer.getAta()));
        }
        return railArrivalDto;
    }

    public void saveTruck(Truck truck) {
        RailContainerEntity container = rcRepository.findOne(truck.getRailContainerId());
        ServiceProviderEntity fleet = serviceProviderService.find(truck.getFleetId());

        if (truck.isBatch()) {
            Collection<RailContainerEntity> containers = rcRepository.findByRoute(container.getRoute());
            for (RailContainerEntity railContainer : containers) {
                railContainer.setFleet(fleet);
                railContainer.setPlateNo(truck.getPlateNo());
                railContainer.setTrucker(truck.getTrucker());
                railContainer.setLoadingToc(DateUtil.dateOf(truck.getLoadingToc()));
            }

            rcRepository.save(containers);
        } else {
            container.setFleet(fleet);
            container.setPlateNo(truck.getPlateNo());
            container.setTrucker(truck.getTrucker());
            container.setLoadingToc(DateUtil.dateOf(truck.getLoadingToc()));
            rcRepository.save(container);
        }
    }

    public void saveRailYard(RailYard railYard) {
        RailContainerEntity container = rcRepository.findOne(railYard.getRailContainerId());

        if (railYard.isBatch()) {
            Collection<RailContainerEntity> containers = rcRepository.findByRoute(container.getRoute());
            for (RailContainerEntity railContainer : containers) {
                railContainer.setStationToa(DateUtil.dateOf(railYard.getRailYardToa()));
            }

            rcRepository.save(containers);
        } else {
            container.setStationToa(DateUtil.dateOf(railYard.getRailYardToa()));
            rcRepository.save(container);
        }
    }

    public void saveRailPlan(RailPlan railPlan) {
        RailContainerEntity container = rcRepository.findOne(railPlan.getRailContainerId());

        if (railPlan.isBatch()) {
            Collection<RailContainerEntity> containers = rcRepository.findByRoute(container.getRoute());
            for (RailContainerEntity railContainer : containers) {
                railContainer.setTransPlanNo(railPlan.getPlanNo());
                railContainer.setEts(DateUtil.dayOf(railPlan.getEts()));
            }

            rcRepository.save(containers);
        } else {
            container.setTransPlanNo(railPlan.getPlanNo());
            container.setEts(DateUtil.dayOf(railPlan.getEts()));
            rcRepository.save(container);
        }
    }

    public void saveRailShipping(RailShipping railShipping) {
        RailContainerEntity container = rcRepository.findOne(railShipping.getRailContainerId());

        if (railShipping.isBatch()) {
            Collection<RailContainerEntity> containers = rcRepository.findByRoute(container.getRoute());
            for (RailContainerEntity railContainer : containers) {
                railContainer.setAts(DateUtil.dateOf(railShipping.getAts()));
            }

            rcRepository.save(containers);
        } else {
            container.setAts(DateUtil.dateOf(railShipping.getAts()));
            rcRepository.save(container);
        }
    }

    public void saveRailArrival(RailArrival railArrival) {
        RailContainerEntity container = rcRepository.findOne(railArrival.getRailContainerId());

        if (railArrival.isBatch()) {
            Collection<RailContainerEntity> containers = rcRepository.findByRoute(container.getRoute());
            for (RailContainerEntity railContainer : containers) {
                railContainer.setAta(DateUtil.dateOf(railArrival.getAta()));
            }

            rcRepository.save(containers);
        } else {
            container.setAta(DateUtil.dateOf(railArrival.getAta()));
            rcRepository.save(container);
        }
    }

    public Iterable<BargeContainerEntity> initBargeContainers(Long orderId) {
        OrderEntity order = orderService.find(orderId);
        Collection<BargeContainerEntity> bcList = bcRepository.findByOrder(order);
        if (bcList.size() > 0) {
            for (WaterContainerEntity container : bcList) {
                if (container.getEts() != null) {
                    container.setEtsStr(DateUtil.dayStrOf(container.getEts()));
                }
            }
            return bcList;
        }

        bcList = new ArrayList<BargeContainerEntity>();
        Iterable<ShipmentEntity> routes = shipmentService.findByOrderId(order.getId());
        for (ShipmentEntity route : routes) {
            List<LegEntity> legList = legRepository.findByShipmentAndTransMode(route, TransMode.BARGE.getType());
            if (legList.size() > 0) {
                List<ShippingContainerEntity> shippingContainers = route.getContainers();
                for (ShippingContainerEntity sc : shippingContainers) {
                    BargeContainerEntity bc = new BargeContainerEntity();
                    bc.setOrder(route.getOrder());
                    bc.setRoute(route);
                    bc.setLeg(legList.get(0));
                    bc.setSc(sc);
                    bcList.add(bc);
                }
            }
        }

        return bcRepository.save(bcList);
    }

    public BargeContainerEntity findBargeContainer(long containerId) {
        BargeContainerEntity bargeContainer = bcRepository.findOne(containerId);

        String defaultDayStr = DateUtil.dayStrOf(new Date());
        if (null == bargeContainer.getEts())
            bargeContainer.setEtsStr(defaultDayStr);
        else
            bargeContainer.setEtsStr(DateUtil.dayStrOf(bargeContainer.getEts()));

        return bargeContainer;
    }

    public void saveBargeContainer(long containerId, BargeContainerEntity formBean) {
        BargeContainerEntity container = bcRepository.findOne(containerId);
        ShipmentEntity route = shipmentService.find(container.getRoute().getId());

        if (formBean.isBatch()) {

            Collection<BargeContainerEntity> containers = bcRepository.findByRouteId(route.getId());

            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                for (WaterContainerEntity rc : containers) {
                    rc.setEts(DateUtil.dayOf(formBean.getEtsStr()));
                }
            }

            if (formBean.getBolNo() != null && formBean.getBolNo().trim().length() > 0) {
                for (WaterContainerEntity rc : containers) {
                    rc.setBolNo(formBean.getBolNo());
                }
            }

            if (formBean.getSlot() != null && formBean.getSlot().trim().length() > 0) {
                for (WaterContainerEntity rc : containers) {
                    rc.setSlot(formBean.getSlot());
                }
            }

            bcRepository.save(containers);
        } else {
            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                container.setEts(DateUtil.dayOf(formBean.getEtsStr()));
            }
            if (formBean.getBolNo() != null && formBean.getBolNo().trim().length() > 0) {
                container.setBolNo(formBean.getBolNo());
            }

            if (formBean.getSlot() != null && formBean.getSlot().trim().length() > 0) {
                container.setSlot(formBean.getSlot());
            }
            bcRepository.save(container);
        }
    }

    public Iterable<VesselContainerEntity> initVesselContainers(Long orderId) {
        OrderEntity order = orderService.find(orderId);
        Collection<VesselContainerEntity> vcList = vcRepository.findByOrder(order);
        if (vcList.size() > 0) {
            for (VesselContainerEntity container : vcList) {
                if (container.getEts() != null) {
                    container.setEtsStr(DateUtil.dayStrOf(container.getEts()));
                }
            }
            return vcList;
        }

        vcList = new ArrayList<VesselContainerEntity>();
        Iterable<ShipmentEntity> routes = shipmentService.findByOrderId(order.getId());
        for (ShipmentEntity route : routes) {
            List<LegEntity> legList = legRepository.findByShipmentAndTransMode(route, TransMode.VESSEL.getType());
            if (legList.size() > 0) {
                List<ShippingContainerEntity> shippingContainers = findByRouteId(route.getId());
                for (ShippingContainerEntity sc : shippingContainers) {
                    VesselContainerEntity vc = new VesselContainerEntity();
                    vc.setOrder(route.getOrder());
                    vc.setRoute(route);
                    vc.setLeg(legList.get(0));
                    vc.setSc(sc);
                    vcList.add(vc);
                }
            }
        }

        return vcRepository.save(vcList);
    }

    public VesselContainerEntity findVesselContainer(long containerId) {
        VesselContainerEntity vesselContainer = vcRepository.findOne(containerId);

        String defaultDayStr = DateUtil.dayStrOf(new Date());
        if (null == vesselContainer.getEts())
            vesselContainer.setEtsStr(defaultDayStr);
        else
            vesselContainer.setEtsStr(DateUtil.dayStrOf(vesselContainer.getEts()));

        return vesselContainer;
    }

    public void saveVesselContainer(long containerId, VesselContainerEntity formBean) {
        VesselContainerEntity container = vcRepository.findOne(containerId);
        ShipmentEntity route = shipmentService.find(container.getRoute().getId());

        if (formBean.isBatch()) {
            Collection<VesselContainerEntity> containers = vcRepository.findByRouteId(route.getId());

            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                for (VesselContainerEntity rc : containers) {
                    rc.setEts(DateUtil.dayOf(formBean.getEtsStr()));
                }
            }

            if (formBean.getBolNo() != null && formBean.getBolNo().trim().length() > 0) {
                for (VesselContainerEntity rc : containers) {
                    rc.setBolNo(formBean.getBolNo());
                }
            }

            if (formBean.getSlot() != null && formBean.getSlot().trim().length() > 0) {
                for (VesselContainerEntity rc : containers) {
                    rc.setSlot(formBean.getSlot());
                }
            }

            vcRepository.save(containers);
        } else {
            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                container.setEts(DateUtil.dayOf(formBean.getEtsStr()));
            }
            if (formBean.getBolNo() != null && formBean.getBolNo().trim().length() > 0) {
                container.setBolNo(formBean.getBolNo());
            }

            if (formBean.getSlot() != null && formBean.getSlot().trim().length() > 0) {
                container.setSlot(formBean.getSlot());
            }
            vcRepository.save(container);
        }
    }
}
