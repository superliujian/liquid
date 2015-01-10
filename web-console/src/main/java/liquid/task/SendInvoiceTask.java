package liquid.task;

import liquid.accounting.persistence.domain.ReceivableSummaryEntity;
import liquid.accounting.service.ReceivableSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@DefinitionKey("sendInvoicing")
@Service
public class SendInvoiceTask extends AbstractTaskProxy {

    @Autowired
    private ReceivableSummaryService receivableSummaryService;

    @Override
    public void doBeforeComplete(String taskId, Map<String, Object> variableMap) {
        Long orderId = taskService.getOrderIdByTaskId(taskId);
        ReceivableSummaryEntity receivableSummaryEntity = receivableSummaryService.findByOrderId(orderId);
        variableMap.put("salesPrice", "CNY: " + receivableSummaryEntity.getCny() + "; USD: " + receivableSummaryEntity.getUsd());
    }
}
