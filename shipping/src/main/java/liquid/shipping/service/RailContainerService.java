package liquid.shipping.service;

import liquid.shipping.persistence.domain.RailContainerEntity;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.repository.RailContainerRepository;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by tao on 12/11/13.
 */
@Service
public class RailContainerService {
    @Autowired
    private RailContainerRepository railContainerRepository;

    public Collection<RailContainerEntity> findByRoute(RouteEntity route) {
        Collection<RailContainerEntity> railContainers = railContainerRepository.findByRoute(route);
        for (RailContainerEntity railContainer : railContainers) {
            if (railContainer.getStationToa() != null) {
                railContainer.setStationToaStr(DateUtil.stringOf(railContainer.getStationToa()));
            }
            if (railContainer.getEts() != null) {
                railContainer.setEtsStr(DateUtil.dayStrOf(railContainer.getEts()));
            }
            if (railContainer.getAts() != null) {
                railContainer.setAtsStr(DateUtil.stringOf(railContainer.getAts()));
            }
        }
        return railContainers;
    }
}
