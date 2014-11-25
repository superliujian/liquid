package liquid.persistence;

import liquid.config.JpaConfig;
import liquid.transport.persistence.domain.RouteEntity;
import liquid.transport.persistence.repository.RouteRepository;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by mat on 9/26/14.
 */
public class RouteRepositoryTest {
    @Test
    public void findOne() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JpaConfig.class);
        context.refresh();

        RouteRepository routeRepository = (RouteRepository) context.getBean(RouteRepository.class);
        RouteEntity route = routeRepository.findOne(57L);
        route.getLegs();
    }
}
