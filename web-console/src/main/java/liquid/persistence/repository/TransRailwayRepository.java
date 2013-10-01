package liquid.persistence.repository;

import liquid.persistence.domain.Planning;
import liquid.persistence.domain.TransRailway;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 2:18 PM
 */
public interface TransRailwayRepository extends CrudRepository<TransRailway, Long> {
    List<TransRailway> findByPlanning(Planning planing);
}
