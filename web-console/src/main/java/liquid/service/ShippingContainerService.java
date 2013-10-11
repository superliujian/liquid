package liquid.service;

import liquid.persistence.domain.Container;
import liquid.persistence.domain.Route;
import liquid.persistence.domain.ShippingContainer;
import liquid.persistence.repository.ContainerRepository;
import liquid.persistence.repository.RouteRepository;
import liquid.persistence.repository.ShippingContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 12:36 AM
 */
@Service
public class ShippingContainerService {

    @Autowired
    private ShippingContainerRepository scRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ContainerRepository containerRepository;

    public void add(long routeId, ShippingContainer sc) {
        Route route = routeRepository.findOne(routeId);
        sc.setRoute(route);
        Container container = containerRepository.findOne(sc.getContainerId());
        sc.setContainer(container);
        scRepository.save(sc);
    }
}
