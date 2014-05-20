package liquid.persistence.repository;

import liquid.persistence.domain.ContainerAllocation;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 5/19/14.
 */
public interface ContainerAllocationRepository extends CrudRepository<ContainerAllocation, Long> {
}
