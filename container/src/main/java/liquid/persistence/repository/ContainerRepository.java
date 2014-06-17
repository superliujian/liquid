package liquid.persistence.repository;

import liquid.persistence.domain.ContainerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * TODO: Comments.
 * User: tao
 * Date: 10/3/13
 * Time: 3:50 PM
 */
public interface ContainerRepository extends CrudRepository<ContainerEntity, Long>, JpaSpecificationExecutor<ContainerEntity> {
    Iterable<ContainerEntity> findByStatusAndSubtype(int status, int type);

    List<ContainerEntity> findAll();

    Page<ContainerEntity> findAll(Pageable pageable);

    Page<ContainerEntity> findAll(Specification<ContainerEntity> specification, Pageable pageable);
}
