package liquid.persistence.repository;

import liquid.persistence.domain.ServiceProvider;
import liquid.persistence.domain.SpType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 5:03 PM
 */
public interface SpRepository extends CrudRepository<ServiceProvider, Long> {
    Iterable<ServiceProvider> findByType(SpType type);

    @Query(value = "SELECT * FROM SERVICE_PROVIDER ORDER BY CONVERT(NAME USING GBK)", nativeQuery = true)
    public Iterable<ServiceProvider> findOrderByName();
}
