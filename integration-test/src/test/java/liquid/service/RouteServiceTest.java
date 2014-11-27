package liquid.service;

import liquid.config.JpaConfig;
import liquid.persistence.domain.LocationEntity;
import liquid.transport.persistence.domain.PathEntity;
import liquid.transport.persistence.domain.RouteEntity;
import liquid.transport.persistence.repository.PathRepository;
import liquid.transport.persistence.repository.RouteRepository;
import liquid.transport.service.RouteService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Tao Ma on 11/26/14.
 */
public class RouteServiceTest {
    @Test
    public void testAddRoute() {
        RouteService routeService = getRouteService();

        RouteEntity route = new RouteEntity();
        route.setName("Test");

        routeService.save(route);
    }

    @Test
    public void testAddPath() {
        RouteService routeService = getRouteService();

        Long routeId = 1L;
        PathEntity path = new PathEntity();
        path.setFrom(LocationEntity.newInstance(LocationEntity.class, 10L));
        path.setTo(LocationEntity.newInstance(LocationEntity.class, 20L));
        path.setTransportMode(1);

        RouteEntity route = routeService.addPath(routeId, path);
    }

    @Test
    public void testFindOne() {
        RouteService routeService = getRouteService();
        routeService.findOne(1L);
    }

    private RouteService getRouteService() {
        System.setProperty("env.target", "dev");

        AuthenticationManager authenticationManager = new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                return authentication;
            }
        };

        Authentication request = new UsernamePasswordAuthenticationToken("市场", "market");
        Authentication result = authenticationManager.authenticate(request);
        SecurityContextHolder.getContext().setAuthentication(result);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(RouteRepository.class);
        context.register(PathRepository.class);
        context.register(RouteService.class);
        context.register(JpaConfig.class);
        context.refresh();

        return context.getBean(RouteService.class);
    }
}
