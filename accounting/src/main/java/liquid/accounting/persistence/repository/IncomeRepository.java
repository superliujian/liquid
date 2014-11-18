package liquid.accounting.persistence.repository;

import liquid.accounting.persistence.domain.IncomeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/30/13
 * Time: 1:00 PM
 */
public interface IncomeRepository extends CrudRepository<IncomeEntity, Long> {
    List<IncomeEntity> findByTaskId(String taskId);
}
