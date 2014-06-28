package liquid.container.persistence.repository;

import liquid.persistence.domain.ContainerSubtypeEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 5/4/14.
 */
public interface ContainerSubtypeRepository extends CrudRepository<ContainerSubtypeEntity, Long> {
    Iterable<ContainerSubtypeEntity> findByState(int state);

    Iterable<ContainerSubtypeEntity> findByContainerType(int containerType);
}