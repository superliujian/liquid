package liquid.operation.repository;

import liquid.operation.domain.ServiceProvider;
import liquid.operation.domain.ServiceProviderType;
import liquid.operation.domain.ServiceSubtype;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 5:03 PM
 */
public interface ServiceProviderRepository extends CrudRepository<ServiceProvider, Long> {
    Iterable<ServiceProvider> findByType(ServiceProviderType type);

    @Query(value = "SELECT * FROM OPS_SERVICE_PROVIDER ORDER BY CONVERT(NAME USING GBK)", nativeQuery = true)
    Iterable<ServiceProvider> findOrderByName();

    Page<ServiceProvider> findAll(Pageable pageable);

    Iterable<ServiceProvider> findByQueryNameLike(String queryName);

    List<ServiceProvider> findBySubtypes(Set<ServiceSubtype> subtypes);
}
