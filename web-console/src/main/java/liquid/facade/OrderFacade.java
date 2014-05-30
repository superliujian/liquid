package liquid.facade;

import liquid.domain.Order;
import liquid.domain.ServiceItem;
import liquid.metadata.ContainerType;
import liquid.metadata.LocationType;
import liquid.metadata.OrderStatus;
import liquid.persistence.domain.*;
import liquid.service.LocationService;
import liquid.service.OrderService;
import liquid.utils.CollectionUtils;
import liquid.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by redbrick9 on 5/28/14.
 */
@Service
public class OrderFacade {
    @Autowired
    private LocationService locationService;

    @Autowired
    private OrderService orderService;

    public Order initOrder() {
        Order order = new Order();
        List<LocationEntity> locations = locationService.findByType(LocationType.CITY.getType());
        LocationEntity secondCity = CollectionUtils.tryToGet2ndElement(locations);
        order.setOriginId(secondCity.getId());
        order.setLoadingEstimatedTime(DateUtils.stringOf(new Date()));
        return order;
    }

    public void save(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setServiceType(ServiceTypeEntity.newInstance(ServiceTypeEntity.class, order.getServiceTypeId()));
        orderEntity.setCustomer(CustomerEntity.newInstance(CustomerEntity.class, order.getCustomerId()));
        orderEntity.setTradeType(order.getTradeType());
        orderEntity.setSrcLoc(LocationEntity.newInstance(LocationEntity.class, order.getOriginId()));
        orderEntity.setDstLoc(LocationEntity.newInstance(LocationEntity.class, order.getDestinationId()));
        orderEntity.setConsignee(order.getConsignee());
        orderEntity.setConsigneePhone(order.getConsigneePhone());
        orderEntity.setConsigneeAddress(order.getConsigneeAddress());
        orderEntity.setGoods(GoodsEntity.newInstance(GoodsEntity.class, order.getGoodsId()));
        orderEntity.setGoodsWeight(order.getGoodsWeight());
        orderEntity.setLoadingType(order.getLoadingType());
        orderEntity.setLoadingAddress(order.getLoadingAddress());
        orderEntity.setLoadingContact(order.getLoadingContact());
        orderEntity.setLoadingPhone(order.getLoadingPhone());
        orderEntity.setLoadingEt(DateUtils.dateOf(order.getLoadingEstimatedTime()));
        orderEntity.setContainerType(order.getContainerType());
        if (order.getContainerType() == ContainerType.RAIL.getType())
            orderEntity.setContainerSubtype(ContainerSubtypeEntity.
                    newInstance(ContainerSubtypeEntity.class, order.getRailContainerSubtypeId()));
        else
            orderEntity.setContainerSubtype(ContainerSubtypeEntity.
                    newInstance(ContainerSubtypeEntity.class, order.getSelfContainerSubtypeId()));
        orderEntity.setContainerQty(order.getContainerQuantity());

        List<ServiceItemEntity> serviceItemEntities = new ArrayList<>();
        for (ServiceItem serviceItem : order.getServiceItems()) {
            ServiceItemEntity serviceItemEntity = new ServiceItemEntity();
            serviceItemEntity.setServiceSubtype(ServiceSubtypeEntity.
                    newInstance(ServiceSubtypeEntity.class, serviceItem.getServiceSubtypeId()));
            serviceItemEntity.setCurrency(serviceItem.getCurrency());
            serviceItemEntity.setQuotation(serviceItem.getQuotation());
            serviceItemEntities.add(serviceItemEntity);
        }

        orderEntity.setCnyTotal(order.getCnyTotal());
        orderEntity.setUsdTotal(order.getUsdTotal());
        orderEntity.setUpdateUser(order.getUsername());
        orderEntity.setCreateRole(order.getRole());
        orderEntity.setStatus(OrderStatus.SAVED.getValue());

        orderService.save(orderEntity);
    }
}
