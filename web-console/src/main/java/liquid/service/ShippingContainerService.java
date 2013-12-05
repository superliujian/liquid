package liquid.service;

import liquid.dto.*;
import liquid.metadata.ContainerStatus;
import liquid.metadata.TransMode;
import liquid.persistence.domain.*;
import liquid.persistence.repository.*;
import liquid.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 12:36 AM
 */
@Service
public class ShippingContainerService {
    @Autowired
    private OrderService orderService;

    @Autowired
    private PlanningService planningService;

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
    private SpService spService;

    @Transactional("transactionManager")
    public void initialize(String taskId) {
        Planning planning = planningService.findByTaskId(taskId);
        Collection<Route> routes = routeService.findByPlanning(planning);
        for (Route route : routes) {
            Collection<ShippingContainer> containers = route.getContainers();
            if (null == containers) containers = new ArrayList<>();
            if (containers.size() == 0) {
                for (int i = 0; i < route.getContainerQty(); i++) {
                    ShippingContainer container = new ShippingContainer();
                    container.setRoute(route);
                    container.setCreateTime(new Date());
                    container.setUpdateTime(new Date());
                    containers.add(container);
                }
                scRepository.save(containers);
            }
        }
    }

    public ShippingContainer find(long scId) {
        return scRepository.findOne(scId);
    }

    @Transactional("transactionManager")
    public void allocate(long routeId, ShippingContainer formBean) {
        ShippingContainer oldOne = find(formBean.getId());
        formBean.setRoute(oldOne.getRoute());

        if (null != oldOne.getContainer()) {
            oldOne.getContainer().setStatus(ContainerStatus.IN_STOCK.getValue());
            containerService.save(oldOne.getContainer());
        }

        Container container = containerService.find(formBean.getContainerId());
        formBean.setContainer(container);
        scRepository.save(formBean);

        container.setStatus(ContainerStatus.ALLOCATED.getValue());
        containerService.save(container);
    }

    @Transactional("transactionManager")
    public void remove(long scId) {
        ShippingContainer sc = scRepository.findOne(scId);
        Container container = sc.getContainer();
        scRepository.delete(scId);

        if (null != container) {
            container.setStatus(ContainerStatus.IN_STOCK.getValue());
            containerService.save(container);
        }
    }

    public Collection<ShippingContainer> findByRoute(Route route) {
        return scRepository.findByRoute(route);
    }

