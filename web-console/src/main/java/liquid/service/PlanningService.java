package liquid.service;

import liquid.persistence.domain.*;
import liquid.persistence.repository.OrderRepository;
import liquid.persistence.repository.PlanningRepository;
import liquid.persistence.repository.TransRailwayRepository;
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

        List<TransRailway> transRailways = transRailwayRepository.findByPlanning(planning);
        for (TransRailway transRailway : transRailways) {
            transRailwayRepository.delete(transRailway);
        }
        planningRepository.delete(planning);
        return planning;
    }

    public void create(String taskId, TransRailway railway) {
        long orderId = bpmService.getOrderIdByTaskId(taskId);
        Order order = orderRepository.findOne(orderId);

        Planning planning = planningRepository.findByOrderAndTransMode(order, TransMode.RAILWAY.getType());
        if (planning.isSameRoute()) {
            for (int i = 0; i < order.getContainerQty(); i++) {
                TransRailway transRailway = new TransRailway();
                transRailway.setOrder(order);
                transRailway.setTaskId(taskId);
                transRailway.setPlanning(planning);
                transRailway.setOrigination(railway.getOrigination());
                transRailway.setDestination(railway.getDestination());
                transRailwayRepository.save(transRailway);
            }
        } else {
            TransRailway transRailway = new TransRailway();
            transRailway.setOrder(order);
            transRailway.setTaskId(taskId);
            transRailway.setPlanning(planning);
            transRailway.setOrigination(railway.getOrigination());
            transRailway.setDestination(railway.getDestination());
            transRailwayRepository.save(transRailway);
        }

        List<TransRailway> railways = transRailwayRepository.findByPlanning(planning);
        if (order.getContainerQty() <= railways.size()) {
            planning.setStatus(PlanningStatus.FULL.getValue());
            planningRepository.save(planning);
        }
    }

    public TransRailway edit(TransRailway railway) {
        TransRailway oldOne = transRailwayRepository.findOne(railway.getId());

        long orderId = bpmService.getOrderIdByTaskId(oldOne.getTaskId());
        Order order = orderRepository.findOne(orderId);

        if (oldOne.getPlanning().isSameRoute()) {
            List<TransRailway> railways = transRailwayRepository.findByPlanning(oldOne.getPlanning());
            for (TransRailway transRailway : railways) {
                transRailway.setOrigination(railway.getOrigination());
                transRailway.setDestination(railway.getDestination());
            }
            if (order.getContainerQty() > railways.size()) {
                for (int i = 0; i < (order.getContainerQty() - railways.size()); i++) {
                    TransRailway transRailway = new TransRailway();
                    transRailway.setOrder(order);
                    transRailway.setTaskId(oldOne.getTaskId());
                    transRailway.setPlanning(oldOne.getPlanning());
                    transRailway.setOrigination(railway.getOrigination());
                    transRailway.setDestination(railway.getDestination());
                    railways.add(transRailway);
                }
            }
            transRailwayRepository.save(railways);
        } else {
            oldOne.setOrigination(railway.getOrigination());
            oldOne.setDestination(railway.getDestination());
            transRailwayRepository.save(oldOne);
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
