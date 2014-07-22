package liquid.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by redbrick9 on 6/7/14.
 */
@Service
public class TaskFactory {
    @Autowired
    private ApplicationContext context;

    private final Object lock = new Object();

    private Class<? extends AbstractTaskProxy>[] classes = new Class[]{
            AllocationContainerTask.class, FeedContainerNoTask.class, PlanRouteTask.class, SendInvoiceTask.class
    };
    private Map<String, AbstractTaskProxy> cache = new HashMap<>();

    public AbstractTaskProxy findTask(String definitionKey) {
        AbstractTaskProxy task = cache.get(definitionKey);
        if (null != task) return task;

        synchronized (lock) {
            task = cache.get(definitionKey);
            if (null != task) return task;

            for (Class<? extends AbstractTaskProxy> clazz : classes) {
                DefinitionKey definitionKeyAnnotation = clazz.getAnnotation(DefinitionKey.class);
                if (definitionKey.equals(definitionKeyAnnotation.value())) {
                    task = context.getBean(clazz);
                    break;
                }
            }
            if (null == task) {
                task = context.getBean(CommonTask.class);
            }
            cache.put(definitionKey, task);
        }
        return task;
    }
}
