package liquid.service;

import liquid.persistence.domain.BaseUpdateEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Tao Ma on 11/26/14.
 */
public abstract class StandardCrudService<E extends BaseUpdateEntity> extends AbstractService<E, CrudRepository<E, Long>> {}
