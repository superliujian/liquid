package liquid.order.facade;

import liquid.container.domain.ContainerType;
import liquid.container.service.ContainerSubtypeService;
import liquid.order.domain.TransportedContainer;
import liquid.order.domain.ValueAddedOrder;
import liquid.order.persistence.domain.ReceivingContainerEntity;
import liquid.order.persistence.domain.ReceivingOrderEntity;
import liquid.order.persistence.repository.ReceivingContainerRepository;
import liquid.order.service.ReceivingOrderService;
import liquid.persistence.domain.ServiceTypeEntity;
import liquid.security.SecurityContext;
import liquid.service.CustomerService;
import liquid.service.GoodsService;
import liquid.service.LocationService;
import liquid.service.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Tao Ma on 2/24/15.
 */
@Service
public class ValueAddedOrderFacade {

    @Autowired
    private ReceivingOrderService receivingOrderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private ReceivingContainerRepository receivingContainerRepository;

    public Page<ValueAddedOrder> findAll(Pageable pageable) {
        Page<ReceivingOrderEntity> page = receivingOrderService.findAll(pageable);
        List<ReceivingOrderEntity> entities = page.getContent();
        List<ValueAddedOrder> orders = convert(entities);
        return new PageImpl<ValueAddedOrder>(orders, pageable, page.getTotalElements());
    }

    private List<ValueAddedOrder> convert(List<ReceivingOrderEntity> entities) {
        List<ValueAddedOrder> orders = new ArrayList<>(entities.size());
        for (ReceivingOrderEntity entity : entities) {
            orders.add(convert(entity));
        }
        return orders;
    }

    private ValueAddedOrder convert(ReceivingOrderEntity orderEntity) {
        ValueAddedOrder order = new ValueAddedOrder();
        order.setId(orderEntity.getId());
        order.setOrderNo(orderEntity.getOrderNo());
        order.setCustomerId(orderEntity.getCustomerId());
        order.setCustomerName(customerService.find(orderEntity.getCustomerId()).getName());
        order.setConsignee(orderEntity.getConsignee());
        order.setConsigneePhone(orderEntity.getConsigneePhone());
        order.setConsigneeAddress(orderEntity.getConsigneeAddress());
        order.setOriginId(orderEntity.getSrcLocId());
        order.setOrigination(locationService.find(orderEntity.getSrcLocId()).getName());
        order.setDestinationId(orderEntity.getDstLocId());
        order.setDestination(locationService.find(orderEntity.getDstLocId()).getName());
        order.setGoodsId(orderEntity.getGoodsId());
        order.setGoodsName(goodsService.find(orderEntity.getGoodsId()).getName());
        order.setGoodsWeight(orderEntity.getGoodsWeight());
        order.setGoodsDimension(orderEntity.getGoodsDimension());
        order.setContainerType(orderEntity.getContainerType());
        order.setContainerTypeName(ContainerType.valueOf(orderEntity.getContainerType()).getI18nKey());
        order.setContainerSubtype(containerSubtypeService.find(orderEntity.getContainerSubtypeId()).getName());
        order.setContainerQuantity(orderEntity.getContainerQty());
        order.setContainerAttribute(orderEntity.getContainerAttribute());
        order.setCnyTotal(orderEntity.getTotalCny());
        order.setStatus(orderEntity.getStatus());

        return order;
    }

    @Transactional(value = "transactionManager")
    public ReceivingOrderEntity save(ValueAddedOrder order) {
        ReceivingOrderEntity orderEntity = convert(order);

        ReceivingOrderEntity entity = receivingOrderService.save(orderEntity);

        Collection<ReceivingContainerEntity> containers = receivingContainerRepository.findByReceivingOrder(entity);
        receivingContainerRepository.delete(containers);

        containers.clear();
        for (TransportedContainer container : order.getContainers()) {
            if (null != container && null != container.getBicCode() && container.getBicCode().trim().length() > 0) {
                ReceivingContainerEntity containerEntity = new ReceivingContainerEntity();
                containerEntity.setId(container.getId());
                containerEntity.setReceivingOrder(entity);
                containerEntity.setBicCode(container.getBicCode());
                containers.add(containerEntity);
            }
        }
        receivingContainerRepository.save(containers);

        return entity;
    }

    public ReceivingOrderEntity submit(ValueAddedOrder order) {
        order.setRole(SecurityContext.getInstance().getRole());
        ServiceTypeEntity serviceType = serviceTypeService.find(order.getServiceTypeId());
        order.setOrderNo(receivingOrderService.computeOrderNo(order.getRole(), serviceType.getCode()));
        ReceivingOrderEntity entity = save(order);
        return entity;
    }

    private ReceivingOrderEntity convert(ValueAddedOrder order) {
        ReceivingOrderEntity orderEntity = new ReceivingOrderEntity();
        orderEntity.setId(order.getId());
        orderEntity.setOrderNo(order.getOrderNo());
        orderEntity.setServiceTypeId(order.getServiceTypeId());
        orderEntity.setCustomerId(order.getCustomerId());
        orderEntity.setConsignee(order.getConsignee());
        orderEntity.setConsigneePhone(order.getConsigneePhone());
        orderEntity.setConsigneeAddress(order.getConsigneeAddress());
        orderEntity.setSrcLocId(Long.valueOf(order.getOrigination()));
        orderEntity.setDstLocId(Long.valueOf(order.getDestination()));
        orderEntity.setGoodsId(order.getGoodsId());
        orderEntity.setGoodsWeight(order.getGoodsWeight());
        orderEntity.setTotalCny(order.getCnyTotal());
        orderEntity.setContainerType(order.getContainerType());
        orderEntity.setContainerQty(order.getContainerQuantity());
        orderEntity.setContainerType(order.getContainerType());
        if (order.getContainerType() == ContainerType.RAIL.getType())
            orderEntity.setContainerSubtypeId(order.getRailContainerSubtypeId());
        else
            orderEntity.setContainerSubtypeId(order.getSelfContainerSubtypeId());
        orderEntity.setCreateRole(order.getRole());
        orderEntity.setStatus(order.getStatus());
        return orderEntity;
    }

    public Page<ValueAddedOrder> findByCustomerId(Long customerId, Pageable pageable) {
        Page<ReceivingOrderEntity> page = receivingOrderService.findByCustomerId(customerId, SecurityContext.getInstance().getUsername(), pageable);

        List<ReceivingOrderEntity> entities = page.getContent();
        List<ValueAddedOrder> orders = convert(entities);

        return new PageImpl<ValueAddedOrder>(orders, pageable, page.getTotalElements());
    }

    public ValueAddedOrder find(Long id) {
        ReceivingOrderEntity orderEntity = receivingOrderService.find(id);
        ValueAddedOrder order = convert(orderEntity);

        List<TransportedContainer> containers = new ArrayList<>();
        Collection<ReceivingContainerEntity> receivingContainers = receivingContainerRepository.findByReceivingOrder(orderEntity);
        for (ReceivingContainerEntity receivingContainer : receivingContainers) {
            TransportedContainer container = new TransportedContainer();
            container.setId(receivingContainer.getId());
            container.setBicCode(receivingContainer.getBicCode());
            containers.add(container);
        }
        order.setContainers(containers);

        return order;
    }
}
