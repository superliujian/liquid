package liquid.service;

import liquid.shipping.persistence.domain.BargeContainer;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.repository.BargeContainerRepository;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by tao on 12/14/13.
 */
@Service
public class BargeContainerService {

    @Autowired
    private BargeContainerRepository bargeContainerRepository;

    public Collection<BargeContainer> findByRoute(RouteEntity route) {
        Collection<BargeContainer> bargeContainers = bargeContainerRepository.findByRoute(route);

        for (BargeContainer bargeContainer : bargeContainers) {
            if (null != bargeContainer.getEts()) bargeContainer.setEtsStr(DateUtil.dayStrOf(bargeContainer.getEts()));
        }
        return bargeContainers;
    }
}
