package liquid.persistence.repository;

import liquid.persistence.PageRepository;
import liquid.persistence.domain.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/24/13
 * Time: 11:14 PM
 */
public interface CustomerRepository extends PageRepository<CustomerEntity> {
    CustomerEntity findByName(String name);

    Iterable<CustomerEntity> findByQueryNameLike(String queryName);

    Page<CustomerEntity> findByQueryNameLike(String queryName, Pageable pageable);
}
