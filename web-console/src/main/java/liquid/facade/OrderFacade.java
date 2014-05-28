package liquid.facade;

import liquid.domain.Order;
import liquid.metadata.LocationType;
import liquid.persistence.domain.LocationEntity;
import liquid.service.LocationService;
import liquid.utils.CollectionUtils;
import liquid.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by redbrick9 on 5/28/14.
 */
@Service
public class OrderFacade {
    @Autowired
    private LocationService locationService;

    public Order initOrder() {
        Order order = new Order();
        List<LocationEntity> locations = locationService.findByType(LocationType.CITY.getType());
        LocationEntity secondCity = CollectionUtils.tryToGet2ndElement(locations);
        order.setOriginId(secondCity.getId());
        order.setLoadingEstimatedTime(DateUtils.stringOf(new Date()));
        return order;
    }
}
