package liquid.service;

import liquid.persistence.domain.Leg;
import liquid.persistence.domain.Planning;
import liquid.persistence.domain.Route;
import liquid.persistence.domain.ShippingContainer;
import liquid.persistence.repository.LegRepository;
import liquid.persistence.repository.RouteRepository;
import liquid.persistence.repository.ShippingContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 12:26 AM
 */
@Service
public class RouteService {

    @Autowired
    private PlanningService planningService;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private LegRepository legRepository;

    @Autowired
    private ShippingContainerRepository scRepository;

    public Collection<Route> findByTaskId(String taskId) {
        Planning planning = planningService.findByTaskId(taskId);
        Collection<Route> routes = planning.getRoutes();
        for (Route route : routes) {
            Collection<Leg> legs = legRepository.findByRoute(route);
            route.setLegs(legs);
            Collection<ShippingContainer> containers = scRepository.findByRoute(route);
            route.setContainers(containers);
        }
        return routes;
    }

    @Transactional("transactionManager")
    public Route save(Route formBean, Planning planning) {
        formBean.setPlanning(planning);
        formBean.setDeliveryAddress(planning.getOrder().getLoadingAddress());
        Route route = routeRepository.save(formBean);
        return route;
    }
}
