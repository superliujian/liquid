package liquid.shipping.service;

import liquid.service.AbstractService;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 12:26 AM
 */
@Service
public class RouteService extends AbstractService<RouteEntity, RouteRepository> {
    @Autowired
    private RouteRepository routeRepository;

    public Iterable<RouteEntity> findByOrderId(Long orderId) {
        return routeRepository.findByOrderId(orderId);
    }

    public RouteEntity find(long id) {
        RouteEntity route = routeRepository.findOne(id);
        return route;
    }

    @Override
    public void doSaveBefore(RouteEntity routeEntity) { }
}
