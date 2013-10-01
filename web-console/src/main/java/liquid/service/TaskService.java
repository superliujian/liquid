package liquid.service;

import liquid.persistence.domain.*;
import liquid.persistence.repository.OrderRepository;
import liquid.persistence.repository.PlanningRepository;
import liquid.persistence.repository.TransRailwayRepository;
import liquid.service.bpm.ActivitiEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 3:40 PM
 */
@Service
public class TaskService {
    @Autowired
    private ActivitiEngineService bpmService;

    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TransRailwayRepository transRailwayRepository;

    @Deprecated
    public void addTransportation(String taskId, String uid, int transMode, boolean sameRoute) {
        long orderId = bpmService.getOrderIdByTaskId(taskId);
        Order order = orderRepository.findOne(orderId);
        TransMode mode = TransMode.valueOf(transMode);

        Planning planning = new Planning();
        planning.setOrder(order);
        planning.setSameRoute(sameRoute);
        planning.setStatus(PlanningStatus.ADDED.getValue());
        planning.generateId();
        planningRepository.save(planning);

        switch (mode) {
            case RAILWAY:
                TransRailway transRailway = new TransRailway();
                transRailway.setOrder(order);
                transRailway.setTaskId(taskId);
                transRailwayRepository.save(transRailway);
                break;
            default:
                break;
        }
    }
}
