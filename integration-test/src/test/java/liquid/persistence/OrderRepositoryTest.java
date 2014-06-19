package liquid.persistence;

import liquid.config.JpaConfig;
import liquid.persistence.domain.OrderEntity;
import liquid.persistence.domain.ServiceItemEntity;
import liquid.persistence.repository.OrderRepository;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by redbrick9 on 6/1/14.
 */
public class OrderRepositoryTest {
    @Test
    public void addOrder() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.refresh();

        OrderRepository orderRepository = context.getBean(OrderRepository.class);
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCnyTotal(1000L);

        ServiceItemEntity serviceItemEntity = new ServiceItemEntity();
        serviceItemEntity.setOrder(orderEntity);
        serviceItemEntity.setQuotation(200L);
        orderEntity.getServiceItems().add(serviceItemEntity);

        orderRepository.save(orderEntity);
    }
}
