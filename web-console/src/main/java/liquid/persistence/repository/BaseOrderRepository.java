package liquid.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/14/13
 * Time: 9:45 PM
 */
public interface BaseOrderRepository<T, ID extends Serializable> extends CrudRepository<T, ID>, JpaRepository<T, ID> {
    Iterable<T> findByCustomerNameLike(String cumtomerName);
}
