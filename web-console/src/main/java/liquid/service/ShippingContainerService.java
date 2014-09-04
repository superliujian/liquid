package liquid.service;

import liquid.container.domain.ContainerStatus;
import liquid.container.persistence.domain.ContainerEntity;
import liquid.container.service.ContainerService;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.shipping.domain.*;
import liquid.shipping.persistence.domain.*;
import liquid.shipping.persistence.repository.*;
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
    private TaskService taskService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ContainerService containerService;

    @Autowired
    private ShippingContainerRepository scRepository;

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

    public Iterable<RouteEntity> findByOrderId(Long orderId) {
        return routeService.findByOrderId(orderId);
    }

    @Transactional("transactionManager")
    public void initialize(String taskId) {
        Long orderId = taskService.findOrderId(taskId);
        Iterable<RouteEntity> routes = routeService.findByOrderId(orderId);
        for (RouteEntity route : routes) {
            Collection<ShippingContainerEntity> containers = route.getContainers();
            if (null == containers) containers = new ArrayList<>();
            if (containers.size() == 0) {
                for (int i = 0; i < route.getContainerQty(); i++) {
                    ShippingContainerEntity container = new ShippingContainerEntity();
                    container.setRoute(route);
                    container.setCreateTime(new Date());
                    container.setUpdateTime(new Date());
                    containers.add(container);
                }
                scRepository.save(containers);
            }
        }
    }

    public ShippingContainerEntity find(long scId) {
        return scRepository.findOne(scId);
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
        scRepository.save(formBean);

        containerEntity.setStatus(ContainerStatus.ALLOCATED.getValue());
        containerService.save(containerEntity);
    }

    @Transactional("transactionManager")
    public void remove(long scId) {
        ShippingContainerEntity sc = scRepository.findOne(scId);
        ContainerEntity containerEntity = sc.getContainer();
        scRepository.delete(scId);

        if (null != containerEntity) {
            containerEntity.setStatus(ContainerStatus.IN_STOCK.getValue());
            containerService.save(containerEntity);
        }
    }

    public List<ShippingContainerEntity> findByRoute(RouteEntity route) {
        return scRepository.findByRoute(route);
    }

    public Iterable<RailContainerEntity> initializeRailContainers(String taskId) {
        OrderEntity order = taskService.findOrderByTaskId(taskId);
        Collection<RailContainerEntity> rcList = rcRepository.findByOrder(order);
        if (rcList.size() > 0) {
            return rcList;
        }

        rcList = new ArrayList<RailContainerEntity>();
        Iterable<RouteEntity> routes = routeService.findByOrderId(order.getId());
        for (RouteEntity route : routes) {
            Collection<ShippingContainerEntity> scList = scRepository.findByRoute(route);
            List<LegEntity> legList = legRepository.findByRouteAndTransMode(route, TransMode.RAIL.getType());
            if (legList.size() > 0) {
                for (ShippingContainerEntity sc : scList) {
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

    public TruckDto findTruckDto(long railContainerId) {
        RailContainerEntity railContainer = rcRepository.findOne(railContainerId);
        return toTruckDto(railContainer);
    }

    public RailYardDto findRailYardDto(long railContainerId) {
        RailContainerEntity railContainer = rcRepository.findOne(railContainerId);
        return toRailYardDto(railContainer);
    }

    public RailPlanDto findRailPlanDto(long railContainerId) {
        RailContainerEntity railContainer = rcRepository.findOne(railContainerId);
        return toRailPlanDto(railContainer);
    }

    public RailShippingDto findRailShippingDto(long railContainerId) {
        RailContainerEntity railContainer = rcRepository.findOne(railContainerId);
        return toRailShippingDto(railContainer);
    }

    public RailArrivalDto findRailArrivalDto(long railContainerId) {
        RailContainerEntity railContainer = rcRepository.findOne(railContainerId);
        return toRailArrivalDto(railContainer);
    }

    private TruckDto toTruckDto(RailContainerEntity railContainer) {
        TruckDto truck = new TruckDto();
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

    private RailYardDto toRailYardDto(RailContainerEntity railContainer) {
        RailYardDto railYard = new RailYardDto();
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

    private RailPlanDto toRailPlanDto(RailContainerEntity railContainer) {
        RailPlanDto railPlan = new RailPlanDto();
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

    private RailShippingDto toRailShippingDto(RailContainerEntity railContainer) {
        RailShippingDto railShipping = new RailShippingDto();
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

    private RailArrivalDto toRailArrivalDto(RailContainerEntity railContainer) {
        RailArrivalDto railArrivalDto = new RailArrivalDto();
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

    public void saveTruck(TruckDto truck) {
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

    public void saveRailYard(RailYardDto railYard) {
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

    public void saveRailPlan(RailPlanDto railPlan) {
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

    public void saveRailShipping(RailShippingDto railShipping) {
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

    public void saveRailArrival(RailArrivalDto railArrival) {
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

    public Iterable<BargeContainer> initBargeContainers(String taskId) {
        OrderEntity order = taskService.findOrderByTaskId(taskId);
        Collection<BargeContainer> bcList = bcRepository.findByOrder(order);
        if (bcList.size() > 0) {
            for (BargeContainer container : bcList) {
                if (container.getEts() != null) {
                    container.setEtsStr(DateUtil.dayStrOf(container.getEts()));
                }
            }
            return bcList;
        }

        bcList = new ArrayList<BargeContainer>();
        Iterable<RouteEntity> routes = routeService.findByOrderId(order.getId());
        for (RouteEntity route : routes) {
            Collection<ShippingContainerEntity> scList = scRepository.findByRoute(route);
            List<LegEntity> legList = legRepository.findByRouteAndTransMode(route, TransMode.BARGE.getType());
            if (legList.size() > 0) {
                for (ShippingContainerEntity sc : scList) {
                    BargeContainer bc = new BargeContainer();
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

    public BargeContainer findBargeContainer(long containerId) {
        BargeContainer bargeContainer = bcRepository.findOne(containerId);

        String defaultDayStr = DateUtil.dayStrOf(new Date());
        if (null == bargeContainer.getEts())
            bargeContainer.setEtsStr(defaultDayStr);
        else
            bargeContainer.setEtsStr(DateUtil.dayStrOf(bargeContainer.getEts()));

        return bargeContainer;
    }

    public void saveBargeContainer(long containerId, BargeContainer formBean) {
        BargeContainer container = bcRepository.findOne(containerId);

        if (formBean.isBatch()) {
            Collection<BargeContainer> containers = bcRepository.findByRoute(container.getRoute());

            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                for (BargeContainer rc : containers) {
                    rc.setEts(DateUtil.dayOf(formBean.getEtsStr()));
                }
            }

            if (formBean.getBolNo() != null && formBean.getBolNo().trim().length() > 0) {
                for (BargeContainer rc : containers) {
                    rc.setBolNo(formBean.getBolNo());
                }
            }

            if (formBean.getSlot() != null && formBean.getSlot().trim().length() > 0) {
                for (BargeContainer rc : containers) {
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

    public Iterable<VesselContainer> initVesselContainers(String taskId) {
        OrderEntity order = taskService.findOrderByTaskId(taskId);
        Collection<VesselContainer> vcList = vcRepository.findByOrder(order);
        if (vcList.size() > 0) {
            for (VesselContainer container : vcList) {
                if (container.getEts() != null) {
                    container.setEtsStr(DateUtil.dayStrOf(container.getEts()));
                }
            }
            return vcList;
        }

        vcList = new ArrayList<VesselContainer>();
        Iterable<RouteEntity> routes = routeService.findByOrderId(order.getId());
        for (RouteEntity route : routes) {
            Collection<ShippingContainerEntity> scList = scRepository.findByRoute(route);
            List<LegEntity> legList = legRepository.findByRouteAndTransMode(route, TransMode.VESSEL.getType());
            if (legList.size() > 0) {
                for (ShippingContainerEntity sc : scList) {
                    VesselContainer vc = new VesselContainer();
                    vc.setOrder(route.getOrder());
                    vc.setRoute(route);
                    vc.setLeg(legList.get(0));
                    vc.setSc(sc);
                    vcList.add(vc);
                }
            }
        }

        return bcRepository.save(vcList);
    }

    public VesselContainer findVesselContainer(long containerId) {
        VesselContainer vesselContainer = vcRepository.findOne(containerId);

        String defaultDayStr = DateUtil.dayStrOf(new Date());
        if (null == vesselContainer.getEts())
            vesselContainer.setEtsStr(defaultDayStr);
        else
            vesselContainer.setEtsStr(DateUtil.dayStrOf(vesselContainer.getEts()));

        return vesselContainer;
    }

    public void saveVesselContainer(long containerId, VesselContainer formBean) {
        VesselContainer container = vcRepository.findOne(containerId);

        if (formBean.isBatch()) {
            Collection<VesselContainer> containers = vcRepository.findByRoute(container.getRoute());

            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                for (VesselContainer rc : containers) {
                    rc.setEts(DateUtil.dayOf(formBean.getEtsStr()));
                }
            }

            if (formBean.getBolNo() != null && formBean.getBolNo().trim().length() > 0) {
                for (VesselContainer rc : containers) {
                    rc.setBolNo(formBean.getBolNo());
                }
            }

            if (formBean.getSlot() != null && formBean.getSlot().trim().length() > 0) {
                for (VesselContainer rc : containers) {
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
