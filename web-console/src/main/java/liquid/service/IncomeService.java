package liquid.service;

import liquid.metadata.IncomeType;
import liquid.persistence.domain.Income;
import liquid.persistence.domain.Order;
import liquid.persistence.repository.IncomeRepository;
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
    private OrderService orderService;

    public List<Income> findByTaskId(String taskId) {
        return incomeRepository.findByTaskId(taskId);
    }

    public Income addIncome(Income income, String uid) {
        income.setCreateUser(uid);
        income.setCreateTime(new Date());
        income.setUpdateUser(uid);
        income.setUpdateTime(new Date());
        return incomeRepository.save(income);
    }

    public Income addIncome(Order order, String uid) {
        Income income = new Income();
        income.setOrder(order);
        income.setType(IncomeType.ORDER.getType());
        income.setAmount(order.getSalesPriceCny());
        income.setComment("Order");
        return addIncome(income, uid);
    }

    public Income addIncome(String taskId, Income income, String uid) {
        Order order = orderService.findByTaskId(taskId);

        income.setOrder(order);
        income.setTaskId(taskId);
        return addIncome(income, uid);
    }

    public void delIncome(long id) {
        incomeRepository.delete(id);
    }

    public long total(Iterable<Income> incomes) {
        long total = 0L;
        for (Income income : incomes) {
            total += income.getAmount();
        }
        return total;
    }
}
