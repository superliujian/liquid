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
        containerEntity.setOwner(new ServiceProviderEntity(container.getOwnerId()));
        containerEntity.setYard(new LocationEntity(container.getYardId()));
        containerEntity.setSubtype(new ContainerSubtypeEntity(container.getSubtypeId()));
        containerEntity.setStatus(0);
        containerService.save(containerEntity);
    }

    public void leave(Container container) {}
}
