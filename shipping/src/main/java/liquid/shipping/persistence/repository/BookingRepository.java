package liquid.shipping.persistence.repository;

import liquid.shipping.persistence.domain.BookingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Iterator;

/**
 * Created by redbrick9 on 8/15/14.
 */
public interface BookingRepository extends CrudRepository<BookingEntity, Long> {
    Iterable<BookingEntity> findByOrderId(Long orderId);
}
