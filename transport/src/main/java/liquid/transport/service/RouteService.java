package liquid.transport.service;

import liquid.service.AbstractService;
import liquid.transport.persistence.domain.PathEntity;
import liquid.transport.persistence.domain.RouteEntity;
import liquid.transport.persistence.repository.RouteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Tao Ma on 11/26/14.
 */
@Service
public class RouteService extends AbstractService<RouteEntity, RouteRepository> {

    @Override
    public void doSaveBefore(RouteEntity entity) { }

    public void enable(Long id) { }

    public void disable(Long id) {}

    public void delete(Long id) {
        repository.delete(id);
    }

    public RouteEntity findOne(Long id) {
        RouteEntity route = repository.findOne(id);
        sort(route);
        return route;
    }

    public RouteEntity addPath(Long routeId, PathEntity newPath) {
        RouteEntity route = repository.findOne(routeId);
        newPath.setRoute(route);

        if (null == route.getHead()) {
            route.setHead(newPath);
        }

        List<PathEntity> paths = route.getPaths();
        for (PathEntity path : paths) {
            if (null == path.getNext()) {
                path.setNext(newPath);
                break;
            }
        }

        paths.add(newPath);

        route.setFrom(route.getHead().getFrom());
        route.setTo(newPath.getTo());
        return save(route);
    }

    private void sort(RouteEntity route) {
        PathEntity head = route.getHead();
        if (null == head) return;

        PathEntity[] pathArray = route.getPaths().toArray(new PathEntity[0]);

        PathEntity p = head;

        // TODO: Optimize this algorithm by the reversed order
        for (int i = 0; i < route.getPaths().size(); i++) {
            for (int j = i; j < route.getPaths().size(); j++) {
                if (p.getId() == pathArray[j].getId()) {
                    p = pathArray[i];
                    pathArray[i] = pathArray[j];
                    pathArray[j] = p;
                    p = pathArray[i].getNext();
                    break;
                }
            }
        }

        route.setPaths(Arrays.asList(pathArray));
    }

    public Page<RouteEntity> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<RouteEntity> find(Long fromId, Long toId) {
        return repository.findByFromIdAndToId(fromId, toId);
    }
}
