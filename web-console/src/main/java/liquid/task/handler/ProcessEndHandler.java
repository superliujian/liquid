package liquid.task.handler;

import liquid.order.service.OrderService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
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
public class ProcessEndHandler implements ExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(ProcessEndHandler.class);

    @Autowired
    private OrderService orderService;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        String businessKey = delegateExecution.getProcessBusinessKey();
        long orderId = Long.parseLong(businessKey);
    }
}
