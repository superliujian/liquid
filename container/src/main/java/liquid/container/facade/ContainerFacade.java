package liquid.container.facade;

import liquid.container.domain.Container;
import liquid.container.persistence.domain.ContainerEntity;
import liquid.container.persistence.domain.ContainerSubtypeEntity;
import liquid.persistence.domain.LocationEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.container.service.ContainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<Container> findByBicCodeLike(String bicCode) {
        List<Container> containers = new ArrayList<Container>();

        Iterable<ContainerEntity> entities = containerService.findByBicCodeLike(bicCode);
        for (ContainerEntity entity : entities) {
            Container container = new Container();
            container.setId(entity.getId());
            container.setBicCode(entity.getBicCode());
            containers.add(container);
        }

        return containers;
    }
}
