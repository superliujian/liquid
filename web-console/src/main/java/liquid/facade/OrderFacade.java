package liquid.facade;

import liquid.domain.Order;
import liquid.domain.ServiceItem;
import liquid.metadata.ContainerType;
import liquid.metadata.LocationType;
import liquid.persistence.domain.*;
import liquid.service.LocationService;
import liquid.service.OrderService;
import liquid.service.ServiceTypeService;
import liquid.service.bpm.ActivitiEngineService;
import liquid.utils.CollectionUtils;
import liquid.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by redbrick9 on 5/28/14.
 */
@Service
public class OrderFacade {
    @Autowired
    private LocationService locationService;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private ActivitiEngineService bpmService;

    @Autowired
    private OrderService orderService;

    public Order initOrder() {
        Order order = new Order();
        List<LocationEntity> locations = locationService.findByType(LocationType.CITY.getType());
        LocationEntity secondCity = CollectionUtils.tryToGet2ndElement(locations);
        order.setDestinationId(secondCity.getId());
        order.setLoadingEstimatedTime(DateUtils.stringOf(new Date()));
        return order;
    }

    public OrderEntity save(Order order) {
        OrderEntity orderEntity = convert(order);
        return orderService.save(orderEntity);
    }

    public OrderEntity submit(Order order) {
        ServiceTypeEntity serviceType = serviceTypeService.find(order.getServiceTypeId());
        // compute order no.
        order.setOrderNo(orderService.computeOrderNo(order.getRole(), serviceType.getCode()));
        OrderEntity orderEntity = save(order);

        boolean hasDelivery = false;
        List<ServiceItem> serviceItems = order.getServiceItems();
        for (ServiceItem serviceItem : serviceItems) {
            if (serviceItem.getServiceSubtypeId() == 12) {
                hasDelivery = true;
                break;
            }
        }

        Map<String, Object> variableMap = new HashMap<>();
        variableMap.put("loadingType", orderEntity.getLoadingType());
        variableMap.put("hasDelivery", hasDelivery);
        variableMap.put("orderOwner", orderEntity.getUpdateUser());
        bpmService.startProcess(orderEntity.getUpdateUser(), orderEntity.getId(), variableMap);
        return orderEntity;
    }

    public Order find(long id) {
        OrderEntity orderEntity = orderService.find(id);
        Order order = convert(orderEntity);
        return order;
    }

    public Order duplicate(long id) {
        OrderEntity orderEntity = orderService.find(id);
        Order order = convert(orderEntity);
        order.setId(null);
        order.setOrderNo(null);

        List<ServiceItem> serviceItems = order.getServiceItems();
        for (ServiceItem serviceItem : serviceItems) {
            serviceItem.setId(null);
        }

        return order;
    }

    private OrderEntity convert(Order order) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(order.getId());
        orderEntity.setOrderNo(order.getOrderNo());
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

        for (ServiceItem serviceItem : order.getServiceItems()) {
            ServiceItemEntity serviceItemEntity = new ServiceItemEntity();
            serviceItemEntity.setId(serviceItem.getId());
            serviceItemEntity.setOrder(orderEntity);
            serviceItemEntity.setServiceSubtype(ServiceSubtypeEntity.
                    newInstance(ServiceSubtypeEntity.class, serviceItem.getServiceSubtypeId()));
            serviceItemEntity.setCurrency(serviceItem.getCurrency());
            serviceItemEntity.setQuotation(serviceItem.getQuotation());
            orderEntity.getServiceItems().add(serviceItemEntity);
        }

        orderEntity.setCnyTotal(order.getCnyTotal());
        orderEntity.setUsdTotal(order.getUsdTotal());
        orderEntity.setCreateRole(order.getRole());
        orderEntity.setStatus(order.getStatus());
        return orderEntity;
    }

    private Order convert(OrderEntity orderEntity) {
        Order order = new Order();
        order.setId(orderEntity.getId());
        order.setOrderNo(orderEntity.getOrderNo());
        order.setServiceTypeId(orderEntity.getServiceType().getId());
        order.setCustomerId(orderEntity.getCustomer().getId());
        order.setCustomerName(orderEntity.getCustomer().getName());
        order.setTradeType(orderEntity.getTradeType());
        order.setOriginId(orderEntity.getSrcLoc().getId());
        order.setDestinationId(orderEntity.getDstLoc().getId());
        order.setConsignee(orderEntity.getConsignee());
        order.setConsigneePhone(orderEntity.getConsigneePhone());
        order.setConsigneeAddress(orderEntity.getConsigneeAddress());
        order.setGoodsId(orderEntity.getGoodsId());
        order.setGoodsWeight(orderEntity.getGoodsWeight());
        order.setLoadingType(orderEntity.getLoadingType());
        order.setLoadingAddress(orderEntity.getLoadingAddress());
        order.setLoadingContact(orderEntity.getLoadingContact());
        order.setLoadingPhone(orderEntity.getLoadingPhone());
        order.setLoadingEstimatedTime(DateUtils.stringOf(orderEntity.getLoadingEt()));
        order.setContainerType(orderEntity.getContainerType());
        order.setContainerType(orderEntity.getContainerType());
        if (order.getContainerType() == ContainerType.RAIL.getType())
            order.setRailContainerSubtypeId(orderEntity.getContainerSubtype().getId());
        else
            order.setRailContainerSubtypeId(orderEntity.getContainerSubtype().getId());
        order.setContainerQuantity(orderEntity.getContainerQty());
        order.setContainerQuantity(orderEntity.getContainerQty());

        Set<ServiceItemEntity> serviceItemEntities = orderEntity.getServiceItems();
        for (ServiceItemEntity serviceItemEntity : serviceItemEntities) {
            ServiceItem serviceItem = new ServiceItem();
            serviceItem.setId(serviceItemEntity.getId());
            serviceItem.setServiceSubtypeId(serviceItemEntity.getServiceSubtype().getId());
            serviceItem.setCurrency(serviceItemEntity.getCurrency());
            serviceItem.setQuotation(serviceItemEntity.getQuotation());
            serviceItem.setComment(serviceItemEntity.getComment());
            order.getServiceItems().add(serviceItem);
        }

        order.setUsdTotal(orderEntity.getUsdTotal());
        order.setCnyTotal(orderEntity.getCnyTotal());
        order.setRole(orderEntity.getCreateRole());
        order.setStatus(orderEntity.getStatus());
        return order;
    }
}
