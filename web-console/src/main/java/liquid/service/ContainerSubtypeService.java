package liquid.service;

import liquid.metadata.ContainerType;
import liquid.persistence.domain.ContainerSubtypeEntity;
import liquid.persistence.repository.ContainerSubtypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 5/4/14.
 */
@Service
public class ContainerSubtypeService {

    @Autowired
    private ContainerSubtypeRepository containerSubtypeRepository;

    public ContainerSubtypeEntity save(ContainerSubtypeEntity containerSubtypeEntity) {
        return containerSubtypeRepository.save(containerSubtypeEntity);
    }

    public ContainerSubtypeEntity find(long id) {
        return containerSubtypeRepository.findOne(id);
    }

    public Iterable<ContainerSubtypeEntity> findAll() {
        return containerSubtypeRepository.findAll();
    }

    public Iterable<ContainerSubtypeEntity> findEnabled() {
        return containerSubtypeRepository.findByState(0);
    }

    public Iterable<ContainerSubtypeEntity> findByContainerType(ContainerType containerType) {
        return containerSubtypeRepository.findByContainerType(containerType.getType());
    }
}
