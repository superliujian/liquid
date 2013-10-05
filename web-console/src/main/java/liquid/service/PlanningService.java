package liquid.service;

import liquid.persistence.domain.*;
import liquid.persistence.repository.*;
import liquid.service.bpm.ActivitiEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 9:03 PM
 */
@Service
public class PlanningService {
    private static final Logger logger = LoggerFactory.getLogger(PlanningService.class);

    @Autowired
    private ActivitiEngineService bpmService;

    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TransRailwayRepository transRailwayRepository;

    @Autowired
    private TransBargeRepository transBargeRepository;

    @Autowired
    private TransVesselRepository transVesselRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SpRepository spRepository;

    public void editPlanning(String taskId, Planning planning) {
        long orderId = bpmService.getOrderIdByTaskId(taskId);
        Order order = orderRepository.findOne(orderId);
        Planning oldOne = planningRepository.findByOrderAndTransMode(order, planning.getTransMode());
        oldOne.setSameRoute(planning.isSameRoute());
        planningRepository.save(oldOne);
    }

    public Planning deletePlanning(String taskId, String transMode) {
        long orderId = bpmService.getOrderIdByTaskId(taskId);
        Order order = orderRepository.findOne(orderId);

        Planning planning = planningRepository.findByOrderAndTransMode(order,
                TransMode.valueOf(transMode.toUpperCase()).getType());
        TransMode mode = TransMode.valueOf(transMode.toUpperCase());
        switch (mode) {
            case RAILWAY:
                List<TransRailway> transRailways = transRailwayRepository.findByPlanning(planning);
                for (TransRailway transRailway : transRailways) {
                    transRailwayRepository.delete(transRailway);
                }
                break;
            case BARGE:
                List<TransBarge> transBarges = transBargeRepository.findByPlanning(planning);
                for (TransBarge transBarge : transBarges) {
                    transBargeRepository.delete(transBarge);
                }
                break;
            case VESSEL:
                List<TransVessel> transVessels = transVesselRepository.findByPlanning(planning);
                for (TransVessel transVessel : transVessels) {
                    transVesselRepository.delete(transVessel);
                }
                break;
            default:
                break;
        }

        planningRepository.delete(planning);
        return planning;
    }

    public void createRailway(String taskId, TransRailway railway) {
        long orderId = bpmService.getOrderIdByTaskId(taskId);
        Order order = orderRepository.findOne(orderId);

        Planning planning = planningRepository.findByOrderAndTransMode(order, TransMode.RAILWAY.getType());
        Location srcLoc = locationRepository.findOne(Long.valueOf(railway.getOrigination()));
        Location dstLoc = locationRepository.findOne(Long.valueOf(railway.getDestination()));
        if (planning.isSameRoute()) {
            List<TransRailway> railways = transRailwayRepository.findByPlanning(planning);
            for (int i = 0; i < (order.getContainerQty() - railways.size()); i++) {
                TransRailway transRailway = new TransRailway();
                transRailway.setOrder(order);
                transRailway.setTaskId(taskId);
                transRailway.setPlanning(planning);
                transRailway.setSrcLoc(srcLoc);
                transRailway.setDstLoc(dstLoc);
                transRailwayRepository.save(transRailway);
            }
        } else {
            TransRailway transRailway = new TransRailway();
            transRailway.setOrder(order);
            transRailway.setTaskId(taskId);
            transRailway.setPlanning(planning);
            transRailway.setSrcLoc(srcLoc);
            transRailway.setDstLoc(dstLoc);
            transRailwayRepository.save(transRailway);
        }

        List<TransRailway> railways = transRailwayRepository.findByPlanning(planning);
        if (order.getContainerQty() <= railways.size()) {
            planning.setStatus(PlanningStatus.FULL.getValue());
            planningRepository.save(planning);
        }
    }

