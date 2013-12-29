package liquid.service;

import liquid.persistence.domain.Route;
import liquid.persistence.domain.VesselContainer;
import liquid.persistence.repository.VesselContainerRepository;
import liquid.utils.DateUtils;
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

    public Collection<VesselContainer> findByRoute(Route route) {
        Collection<VesselContainer> vesselContainers = vesselContainerRepository.findByRoute(route);

        for (VesselContainer vesselContainer : vesselContainers) {
            if (null != vesselContainer.getEts())
                vesselContainer.setEtsStr(DateUtils.dayStrOf(vesselContainer.getEts()));
        }
        return vesselContainers;
    }
}