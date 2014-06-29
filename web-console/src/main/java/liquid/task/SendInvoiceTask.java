package liquid.task;

import liquid.persistence.domain.OrderEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@DefinitionKey("sendInvoicing")
@Service
public class SendInvoiceTask extends AbstractTaskProxy {
    @Override
    public void doBeforeComplete(String taskId, Map<String, Object> variableMap) {
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        variableMap.put("salesPrice", "CNY: " + order.getCnyTotal() + "; USD: " + order.getUsdTotal());
    }
}
