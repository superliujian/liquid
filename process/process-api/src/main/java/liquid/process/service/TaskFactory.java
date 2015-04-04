package liquid.process.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 6/7/14.
 */
@Service
public class TaskFactory {
    @Autowired
    private ApplicationContext context;

    public AbstractTaskProxy findTask(String definitionKey) {
        AbstractTaskProxy task = context.containsBean(definitionKey)
                ? context.getBean(definitionKey, AbstractTaskProxy.class)
                : context.getBean("common", AbstractTaskProxy.class);
        return task;
    }
}
