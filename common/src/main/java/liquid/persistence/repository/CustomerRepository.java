package liquid.persistence.repository;

import liquid.persistence.domain.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/24/13
 * Time: 11:14 PM
 */
public interface CustomerRepository extends CrudRepository<CustomerEntity, Long> {
    @Query(value = "SELECT * FROM CUSTOMER ORDER BY CONVERT(NAME USING GBK)", nativeQuery = true)
    Iterable<CustomerEntity> findOrderByName();

    CustomerEntity findByName(String name);

    Iterable<CustomerEntity> findByNameLike(String name);

    Page<CustomerEntity> findAll(Pageable pageable);
}
