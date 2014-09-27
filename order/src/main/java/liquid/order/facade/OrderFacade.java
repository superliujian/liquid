package liquid.order.facade;

import liquid.container.domain.ContainerType;
import liquid.container.persistence.domain.ContainerSubtypeEntity;
import liquid.domain.LocationType;
import liquid.domain.ServiceItem;
import liquid.order.domain.Order;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.persistence.domain.RailwayEntity;
import liquid.order.persistence.domain.ReceivableSummaryEntity;
import liquid.order.persistence.domain.ServiceItemEntity;
import liquid.order.service.OrderService;
import liquid.order.service.RailwayService;
import liquid.persistence.domain.*;
import liquid.security.SecurityContext;
import liquid.service.CustomerService;
import liquid.service.LocationService;
import liquid.service.ServiceTypeService;
import liquid.task.service.ActivitiEngineService;
import liquid.util.CollectionUtil;
import liquid.util.DateUtil;
import liquid.validation.FormValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by redbrick9 on 5/28/14.
 */
@Service
public class OrderFacade {
    @Autowired
    private Environment env;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private ActivitiEngineService bpmService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RailwayService railwayService;

    public Order initOrder() {
        Order order = new Order();
        List<LocationEntity> locations = locationService.findByType(LocationType.CITY.getType());
        LocationEntity secondCity = CollectionUtil.tryToGet2ndElement(locations);
        order.setDestinationId(secondCity.getId());
        order.setLoadingEstimatedTime(DateUtil.stringOf(new Date()));

        order.setPlanReportTime(DateUtil.stringOf(new Date()));
        return order;
    }

    @Transactional("transactionManager")
    public OrderEntity save(Order order) {
        OrderEntity orderEntity = convert(order);
        RailwayEntity railwayEntity = convertRailway(order);
        railwayEntity.setOrder(orderEntity);
        orderEntity.setRailway(railwayEntity);
        orderEntity = orderService.save(orderEntity);
        return orderEntity;
    }

