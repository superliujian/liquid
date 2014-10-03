package liquid.persistence.repository;

import liquid.persistence.domain.ServiceProviderEntity;
import liquid.persistence.domain.ServiceProviderTypeEnity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 5:03 PM
 */
public interface ServiceProviderRepository extends CrudRepository<ServiceProviderEntity, Long> {
    Iterable<ServiceProviderEntity> findByType(ServiceProviderTypeEnity type);

    @Query(value = "SELECT * FROM OPS_SERVICE_PROVIDER ORDER BY CONVERT(NAME USING GBK)", nativeQuery = true)
    Iterable<ServiceProviderEntity> findOrderByName();

    Page<ServiceProviderEntity> findAll(Pageable pageable);
}
