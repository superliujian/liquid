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

    public void apply(String taskId, String uid, Planning planning) {
        long orderId = bpmService.getOrderIdByTaskId(taskId);
        Order order = orderRepository.findOne(orderId);
        TransMode mode = TransMode.valueOf(planning.getTransMode());
        planning.setOrder(order);
        planning.generateId();

        Planning oldOne = planningRepository.findOne(planning.getId());
        oldOne.setSameRoute(planning.isSameRoute());
        planningRepository.save(oldOne);
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

    public void create(String taskId, TransRailway railway) {
        long orderId = bpmService.getOrderIdByTaskId(taskId);
        Order order = orderRepository.findOne(orderId);

        Planning planning = planningRepository.findByOrder(order);
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
        if (order.getContainerQty() == railways.size()) {
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
}