    public OrderEntity submit(Order order) {
        // set role
        order.setRole(SecurityContext.getInstance().getRole());
        ServiceTypeEntity serviceType = serviceTypeService.find(order.getServiceTypeId());
        // compute order no.
        order.setOrderNo(orderService.computeOrderNo(order.getRole(), serviceType.getCode()));
        OrderEntity orderEntity = save(order);

        boolean hasDelivery = false;
        List<ServiceItem> serviceItems = order.getServiceItems();
        for (ServiceItem serviceItem : serviceItems) {
            if (serviceItem.getServiceSubtypeId() == Integer.valueOf(env.getProperty("service.subtype.delivery.id"))) {
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
        order.setRailwayId(null);
        order.setOrderNo(null);

        List<ServiceItem> serviceItems = order.getServiceItems();
        for (ServiceItem serviceItem : serviceItems) {
            serviceItem.setId(null);
        }

        order.setReceivableSummaryId(null);

        return order;
    }

    public OrderEntity convert(Order order) {
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
        orderEntity.setGoodsDimension(order.getGoodsDimension());
        orderEntity.setLoadingType(order.getLoadingType());
        orderEntity.setLoadingAddress(order.getLoadingAddress());
        orderEntity.setLoadingContact(order.getLoadingContact());
        orderEntity.setLoadingPhone(order.getLoadingPhone());
        orderEntity.setLoadingEt(DateUtil.dateOf(order.getLoadingEstimatedTime()));
        orderEntity.setContainerType(order.getContainerType());
        if (order.getContainerType() == ContainerType.RAIL.getType())
            orderEntity.setContainerSubtype(ContainerSubtypeEntity.
                    newInstance(ContainerSubtypeEntity.class, order.getRailContainerSubtypeId()));
        else
            orderEntity.setContainerSubtype(ContainerSubtypeEntity.
                    newInstance(ContainerSubtypeEntity.class, order.getSelfContainerSubtypeId()));
        orderEntity.setContainerQty(order.getContainerQuantity());
        orderEntity.setContainerAttribute(order.getContainerAttribute());

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
        ReceivableSummaryEntity receivableSummaryEntity = new ReceivableSummaryEntity();
        receivableSummaryEntity.setId(order.getReceivableSummaryId());
        receivableSummaryEntity.setOrder(orderEntity);
        receivableSummaryEntity.setCny(order.getCnyTotal());
        receivableSummaryEntity.setUsd(order.getUsdTotal());
        receivableSummaryEntity.setRemainingBalanceCny(order.getCnyTotal());
        receivableSummaryEntity.setRemainingBalanceUsd(order.getUsdTotal());
        orderEntity.setReceivableSummary(receivableSummaryEntity);

        orderEntity.setCreateRole(SecurityContext.getInstance().getRole());
        orderEntity.setStatus(order.getStatus());

        return orderEntity;
    }

    // TODO: Should enhance it in one to one way.
    public Order convert(OrderEntity orderEntity) {
        Order order = new Order();
        order.setId(orderEntity.getId());
        order.setOrderNo(orderEntity.getOrderNo());
        order.setServiceTypeId(orderEntity.getServiceType().getId());
        order.setCustomerId(orderEntity.getCustomer().getId());
        order.setCustomerName(orderEntity.getCustomer().getName());
        order.setTradeType(orderEntity.getTradeType());
        order.setOriginId(orderEntity.getSrcLoc().getId());
        order.setOrigination(orderEntity.getSrcLoc().getName());
        order.setDestinationId(orderEntity.getDstLoc().getId());
        order.setDestination(orderEntity.getDstLoc().getName());
        order.setConsignee(orderEntity.getConsignee());
        order.setConsigneePhone(orderEntity.getConsigneePhone());
        order.setConsigneeAddress(orderEntity.getConsigneeAddress());
        order.setGoodsId(orderEntity.getGoodsId());
        order.setGoodsName(orderEntity.getGoods().getName());
        order.setGoodsWeight(orderEntity.getGoodsWeight());
        order.setGoodsDimension(orderEntity.getGoodsDimension());
        order.setLoadingType(orderEntity.getLoadingType());
        order.setLoadingAddress(orderEntity.getLoadingAddress());
        order.setLoadingContact(orderEntity.getLoadingContact());
        order.setLoadingPhone(orderEntity.getLoadingPhone());
        order.setLoadingEstimatedTime(DateUtil.stringOf(orderEntity.getLoadingEt()));
        order.setContainerType(orderEntity.getContainerType());
        order.setContainerType(orderEntity.getContainerType());
        if (order.getContainerType() == ContainerType.RAIL.getType())
            order.setRailContainerSubtypeId(orderEntity.getContainerSubtype().getId());
        else
            order.setRailContainerSubtypeId(orderEntity.getContainerSubtype().getId());
        order.setContainerQuantity(orderEntity.getContainerQty());
        order.setContainerAttribute(orderEntity.getContainerAttribute());

        RailwayEntity railwayEntity = orderEntity.getRailway();
        if (null != railwayEntity) {
            order.setRailwayId(railwayEntity.getId());
            order.setPlanReportTime(DateUtil.stringOf(railwayEntity.getPlanReportTime()));
            order.setRailwayPlanTypeId(railwayEntity.getPlanType().getId());
            order.setRailwayPlanType(railwayEntity.getPlanType().getName());
            order.setProgramNo(railwayEntity.getProgramNo());
            if (null != railwayEntity.getSource()) {
                order.setRailSourceId(railwayEntity.getSource().getId());
                order.setRailSource(railwayEntity.getSource().getName());
            }
            if (null != railwayEntity.getDestination()) {
                order.setRailDestinationId(railwayEntity.getDestination().getId());
                order.setRailDestination(railwayEntity.getDestination().getName());
            }
            order.setComment(railwayEntity.getComment());
            order.setSameDay(railwayEntity.getSameDay());
        }

        List<ServiceItemEntity> serviceItemEntities = orderEntity.getServiceItems();
        for (ServiceItemEntity serviceItemEntity : serviceItemEntities) {
            ServiceItem serviceItem = new ServiceItem();
            serviceItem.setId(serviceItemEntity.getId());
            serviceItem.setServiceSubtypeId(serviceItemEntity.getServiceSubtype().getId());
            serviceItem.setCurrency(serviceItemEntity.getCurrency());
            serviceItem.setQuotation(serviceItemEntity.getQuotation());
            serviceItem.setComment(serviceItemEntity.getComment());
            order.getServiceItems().add(serviceItem);
        }
        order.setReceivableSummaryId(orderEntity.getReceivableSummary().getId());

        order.setUsdTotal(orderEntity.getReceivableSummary().getUsd());
        order.setCnyTotal(orderEntity.getReceivableSummary().getCny());
        order.setRole(orderEntity.getCreateRole());
        order.setStatus(orderEntity.getStatus());
        return order;
    }

    private RailwayEntity convertRailway(Order order) {
        RailwayEntity railwayEntity = new RailwayEntity();
        railwayEntity.setId(order.getRailwayId());
        railwayEntity.setPlanReportTime(DateUtil.dateOf(order.getPlanReportTime()));
        railwayEntity.setPlanType(RailwayPlanTypeEntity.newInstance(RailwayPlanTypeEntity.class, order.getRailwayPlanTypeId()));
        railwayEntity.setSource(LocationEntity.newInstance(LocationEntity.class, order.getRailSourceId()));
        railwayEntity.setDestination(LocationEntity.newInstance(LocationEntity.class, order.getDestinationId()));
        railwayEntity.setProgramNo(order.getProgramNo());
        railwayEntity.setComment(order.getComment());
        railwayEntity.setSameDay(order.getSameDay());
        return railwayEntity;
    }

    /**
     * If customer name is exactly equals to the one in database, the customer id is set.
     *
     * @param order
     * @return
     */
    public FormValidationResult validateCustomer(Order order) {
        long id = order.getCustomerId();
        String name = order.getCustomerName();
        if (id == 0L) {
            if (null != name && name.trim().length() > 0) {
                CustomerEntity customer = customerService.findByName(name);
                if (null != customer) {
                    order.setCustomerId(customer.getId());
                    return FormValidationResult.newSuccess();
                }
            } else {
                return FormValidationResult.newFailure("invalid.customer");
            }
        }

        CustomerEntity customer = customerService.find(id);
        if (name == null) return FormValidationResult.newFailure("invalid.customer");

        if (null == customer) return FormValidationResult.newFailure("invalid.customer");

        if (name.equals(customer.getName())) return FormValidationResult.newSuccess();
        else return FormValidationResult.newFailure("invalid.customer");
    }
}
