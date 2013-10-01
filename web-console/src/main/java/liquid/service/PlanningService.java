package liquid.service;

import liquid.persistence.domain.*;
import liquid.persistence.repository.OrderRepository;
import liquid.persistence.repository.PlanningRepository;
import liquid.persistence.repository.TransRailwayRepository;
import liquid.service.bpm.ActivitiEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 9:03 PM
 */
@Service
public class PlanningService {
    @Autowired
    private ActivitiEngineService bpmService;

    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TransRailwayRepository transRailwayRepository;

    public void create(String taskId, String uid, Planning planning) {
        long orderId = bpmService.getOrderIdByTaskId(taskId);
        Order order = orderRepository.findOne(orderId);
        TransMode mode = TransMode.valueOf(planning.getTransMode());

        planning.setOrder(order);
        planning.setStatus(PlanningStatus.ADDED.getValue());
        planning.generateId();
        planningRepository.save(planning);

        switch (mode) {
            case RAILWAY:
                for (int i = 0; i < order.getContainerQty(); i++) {
                    TransRailway transRailway = new TransRailway();
                    transRailway.setOrder(order);
                    transRailway.setTaskId(taskId);
                    transRailwayRepository.save(transRailway);
                }
                break;
            default:
                break;
        }
    }

    public Planning delete(String planningId) {
        Planning planning = planningRepository.findOne(planningId);

        List<TransRailway> transRailways = transRailwayRepository.findByPlanning(planning);
        for (TransRailway transRailway : transRailways) {
            transRailwayRepository.delete(transRailway);
        }
        planningRepository.delete(planning);
        return planning;
    }
}
