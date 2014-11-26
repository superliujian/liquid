package liquid.persistence;

import liquid.config.JpaConfig;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.persistence.repository.ShipmentRepository;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by mat on 9/26/14.
 */
public class ShipmentRepositoryTest {
    @Test
    public void findOne() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.refresh();

        ShipmentRepository shipmentRepository = (ShipmentRepository) context.getBean(ShipmentRepository.class);
        ShipmentEntity shipment = shipmentRepository.findOne(57L);
        shipment.getLegs();
    }
}
