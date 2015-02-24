package liquid.order.facade;

import liquid.container.domain.ContainerType;
import liquid.container.service.ContainerSubtypeService;
import liquid.order.domain.ValueAddedOrder;
import liquid.order.persistence.domain.ReceivingOrderEntity;
import liquid.order.service.ReceivingOrderService;
import liquid.service.CustomerService;
import liquid.service.GoodsService;
import liquid.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        return order;
    }

    public ReceivingOrderEntity save(ValueAddedOrder order) {
        ReceivingOrderEntity orderEntity = new ReceivingOrderEntity();
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
        orderEntity.setBicCodes(order.getBicCodes());
        orderEntity.setContainerType(order.getContainerType());
        if (order.getContainerType() == ContainerType.RAIL.getType())
            orderEntity.setContainerSubtypeId(order.getRailContainerSubtypeId());
        else
            orderEntity.setContainerSubtypeId(order.getSelfContainerSubtypeId());

        ReceivingOrderEntity entity = receivingOrderService.save(orderEntity);

        return entity;
    }
}
