package liquid.service;

import liquid.config.JpaConfig;
import liquid.transport.persistence.repository.ShipmentRepository;
import liquid.transport.service.LegService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by mat on 9/26/14.
 */
public class LegServiceTest {
    @Test
    public void deleteLeg() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(LegService.class);
        context.register(ShipmentRepository.class);
        context.register(JpaConfig.class);
        context.refresh();

        LegService legService = context.getBean(LegService.class);
        legService.delete(131L);
    }
}
