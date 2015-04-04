package liquid.process.handler;

import liquid.order.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.process.service.BusinessKey;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO: Comments.
 * User: tao
 * Date: 11/12/13
 * Time: 10:36 PM
 */
@Component
public class ProcessEndHandler implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(ProcessEndHandler.class);

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKeyText = execution.getProcessBusinessKey();
        BusinessKey businessKey = BusinessKey.decode(businessKeyText);
        OrderEntity order = orderService.complete(businessKey.getOrderId());
    }
}