    public void createBarge(String taskId, TransBarge barge) {
        long orderId = bpmService.getOrderIdByTaskId(taskId);
        Order order = orderRepository.findOne(orderId);

        Planning planning = planningRepository.findByOrderAndTransMode(order, TransMode.BARGE.getType());
        Location srcLoc = locationRepository.findOne(Long.valueOf(barge.getOrigination()));
        Location dstLoc = locationRepository.findOne(Long.valueOf(barge.getDestination()));
        ServiceProvider sp = spRepository.findOne(Long.valueOf(barge.getSpId()));

        if (planning.isSameRoute()) {
            List<TransBarge> barges = transBargeRepository.findByPlanning(planning);

            for (int i = 0; i < (order.getContainerQty() - barges.size()); i++) {
                TransBarge transBarge = new TransBarge();
                transBarge.setOrder(order);
                transBarge.setTaskId(taskId);
                transBarge.setPlanning(planning);
                transBarge.setSrcLoc(srcLoc);
                transBarge.setDstLoc(dstLoc);
                transBarge.setSp(sp);
                transBargeRepository.save(transBarge);
            }
        } else {
            TransBarge transBarge = new TransBarge();
            transBarge.setOrder(order);
            transBarge.setTaskId(taskId);
            transBarge.setPlanning(planning);
            transBarge.setSrcLoc(srcLoc);
            transBarge.setDstLoc(dstLoc);
            transBarge.setSp(sp);
            transBargeRepository.save(transBarge);
        }

        List<TransBarge> barges = transBargeRepository.findByPlanning(planning);
        if (order.getContainerQty() <= barges.size()) {
            planning.setStatus(PlanningStatus.FULL.getValue());
            planningRepository.save(planning);
        }
    }

    public void createVessel(String taskId, TransVessel vessel) {
        long orderId = bpmService.getOrderIdByTaskId(taskId);
        Order order = orderRepository.findOne(orderId);

        Planning planning = planningRepository.findByOrderAndTransMode(order, TransMode.VESSEL.getType());
        Location srcLoc = locationRepository.findOne(Long.valueOf(vessel.getOrigination()));
        Location dstLoc = locationRepository.findOne(Long.valueOf(vessel.getDestination()));
        ServiceProvider sp = spRepository.findOne(Long.valueOf(vessel.getSpId()));

        if (planning.isSameRoute()) {
            List<TransVessel> vessels = transVesselRepository.findByPlanning(planning);

            for (int i = 0; i < (order.getContainerQty() - vessels.size()); i++) {
                TransVessel transVessel = new TransVessel();
                transVessel.setOrder(order);
                transVessel.setTaskId(taskId);
                transVessel.setPlanning(planning);
                transVessel.setSrcLoc(srcLoc);
                transVessel.setDstLoc(dstLoc);
                transVessel.setSp(sp);
                transVesselRepository.save(transVessel);
            }
        } else {
            TransVessel transVessel = new TransVessel();
            transVessel.setOrder(order);
            transVessel.setTaskId(taskId);
            transVessel.setPlanning(planning);
            transVessel.setSrcLoc(srcLoc);
            transVessel.setDstLoc(dstLoc);
            transVessel.setSp(sp);
            transVesselRepository.save(transVessel);
        }

        List<TransVessel> vessels = transVesselRepository.findByPlanning(planning);
        if (order.getContainerQty() <= vessels.size()) {
            planning.setStatus(PlanningStatus.FULL.getValue());
            planningRepository.save(planning);
        }
    }

    public TransRailway editRailway(TransRailway railway) {
        TransRailway oldOne = transRailwayRepository.findOne(railway.getId());

        long orderId = bpmService.getOrderIdByTaskId(oldOne.getTaskId());
        Order order = orderRepository.findOne(orderId);

        Location srcLoc = locationRepository.findOne(Long.valueOf(railway.getOrigination()));
        Location dstLoc = locationRepository.findOne(Long.valueOf(railway.getDestination()));

        if (oldOne.getPlanning().isSameRoute()) {
            List<TransRailway> railways = transRailwayRepository.findByPlanning(oldOne.getPlanning());
            for (TransRailway transRailway : railways) {
                transRailway.setSrcLoc(srcLoc);
                transRailway.setDstLoc(dstLoc);
            }
            if (order.getContainerQty() > railways.size()) {
                for (int i = 0; i < (order.getContainerQty() - railways.size()); i++) {
                    TransRailway transRailway = new TransRailway();
                    transRailway.setOrder(order);
                    transRailway.setTaskId(oldOne.getTaskId());
                    transRailway.setPlanning(oldOne.getPlanning());
                    transRailway.setSrcLoc(srcLoc);
                    transRailway.setDstLoc(dstLoc);
                    railways.add(transRailway);
                }
            }
            transRailwayRepository.save(railways);
        } else {
            oldOne.setSrcLoc(srcLoc);
            oldOne.setDstLoc(dstLoc);
            transRailwayRepository.save(oldOne);
        }

        return oldOne;
    }

