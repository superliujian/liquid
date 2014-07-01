package liquid.service;

import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.domain.VesselContainer;
import liquid.shipping.persistence.repository.VesselContainerRepository;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by tao on 12/14/13.
 */
@Service
public class VesselContainerService {
    @Autowired
    private VesselContainerRepository vesselContainerRepository;

    public Collection<VesselContainer> findByRoute(RouteEntity route) {
        Collection<VesselContainer> vesselContainers = vesselContainerRepository.findByRoute(route);

        for (VesselContainer vesselContainer : vesselContainers) {
            if (null != vesselContainer.getEts())
                vesselContainer.setEtsStr(DateUtil.dayStrOf(vesselContainer.getEts()));
        }
        return vesselContainers;
    }
}
