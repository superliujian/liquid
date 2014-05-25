package liquid.service;

import liquid.metadata.ContainerStatus;
import liquid.persistence.domain.ContainerEntity;
import liquid.persistence.domain.ShippingContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by redbrick9 on 5/19/14.
 */
@Service
public class ContainerAllocationService {
    @Autowired
    private ContainerService containerService;

    @Autowired
    private ShippingContainerService shippingContainerService;

    @Transactional("transactionManager")
    public void allocate(List<ShippingContainer> shippingContainers) {
        List<ContainerEntity> containers = new ArrayList<>();
        for (ShippingContainer shippingContainer : shippingContainers) {
            ContainerEntity container = containerService.find(shippingContainer.getContainer().getId());
            container.setStatus(ContainerStatus.ALLOCATED.getValue());
            containers.add(container);
        }

        containerService.save(containers);
        shippingContainerService.save(shippingContainers);
    }

    @Transactional("transactionManager")
    public void undo(ShippingContainer shippingContainer) {
        ContainerEntity container = shippingContainer.getContainer();
        container.setStatus(ContainerStatus.IN_STOCK.getValue());
        shippingContainerService.remove(shippingContainer.getId());
        containerService.save(container);
    }
}
