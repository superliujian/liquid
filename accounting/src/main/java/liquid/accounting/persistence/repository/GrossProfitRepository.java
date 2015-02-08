package liquid.accounting.persistence.repository;

import liquid.accounting.persistence.domain.GrossProfitEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Tao Ma on 2/7/15.
 */
public interface GrossProfitRepository extends CrudRepository<GrossProfitEntity, Long> {
    Page<GrossProfitEntity> findAll(Specification<GrossProfitEntity> specification, Pageable pageable);
}
