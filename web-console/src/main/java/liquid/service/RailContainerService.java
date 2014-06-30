package liquid.service;

import liquid.shipping.persistence.domain.RailContainer;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.repository.RailContainerRepository;
import liquid.util.DateUtils;
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

    public Collection<RailContainer> findByRoute(RouteEntity route) {
        Collection<RailContainer> railContainers = railContainerRepository.findByRoute(route);
        for (RailContainer railContainer : railContainers) {
            if (railContainer.getLoadingToc() != null) {
                railContainer.setLoadingTocStr(DateUtils.stringOf(railContainer.getLoadingToc()));
            }
            if (railContainer.getStationToa() != null) {
                railContainer.setStationToaStr(DateUtils.stringOf(railContainer.getStationToa()));
            }
            if (railContainer.getEts() != null) {
                railContainer.setEtsStr(DateUtils.dayStrOf(railContainer.getEts()));
            }
            if (railContainer.getAts() != null) {
                railContainer.setAtsStr(DateUtils.stringOf(railContainer.getAts()));
            }
            if (railContainer.getAta() != null) {
                railContainer.setAtaStr(DateUtils.stringOf(railContainer.getAta()));
            }
        }
        return railContainers;
    }
}
