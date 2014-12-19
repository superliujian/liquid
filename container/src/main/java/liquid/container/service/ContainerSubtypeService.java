package liquid.container.service;

import liquid.container.domain.ContainerType;
import liquid.container.persistence.domain.ContainerSubtypeEntity;
import liquid.container.persistence.repository.ContainerSubtypeRepository;
import liquid.service.AbstractCachedService;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 5/4/14.
 */
@Service
public class ContainerSubtypeService extends AbstractCachedService<ContainerSubtypeEntity, ContainerSubtypeRepository> {
    @Override
    public void doSaveBefore(ContainerSubtypeEntity entity) { }

    public Iterable<ContainerSubtypeEntity> findEnabled() {
        return repository.findByState(0);
    }

    public Iterable<ContainerSubtypeEntity> findByContainerType(ContainerType containerType) {
        return repository.findByContainerType(containerType.getType());
    }
}
