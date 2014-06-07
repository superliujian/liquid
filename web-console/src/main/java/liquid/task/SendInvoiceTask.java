package liquid.task;

import liquid.persistence.domain.OrderEntity;

import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@DefinitionKey("sendInvoicing")
public class SendInvoiceTask extends AbstractTaskProxy {
    @Override
    public void doBeforeComplete(String taskId, Map<String, Object> variableMap) {
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);
        variableMap.put("salesPrice", order.getSalesPriceCny());
    }
}
