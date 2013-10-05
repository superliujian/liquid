package liquid.persistence.repository;

import liquid.persistence.domain.Planning;
import liquid.persistence.domain.TransRailway;
import liquid.persistence.domain.TransVessel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 5:29 PM
 */
public interface TransVesselRepository extends CrudRepository<TransVessel, Long> {
    List<TransVessel> findByPlanning(Planning planing);

    List<TransVessel> findByTaskId(String taskId);

    List<TransVessel> findByOrderId(long orderId);
}
