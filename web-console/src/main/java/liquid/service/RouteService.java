package liquid.service;

import liquid.persistence.domain.Leg;
import liquid.persistence.domain.Planning;
import liquid.persistence.domain.Route;
import liquid.persistence.domain.ShippingContainer;
import liquid.persistence.repository.LegRepository;
import liquid.persistence.repository.RouteRepository;
import liquid.persistence.repository.ShippingContainerRepository;
import liquid.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    public Collection<Route> findByPlanning(Planning planning) {
        if (planning == null) {
            return Collections.EMPTY_LIST;
        }
        Collection<Route> routes = planning.getRoutes();
        for (Route route : routes) {
            Collection<Leg> legs = legRepository.findByRoute(route);
            route.setLegs(legs);
            Collection<ShippingContainer> containers = scRepository.findByRoute(route);
            route.setContainers(containers);
        }
        return routes;
    }

    public Route find(long id) {
        Route route = routeRepository.findOne(id);
        return route;
    }

    /**
     * Update for delivery date.
     *
     * @param formBean
     * @return
     */
    @Transactional("transactionManager")
    public void save(Route formBean) {
        Route oldOne = routeRepository.findOne(formBean.getId());

        if (formBean.isBatch()) {
            Collection<Route> routes = routeRepository.findByPlanning(oldOne.getPlanning());
            routeRepository.save(routes);
        } else {
            routeRepository.save(oldOne);
        }
    }

    @Transactional("transactionManager")
    public Route save(Route formBean, Planning planning) {
        formBean.setPlanning(planning);
        Route route = routeRepository.save(formBean);
        return route;
    }
}
