package liquid.persistence.repository;

import liquid.persistence.domain.ContainerSubtype;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 5/4/14.
 */
public interface ContainerSubtypeRepository extends CrudRepository<ContainerSubtype, Long> {
    Iterable<ContainerSubtype> findByState(int state);

    Iterable<ContainerSubtype> findByContainerType(int containerType);
}