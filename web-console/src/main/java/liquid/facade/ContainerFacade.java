package liquid.facade;

import liquid.domain.Container;
import liquid.persistence.domain.ContainerEntity;
import liquid.persistence.domain.ContainerSubtypeEntity;
import liquid.persistence.domain.LocationEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 5/24/14.
 */
@Service
public class ContainerFacade {

    @Autowired
    private ContainerService containerService;

    public void enter(Container container) {

        ContainerEntity containerEntity = new ContainerEntity();

        containerEntity.setBicCode(container.getBicCode());
        containerEntity.setOwner(ServiceProviderEntity.newInstance(ServiceProviderEntity.class, container.getOwnerId()));
        containerEntity.setYard(LocationEntity.newInstance(LocationEntity.class, container.getYardId()));
        containerEntity.setSubtype(ContainerSubtypeEntity.newInstance(ContainerSubtypeEntity.class, container.getSubtypeId()));
        containerEntity.setStatus(0);
        containerService.save(containerEntity);
    }

    public void leave(Container container) {}
}
