package liquid.shipping.service;

import liquid.service.AbstractService;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.repository.RouteRepository;
import org.springframework.stereotype.Service;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 12:26 AM
 */
@Service
public class RouteService extends AbstractService<RouteEntity, RouteRepository> {

    @Override
    public void doSaveBefore(RouteEntity routeEntity) { }

    public Iterable<RouteEntity> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    public RouteEntity find(long id) {
        RouteEntity route = repository.findOne(id);
        return route;
    }
}