    public Iterable<RailContainer> initializeRailContainers(String taskId) {
        Order order = orderService.findByTaskId(taskId);
        Collection<RailContainer> rcList = rcRepository.findByOrder(order);
        if (rcList.size() > 0) {
            for (RailContainer container : rcList) {
                if (container.getLoadingToc() != null) {
                    container.setLoadingTocStr(DateUtils.stringOf(container.getLoadingToc()));
                }
                if (container.getStationToa() != null) {
                    container.setStationToaStr(DateUtils.stringOf(container.getStationToa()));
                }
                if (container.getEts() != null) {
                    container.setEtsStr(DateUtils.dayStrOf(container.getEts()));
                }
                if (container.getAts() != null) {
                    container.setAtsStr(DateUtils.stringOf(container.getAts()));
                }
                if (container.getAta() != null) {
                    container.setAtaStr(DateUtils.stringOf(container.getAta()));
                }
            }
            return rcList;
        }

        rcList = new ArrayList<RailContainer>();
        Collection<Route> routes = routeService.findByTaskId(taskId);
        for (Route route : routes) {
            Collection<ShippingContainer> scList = scRepository.findByRoute(route);
            List<Leg> legList = legRepository.findByRouteAndTransMode(route, TransMode.RAIL.getType());
            if (legList.size() > 0) {
                for (ShippingContainer sc : scList) {
                    RailContainer rc = new RailContainer();
                    rc.setOrder(route.getPlanning().getOrder());
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
        RailContainer railContainer = rcRepository.findOne(railContainerId);
        return toTruckDto(railContainer);
    }

    public RailYardDto findRailYardDto(long railContainerId) {
        RailContainer railContainer = rcRepository.findOne(railContainerId);
        return toRailYardDto(railContainer);
    }

    public RailPlanDto findRailPlanDto(long railContainerId) {
        RailContainer railContainer = rcRepository.findOne(railContainerId);
        return toRailPlanDto(railContainer);
    }

    public RailShippingDto findRailShippingDto(long railContainerId) {
        RailContainer railContainer = rcRepository.findOne(railContainerId);
        return toRailShippingDto(railContainer);
    }

    public RailArrivalDto findRailArrivalDto(long railContainerId) {
        RailContainer railContainer = rcRepository.findOne(railContainerId);
        return toRailArrivalDto(railContainer);
    }

    private TruckDto toTruckDto(RailContainer railContainer) {
        TruckDto truck = new TruckDto();
        if (null != railContainer.getFleet())
            truck.setFleetId(railContainer.getFleet().getId());
        truck.setRailContainerId(railContainer.getId());
        if (null != railContainer.getSc().getContainer())
            truck.setBicCode(railContainer.getSc().getContainer().getBicCode());
        truck.setPlateNo(railContainer.getPlateNo());
        truck.setTrucker(railContainer.getTrucker());
        if (null == railContainer.getLoadingToc()) {
            truck.setLoadingToc(DateUtils.stringOf(new Date()));
        } else {
            truck.setLoadingToc(DateUtils.stringOf(railContainer.getLoadingToc()));
        }
        return truck;
    }

    private RailYardDto toRailYardDto(RailContainer railContainer) {
        RailYardDto railYard = new RailYardDto();
        railYard.setRailContainerId(railContainer.getId());
        if (null != railContainer.getSc().getContainer())
            railYard.setBicCode(railContainer.getSc().getContainer().getBicCode());
        if (null == railContainer.getStationToa()) {
            railYard.setRailYardToa(DateUtils.stringOf(new Date()));
        } else {
            railYard.setRailYardToa(DateUtils.stringOf(railContainer.getStationToa()));
        }
        return railYard;
    }

    private RailPlanDto toRailPlanDto(RailContainer railContainer) {
        RailPlanDto railPlan = new RailPlanDto();
        railPlan.setRailContainerId(railContainer.getId());
        if (null != railContainer.getSc().getContainer())
            railPlan.setBicCode(railContainer.getSc().getContainer().getBicCode());
        railPlan.setPlanNo(railContainer.getTransPlanNo());
        if (null == railContainer.getEts()) {
            railPlan.setEts(DateUtils.dayStrOf(new Date()));
        } else {
            railPlan.setEts(DateUtils.dayStrOf(railContainer.getEts()));
        }
        return railPlan;
    }

    private RailShippingDto toRailShippingDto(RailContainer railContainer) {
        RailShippingDto railShipping = new RailShippingDto();
        railShipping.setRailContainerId(railContainer.getId());
        if (null != railContainer.getSc().getContainer())
            railShipping.setBicCode(railContainer.getSc().getContainer().getBicCode());
        if (null == railContainer.getAts()) {
            railShipping.setAts(DateUtils.stringOf(new Date()));
        } else {
            railShipping.setAts(DateUtils.stringOf(railContainer.getAts()));
        }
        return railShipping;
    }

    private RailArrivalDto toRailArrivalDto(RailContainer railContainer) {
        RailArrivalDto railArrivalDto = new RailArrivalDto();
        railArrivalDto.setRailContainerId(railContainer.getId());
        if (null != railContainer.getSc().getContainer())
            railArrivalDto.setBicCode(railContainer.getSc().getContainer().getBicCode());
        if (null == railContainer.getAta()) {
            railArrivalDto.setAta(DateUtils.stringOf(new Date()));
        } else {
            railArrivalDto.setAta(DateUtils.stringOf(railContainer.getAta()));
        }
        return railArrivalDto;
    }

    public void saveTruck(TruckDto truck) {
        RailContainer container = rcRepository.findOne(truck.getRailContainerId());
        ServiceProvider fleet = spService.find(truck.getFleetId());

        if (truck.isBatch()) {
            Collection<RailContainer> containers = rcRepository.findByOrder(container.getOrder());
            for (RailContainer railContainer : containers) {
                railContainer.setFleet(fleet);
                railContainer.setPlateNo(truck.getPlateNo());
                railContainer.setTrucker(truck.getTrucker());
                railContainer.setLoadingToc(DateUtils.dateOf(truck.getLoadingToc()));
            }

            rcRepository.save(containers);
        } else {
            container.setFleet(fleet);
            container.setPlateNo(truck.getPlateNo());
            container.setTrucker(truck.getTrucker());
            container.setLoadingToc(DateUtils.dateOf(truck.getLoadingToc()));
            rcRepository.save(container);
        }
    }

    public void saveRailYard(RailYardDto railYard) {
        RailContainer container = rcRepository.findOne(railYard.getRailContainerId());

        if (railYard.isBatch()) {
            Collection<RailContainer> containers = rcRepository.findByOrder(container.getOrder());
            for (RailContainer railContainer : containers) {
                railContainer.setStationToa(DateUtils.dateOf(railYard.getRailYardToa()));
            }

            rcRepository.save(containers);
        } else {
            container.setStationToa(DateUtils.dateOf(railYard.getRailYardToa()));
            rcRepository.save(container);
        }
    }

    public void saveRailPlan(RailPlanDto railPlan) {
        RailContainer container = rcRepository.findOne(railPlan.getRailContainerId());

        if (railPlan.isBatch()) {
            Collection<RailContainer> containers = rcRepository.findByRoute(container.getRoute());
            for (RailContainer railContainer : containers) {
                railContainer.setTransPlanNo(railPlan.getPlanNo());
                railContainer.setEts(DateUtils.dayOf(railPlan.getEts()));
            }

            rcRepository.save(containers);
        } else {
            container.setTransPlanNo(railPlan.getPlanNo());
            container.setEts(DateUtils.dayOf(railPlan.getEts()));
            rcRepository.save(container);
        }
    }

    public void saveRailShipping(RailShippingDto railShipping) {
        RailContainer container = rcRepository.findOne(railShipping.getRailContainerId());

        if (railShipping.isBatch()) {
            Collection<RailContainer> containers = rcRepository.findByOrder(container.getOrder());
            for (RailContainer railContainer : containers) {
                railContainer.setAts(DateUtils.dateOf(railShipping.getAts()));
            }

            rcRepository.save(containers);
        } else {
            container.setAts(DateUtils.dateOf(railShipping.getAts()));
            rcRepository.save(container);
        }
    }

    public void saveRailArrival(RailArrivalDto railArrival) {
        RailContainer container = rcRepository.findOne(railArrival.getRailContainerId());

        if (railArrival.isBatch()) {
            Collection<RailContainer> containers = rcRepository.findByOrder(container.getOrder());
            for (RailContainer railContainer : containers) {
                railContainer.setAta(DateUtils.dateOf(railArrival.getAta()));
            }

            rcRepository.save(containers);
        } else {
            container.setAta(DateUtils.dateOf(railArrival.getAta()));
            rcRepository.save(container);
        }
    }

    public RailContainer findRailContainer(long containerId) {
        RailContainer railContainer = rcRepository.findOne(containerId);
        String defaultDateStr = DateUtils.stringOf(new Date());
        if (null == railContainer.getLoadingToc())
            railContainer.setLoadingTocStr(defaultDateStr);
        else
            railContainer.setLoadingTocStr(DateUtils.stringOf(railContainer.getLoadingToc()));

        if (null == railContainer.getStationToa())
            railContainer.setStationToaStr(defaultDateStr);
        else
            railContainer.setStationToaStr(DateUtils.stringOf(railContainer.getStationToa()));

        String defaultDayStr = DateUtils.dayStrOf(new Date());
        if (null == railContainer.getEts())
            railContainer.setEtsStr(defaultDayStr);
        else
            railContainer.setEtsStr(DateUtils.dayStrOf(railContainer.getEts()));

        if (null == railContainer.getAts())
            railContainer.setAtsStr(defaultDateStr);
        else
            railContainer.setAtsStr(DateUtils.stringOf(railContainer.getAts()));

        if (null == railContainer.getAta())
            railContainer.setAtaStr(defaultDateStr);
        else
            railContainer.setAtaStr(DateUtils.stringOf(railContainer.getAta()));
        return railContainer;
    }

    public void saveRailContainer(long containerId, RailContainer formBean) {
        RailContainer container = rcRepository.findOne(containerId);

        if (formBean.isBatch()) {
            Collection<RailContainer> containers = rcRepository.findByOrder(container.getOrder());
            if (formBean.getLoadingTocStr() != null && formBean.getLoadingTocStr().trim().length() > 0) {
                for (RailContainer rc : containers) {
                    rc.setLoadingToc(DateUtils.dateOf(formBean.getLoadingTocStr()));
                }
            }

            if (formBean.getStationToaStr() != null && formBean.getStationToaStr().trim().length() > 0) {
                for (RailContainer rc : containers) {
                    rc.setStationToa(DateUtils.dateOf(formBean.getStationToaStr()));
                }
            }

            if (formBean.getTransPlanNo() != null && formBean.getTransPlanNo().trim().length() > 0) {
                for (RailContainer rc : containers) {
                    rc.setTransPlanNo(formBean.getTransPlanNo());
                }
            }

            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                for (RailContainer rc : containers) {
                    rc.setEts(DateUtils.dayOf(formBean.getEtsStr()));
                }
            }

            if (formBean.getAtsStr() != null && formBean.getAtsStr().trim().length() > 0) {
                for (RailContainer rc : containers) {
                    rc.setAts(DateUtils.dateOf(formBean.getAtsStr()));
                }
            }

            if (formBean.getAtaStr() != null && formBean.getAtaStr().trim().length() > 0) {
                for (RailContainer rc : containers) {
                    rc.setAta(DateUtils.dateOf(formBean.getAtaStr()));
                }
            }

            rcRepository.save(containers);
        } else {
            if (formBean.getLoadingTocStr() != null && formBean.getLoadingTocStr().trim().length() > 0) {
                container.setLoadingToc(DateUtils.dateOf(formBean.getLoadingTocStr()));
            }
            if (formBean.getStationToaStr() != null && formBean.getStationToaStr().trim().length() > 0) {
                container.setStationToa(DateUtils.dateOf(formBean.getStationToaStr()));
            }
            if (formBean.getTransPlanNo() != null && formBean.getTransPlanNo().trim().length() > 0) {
                container.setTransPlanNo(formBean.getTransPlanNo());
            }
            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                container.setEts(DateUtils.dayOf(formBean.getEtsStr()));
            }
            if (formBean.getAtsStr() != null && formBean.getAtsStr().trim().length() > 0) {
                container.setAts(DateUtils.dateOf(formBean.getAtsStr()));
            }
            if (formBean.getAtaStr() != null && formBean.getAtaStr().trim().length() > 0) {
                container.setAta(DateUtils.dateOf(formBean.getAtaStr()));
            }
            rcRepository.save(container);
        }
    }

    public Iterable<BargeContainer> initBargeContainers(String taskId) {
        Order order = orderService.findByTaskId(taskId);
        Collection<BargeContainer> bcList = bcRepository.findByOrder(order);
        if (bcList.size() > 0) {
            for (BargeContainer container : bcList) {
                if (container.getEts() != null) {
                    container.setEtsStr(DateUtils.dayStrOf(container.getEts()));
                }
            }
            return bcList;
        }

        bcList = new ArrayList<BargeContainer>();
        Collection<Route> routes = routeService.findByTaskId(taskId);
        for (Route route : routes) {
            Collection<ShippingContainer> scList = scRepository.findByRoute(route);
            List<Leg> legList = legRepository.findByRouteAndTransMode(route, TransMode.BARGE.getType());
            if (legList.size() > 0) {
                for (ShippingContainer sc : scList) {
                    BargeContainer bc = new BargeContainer();
                    bc.setOrder(route.getPlanning().getOrder());
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

        String defaultDayStr = DateUtils.dayStrOf(new Date());
        if (null == bargeContainer.getEts())
            bargeContainer.setEtsStr(defaultDayStr);
        else
            bargeContainer.setEtsStr(DateUtils.dayStrOf(bargeContainer.getEts()));

        return bargeContainer;
    }

    public void saveBargeContainer(long containerId, BargeContainer formBean) {
        BargeContainer container = bcRepository.findOne(containerId);

        if (formBean.isBatch()) {
            Collection<BargeContainer> containers = bcRepository.findByOrder(container.getOrder());

            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                for (BargeContainer rc : containers) {
                    rc.setEts(DateUtils.dayOf(formBean.getEtsStr()));
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
                container.setEts(DateUtils.dayOf(formBean.getEtsStr()));
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
        Order order = orderService.findByTaskId(taskId);
        Collection<VesselContainer> vcList = vcRepository.findByOrder(order);
        if (vcList.size() > 0) {
            for (VesselContainer container : vcList) {
                if (container.getEts() != null) {
                    container.setEtsStr(DateUtils.dayStrOf(container.getEts()));
                }
            }
            return vcList;
        }

        vcList = new ArrayList<VesselContainer>();
        Collection<Route> routes = routeService.findByTaskId(taskId);
        for (Route route : routes) {
            Collection<ShippingContainer> scList = scRepository.findByRoute(route);
            List<Leg> legList = legRepository.findByRouteAndTransMode(route, TransMode.VESSEL.getType());
            if (legList.size() > 0) {
                for (ShippingContainer sc : scList) {
                    VesselContainer bc = new VesselContainer();
                    bc.setOrder(route.getPlanning().getOrder());
                    bc.setLeg(legList.get(0));
                    bc.setSc(sc);
                    vcList.add(bc);
                }
            }
        }

        return bcRepository.save(vcList);
    }

    public VesselContainer findVesselContainer(long containerId) {
        VesselContainer vesselContainer = vcRepository.findOne(containerId);

        String defaultDayStr = DateUtils.dayStrOf(new Date());
        if (null == vesselContainer.getEts())
            vesselContainer.setEtsStr(defaultDayStr);
        else
            vesselContainer.setEtsStr(DateUtils.dayStrOf(vesselContainer.getEts()));

        return vesselContainer;
    }

    public void saveVesselContainer(long containerId, VesselContainer formBean) {
        VesselContainer container = vcRepository.findOne(containerId);

        if (formBean.isBatch()) {
            Collection<VesselContainer> containers = vcRepository.findByOrder(container.getOrder());

            if (formBean.getEtsStr() != null && formBean.getEtsStr().trim().length() > 0) {
                for (VesselContainer rc : containers) {
                    rc.setEts(DateUtils.dayOf(formBean.getEtsStr()));
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
                container.setEts(DateUtils.dayOf(formBean.getEtsStr()));
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

    public Iterable<DeliveryContainer> initDeliveryContainers(String taskId) {
        Order order = orderService.findByTaskId(taskId);
        Collection<DeliveryContainer> dcList = dcRepository.findByOrder(order);
        if (dcList.size() > 0) {
            for (DeliveryContainer container : dcList) {
                if (container.getEtd() != null) {
                    container.setEtdStr(DateUtils.dayStrOf(container.getEtd()));
                }
            }
            return dcList;
        }

        dcList = new ArrayList<DeliveryContainer>();
        Collection<Route> routes = routeService.findByTaskId(taskId);
        for (Route route : routes) {
            Collection<ShippingContainer> scList = scRepository.findByRoute(route);
            for (ShippingContainer sc : scList) {
                DeliveryContainer dc = new DeliveryContainer();
                dc.setOrder(route.getPlanning().getOrder());
                dc.setSc(sc);
                dc.setAddress(order.getConsigneeAddress());
                dcList.add(dc);
            }
        }

        return dcRepository.save(dcList);
    }

    public DeliveryContainer findDeliveryContainer(long containerId) {
        DeliveryContainer deliveryContainer = dcRepository.findOne(containerId);

        if (null == deliveryContainer.getAddress()) {
            deliveryContainer.setAddress(deliveryContainer.getOrder().getConsigneeAddress());
        }

        String defaultDayStr = DateUtils.dayStrOf(new Date());
        if (null == deliveryContainer.getEtd())
            deliveryContainer.setEtdStr(defaultDayStr);
        else
            deliveryContainer.setEtdStr(DateUtils.dayStrOf(deliveryContainer.getEtd()));

        return deliveryContainer;
    }

    public void saveDeliveryContainer(long containerId, DeliveryContainer formBean) {
        DeliveryContainer container = dcRepository.findOne(containerId);

        if (formBean.isBatch()) {
            Collection<DeliveryContainer> containers = dcRepository.findByOrder(container.getOrder());

            if (formBean.getEtdStr() != null && formBean.getEtdStr().trim().length() > 0) {
                for (DeliveryContainer dc : containers) {
                    dc.setEtd(DateUtils.dayOf(formBean.getEtdStr()));
                }
            }

            if (formBean.getAddress() != null && formBean.getAddress().trim().length() > 0) {
                for (DeliveryContainer dc : containers) {
                    dc.setAddress(formBean.getAddress());
                }
            }

            dcRepository.save(containers);
        } else {
            if (formBean.getEtdStr() != null && formBean.getEtdStr().trim().length() > 0) {
                container.setEtd(DateUtils.dayOf(formBean.getEtdStr()));
            }
            if (formBean.getAddress() != null && formBean.getAddress().trim().length() > 0) {
                container.setAddress(formBean.getAddress());
            }
            dcRepository.save(container);
        }
    }

}
