package liquid.persistence.repository;

import liquid.persistence.domain.Charge;
import liquid.persistence.domain.TransRailway;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 8:37 PM
 */
public interface ChargeRepository extends CrudRepository<Charge, Long> {
    List<Charge> findByTaskId(String taskId);
}
