package liquid.container.facade;

import liquid.container.domain.Container;
import liquid.container.domain.Containers;
import liquid.container.persistence.domain.ContainerEntity;
import liquid.container.persistence.domain.ContainerSubtypeEntity;
import liquid.container.service.ContainerService;
import liquid.operation.domain.ServiceProvider;
import liquid.persistence.domain.LocationEntity;
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

    public Iterable<ContainerEntity> enter(Containers containers) {
        List<ContainerEntity> entities = new ArrayList<>();

        for (int i = 0; i < containers.getList().size(); i++) {
            Container container = containers.getList().get(i);
            ContainerEntity containerEntity = new ContainerEntity();
            if (null == container.getBicCode() || container.getBicCode().trim().isEmpty())
                continue;
            containerEntity.setId(container.getId());
            containerEntity.setBicCode(container.getBicCode());
            containerEntity.setOwner(ServiceProvider.newInstance(ServiceProvider.class, container.getOwnerId()));
            containerEntity.setYard(LocationEntity.newInstance(LocationEntity.class, container.getYardId()));
            containerEntity.setSubtype(ContainerSubtypeEntity.newInstance(ContainerSubtypeEntity.class, container.getSubtypeId()));
            containerEntity.setStatus(0);
            entities.add(containerEntity);
        }

        return containerService.save(entities);
    }

    public void leave(Container container) {}

    public List<Container> findByBicCodeLike(String bicCode) {
        List<Container> containers = new ArrayList<Container>();

        Iterable<ContainerEntity> entities = containerService.findByBicCodeLike(bicCode);
        for (ContainerEntity entity : entities) {
            Container container = new Container();
            container.setId(entity.getId());
            container.setBicCode(entity.getBicCode());
            container.setOwnerId(entity.getOwner().getId());
            container.setOwner(entity.getOwner().getName());
            container.setSubtypeId(entity.getSubtype().getId());
            container.setSubtype(entity.getSubtype().getName());
            container.setYardId(entity.getYard().getId());
            container.setYard(entity.getYard().getName());
            containers.add(container);
        }

        return containers;
    }

    public Container find(Long id) {
        ContainerEntity entity = containerService.find(id);
        Container container = new Container();
        container.setId(entity.getId());
        container.setBicCode(entity.getBicCode());
        container.setSubtypeId(entity.getSubtype().getId());
        container.setOwnerId(entity.getOwner().getId());
        container.setYardId(entity.getYard().getId());
        return container;
    }
}
