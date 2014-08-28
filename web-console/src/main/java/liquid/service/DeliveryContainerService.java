package liquid.service;

import liquid.order.persistence.domain.OrderEntity;
import liquid.shipping.persistence.domain.DeliveryContainer;
import liquid.shipping.persistence.domain.RouteEntity;
import liquid.shipping.persistence.domain.ShippingContainerEntity;
import liquid.shipping.persistence.repository.DeliveryContainerRepository;
import liquid.shipping.persistence.repository.ShippingContainerRepository;
import liquid.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by redbrick9 on 6/10/14.
 */
@Service
public class DeliveryContainerService extends AbstractService<DeliveryContainer, DeliveryContainerRepository> {

    @Autowired
    private DeliveryContainerRepository deliveryContainerRepository;

    @Autowired
    private ShippingContainerRepository shippingContainerRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RouteService routeService;

    @Override
    public void doSaveBefore(DeliveryContainer entity) {}

    public Collection<DeliveryContainer> findByRoute(RouteEntity route) {
        Collection<DeliveryContainer> deliveryContainers = deliveryContainerRepository.findByRoute(route);
        for (DeliveryContainer deliveryContainer : deliveryContainers) {
            if (deliveryContainer.getEtd() != null) {
                deliveryContainer.setEtdStr(DateUtil.dayStrOf(deliveryContainer.getEtd()));
            }
        }
        return deliveryContainers;
    }

    public Iterable<DeliveryContainer> initDeliveryContainers(String taskId) {
        OrderEntity order = taskService.findOrderByTaskId(taskId);
        Collection<DeliveryContainer> dcList = deliveryContainerRepository.findByOrder(order);
        if (dcList.size() > 0) {
            for (DeliveryContainer container : dcList) {
                if (container.getEtd() != null) {
                    container.setEtdStr(DateUtil.dayStrOf(container.getEtd()));
                }
            }
            return dcList;
        }

        dcList = new ArrayList<DeliveryContainer>();
        Iterable<RouteEntity> routes = routeService.findByOrderId(order.getId());
        for (RouteEntity route : routes) {
            Collection<ShippingContainerEntity> scList = shippingContainerRepository.findByRoute(route);
            for (ShippingContainerEntity sc : scList) {
                DeliveryContainer dc = new DeliveryContainer();
                dc.setOrder(route.getOrder());
                dc.setRoute(route);
                dc.setSc(sc);
                dc.setAddress(order.getConsigneeAddress());
                dcList.add(dc);
            }
        }

        return deliveryContainerRepository.save(dcList);
    }

    public DeliveryContainer findDeliveryContainer(long containerId) {
        DeliveryContainer deliveryContainer = deliveryContainerRepository.findOne(containerId);

        if (null == deliveryContainer.getAddress()) {
            deliveryContainer.setAddress(deliveryContainer.getOrder().getConsigneeAddress());
        }

        String defaultDayStr = DateUtil.dayStrOf(new Date());
        if (null == deliveryContainer.getEtd())
            deliveryContainer.setEtdStr(defaultDayStr);
        else
            deliveryContainer.setEtdStr(DateUtil.dayStrOf(deliveryContainer.getEtd()));

        return deliveryContainer;
    }

    public void saveDeliveryContainer(long containerId, DeliveryContainer formBean) {
        DeliveryContainer container = deliveryContainerRepository.findOne(containerId);

        if (formBean.isBatch()) {
            Collection<DeliveryContainer> containers = deliveryContainerRepository.findByOrder(container.getOrder());

            if (formBean.getEtdStr() != null && formBean.getEtdStr().trim().length() > 0) {
                for (DeliveryContainer dc : containers) {
                    dc.setEtd(DateUtil.dayOf(formBean.getEtdStr()));
                }
            }

            if (formBean.getAddress() != null && formBean.getAddress().trim().length() > 0) {
                for (DeliveryContainer dc : containers) {
                    dc.setAddress(formBean.getAddress());
                }
            }

            deliveryContainerRepository.save(containers);
        } else {
            if (formBean.getEtdStr() != null && formBean.getEtdStr().trim().length() > 0) {
                container.setEtd(DateUtil.dayOf(formBean.getEtdStr()));
            }
            if (formBean.getAddress() != null && formBean.getAddress().trim().length() > 0) {
                container.setAddress(formBean.getAddress());
            }
            deliveryContainerRepository.save(container);
        }
    }
}
