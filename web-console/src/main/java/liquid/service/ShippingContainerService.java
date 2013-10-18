package liquid.service;

import liquid.dto.TruckDto;
import liquid.metadata.ContainerStatus;
import liquid.metadata.TransMode;
import liquid.persistence.domain.*;
import liquid.persistence.repository.*;
import liquid.utils.DateUtils;
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
public class ShippingContainerService {
    @Autowired
    private OrderService orderService;

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
    private LegRepository legRepository;

    @Transactional("transactionManager")
    public void add(long routeId, ShippingContainer formBean) {
        Route route = routeService.find(routeId);
        formBean.setRoute(route);
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

        container.setStatus(ContainerStatus.IN_STOCK.getValue());
        containerService.save(container);
    }

    public Iterable<RailContainer> initialize(String taskId) {
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

    private TruckDto toTruckDto(RailContainer railContainer) {
        TruckDto truck = new TruckDto();
        truck.setRailContainerId(railContainer.getId());
        truck.setBicCode(railContainer.getSc().getContainer().getBicCode());
        truck.setPlateNo(railContainer.getPlateNo());
        truck.setTrucker(railContainer.getTrucker());
        return truck;
    }

    public void saveTruck(TruckDto truck) {
        RailContainer container = rcRepository.findOne(truck.getRailContainerId());

        if (truck.isBatch()) {
            Collection<RailContainer> containers = rcRepository.findByOrder(container.getOrder());
            for (RailContainer railContainer : containers) {
                railContainer.setPlateNo(truck.getPlateNo());
                railContainer.setTrucker(truck.getTrucker());
            }

            rcRepository.save(containers);
        } else {
            container.setPlateNo(truck.getPlateNo());
            container.setTrucker(truck.getTrucker());
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
}
