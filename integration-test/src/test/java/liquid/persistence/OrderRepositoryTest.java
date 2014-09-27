package liquid.persistence;

import liquid.config.JpaConfig;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.persistence.domain.ServiceItemEntity;
import liquid.order.persistence.repository.OrderRepository;
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

        ServiceItemEntity serviceItemEntity = new ServiceItemEntity();
        serviceItemEntity.setOrder(orderEntity);
        serviceItemEntity.setQuotation(200L);
        orderEntity.getServiceItems().add(serviceItemEntity);

        orderRepository.save(orderEntity);
    }
}
