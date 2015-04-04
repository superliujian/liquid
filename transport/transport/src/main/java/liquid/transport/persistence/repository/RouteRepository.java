package liquid.transport.persistence.repository;

import liquid.persistence.PageRepository;
import liquid.transport.persistence.domain.RouteEntity;

import java.util.List;

/**
 * Created by mat on 11/26/14.
 */
public interface RouteRepository extends PageRepository<RouteEntity> {
    List<RouteEntity> findByFromIdAndToId(Long fromId, Long toId);
}
