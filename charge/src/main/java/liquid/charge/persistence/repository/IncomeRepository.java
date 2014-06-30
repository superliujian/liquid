package liquid.charge.persistence.repository;

import liquid.charge.persistence.domain.Income;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/30/13
 * Time: 1:00 PM
 */
public interface IncomeRepository extends CrudRepository<Income, Long> {
    List<Income> findByTaskId(String taskId);
}