    public TransBarge editBarge(TransBarge barge) {
        TransBarge oldOne = transBargeRepository.findOne(barge.getId());

        long orderId = bpmService.getOrderIdByTaskId(oldOne.getTaskId());
        Order order = orderRepository.findOne(orderId);

        Location srcLoc = locationRepository.findOne(Long.valueOf(barge.getOrigination()));
        Location dstLoc = locationRepository.findOne(Long.valueOf(barge.getDestination()));
        ServiceProvider sp = spRepository.findOne(Long.valueOf(barge.getSpId()));

        if (oldOne.getPlanning().isSameRoute()) {
            List<TransBarge> transBarges = transBargeRepository.findByPlanning(oldOne.getPlanning());
            for (TransBarge transBarge : transBarges) {
                transBarge.setSrcLoc(srcLoc);
                transBarge.setDstLoc(dstLoc);
                transBarge.setSp(sp);
            }
            if (order.getContainerQty() > transBarges.size()) {
                for (int i = 0; i < (order.getContainerQty() - transBarges.size()); i++) {
                    TransBarge transBarge = new TransBarge();
                    transBarge.setOrder(order);
                    transBarge.setTaskId(oldOne.getTaskId());
                    transBarge.setPlanning(oldOne.getPlanning());
                    transBarge.setSrcLoc(srcLoc);
                    transBarge.setDstLoc(dstLoc);
                    transBarge.setSp(sp);
                    transBarges.add(transBarge);
                }
            }
            transBargeRepository.save(transBarges);
        } else {
            oldOne.setSrcLoc(srcLoc);
            oldOne.setDstLoc(dstLoc);
            oldOne.setSp(sp);
            transBargeRepository.save(oldOne);
        }

        return oldOne;
    }

    public TransVessel editVessel(TransVessel vessel) {
        TransVessel oldOne = transVesselRepository.findOne(vessel.getId());

        long orderId = bpmService.getOrderIdByTaskId(oldOne.getTaskId());
        Order order = orderRepository.findOne(orderId);

        Location srcLoc = locationRepository.findOne(Long.valueOf(vessel.getOrigination()));
        Location dstLoc = locationRepository.findOne(Long.valueOf(vessel.getDestination()));
        ServiceProvider sp = spRepository.findOne(Long.valueOf(vessel.getSpId()));

        if (oldOne.getPlanning().isSameRoute()) {
            List<TransVessel> transVessels = transVesselRepository.findByPlanning(oldOne.getPlanning());
            for (TransVessel transVessel : transVessels) {
                transVessel.setSrcLoc(srcLoc);
                transVessel.setDstLoc(dstLoc);
                transVessel.setSp(sp);
            }
            if (order.getContainerQty() > transVessels.size()) {
                for (int i = 0; i < (order.getContainerQty() - transVessels.size()); i++) {
                    TransVessel transVessel = new TransVessel();
                    transVessel.setOrder(order);
                    transVessel.setTaskId(oldOne.getTaskId());
                    transVessel.setPlanning(oldOne.getPlanning());
                    transVessel.setSrcLoc(srcLoc);
                    transVessel.setDstLoc(dstLoc);
                    transVessel.setSp(sp);
                    transVessels.add(transVessel);
                }
            }
            transVesselRepository.save(transVessels);
        } else {
            oldOne.setSrcLoc(srcLoc);
            oldOne.setDstLoc(dstLoc);
            oldOne.setSp(sp);
            transVesselRepository.save(oldOne);
        }

        return oldOne;
    }

    @Transactional("transactionManager")
    public Map<String, Object> getTransTypes(String taskId) {
        Map<String, Object> transTypes = new HashMap<String, Object>();
        transTypes.put("hasRailway", false);
        transTypes.put("hasBarge", false);
        transTypes.put("hasVessel", false);

        long orderId = bpmService.getOrderIdByTaskId(taskId);
        Order order = orderRepository.findOne(orderId);
        Collection<Planning> plannings = planningRepository.findByOrder(order);
        logger.debug("planning size: {}", plannings.size());
        for (Planning planning : plannings) {
            TransMode mode = TransMode.valueOf(planning.getTransMode());
            switch (mode) {
                case RAILWAY:
                    transTypes.put("hasRailway", true);
                    break;
                case BARGE:
                    transTypes.put("hasBarge", true);
                    break;
                case VESSEL:
                    transTypes.put("hasVessel", true);
                    break;
                default:
                    logger.warn("{} transportation mode is illegal.");
                    break;
            }
        }
        logger.debug("The order has the transportation {}.", transTypes);
        return transTypes;
    }
}
