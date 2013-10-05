package liquid.persistence.repository;

import liquid.persistence.domain.Planning;
import liquid.persistence.domain.TransBarge;
import liquid.persistence.domain.TransRailway;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 5:28 PM
 */
public interface TransBargeRepository extends CrudRepository<TransBarge, Long> {
    List<TransBarge> findByPlanning(Planning planing);

    List<TransBarge> findByTaskId(String taskId);

    List<TransBarge> findByOrderId(long orderId);
}
