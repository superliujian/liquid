package liquid.order.persistence.repository;


import liquid.order.persistence.domain.ReceivedDetailEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by redbrick9 on 8/29/14.
 */
public interface ReceivedDetailRepository extends CrudRepository<ReceivedDetailEntity, Long> {}