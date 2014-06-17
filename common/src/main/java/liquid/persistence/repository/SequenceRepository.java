package liquid.persistence.repository;

import liquid.persistence.domain.Sequence;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by redbrick9 on 4/26/14.
 */
public interface SequenceRepository extends CrudRepository<Sequence, Long> {

    @Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
    @Modifying
    @Query("UPDATE SEQUENCE s SET s.value = LAST_INSERT_ID(s.value + 1) WHERE s.name= ?")
    void increment(String name);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    int getValue();
}
