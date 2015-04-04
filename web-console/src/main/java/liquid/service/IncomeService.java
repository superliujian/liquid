package liquid.service;

import liquid.accounting.persistence.domain.IncomeEntity;
import liquid.accounting.persistence.domain.ReceivableSummaryEntity;
import liquid.accounting.persistence.repository.IncomeRepository;
import liquid.accounting.service.ReceivableSummaryService;
import liquid.metadata.IncomeType;
import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderServiceImpl;
import liquid.process.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/30/13
 * Time: 1:02 PM
 */
@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ReceivableSummaryService receivableSummaryService;

    @Autowired
    private OrderServiceImpl orderService;

    public List<IncomeEntity> findByTaskId(String taskId) {
        return incomeRepository.findByTaskId(taskId);
    }

    public IncomeEntity addIncome(IncomeEntity income, String uid) {
        income.setCreatedBy(uid);
        income.setCreatedAt(new Date());
        income.setUpdatedBy(uid);
        income.setUpdatedAt(new Date());
        return incomeRepository.save(income);
    }

    public IncomeEntity addIncome(OrderEntity order, String uid) {
        ReceivableSummaryEntity receivableSummaryEntity = receivableSummaryService.findByOrderId(order.getId());

        IncomeEntity income = new IncomeEntity();
        income.setOrder(order);
        income.setType(IncomeType.ORDER.getType());
        income.setAmount(receivableSummaryEntity.getCny().longValue());
        income.setComment("Order");
        return addIncome(income, uid);
    }

    public IncomeEntity addIncome(String taskId, IncomeEntity income, String uid) {
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);

        income.setOrder(order);
        income.setTaskId(taskId);
        return addIncome(income, uid);
    }

    public void delIncome(long id) {
        incomeRepository.delete(id);
    }

    public long total(Iterable<IncomeEntity> incomes) {
        long total = 0L;
        for (IncomeEntity income : incomes) {
            total += income.getAmount();
        }
        return total;
    }
}
