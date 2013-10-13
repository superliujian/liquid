package liquid.service;

import liquid.persistence.domain.Container;
import liquid.persistence.repository.ContainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 1:11 AM
 */
@Service
public class ContainerService {

    @Autowired
    private ContainerRepository containerRepository;

    public Iterable<Container> findAll() {
        return containerRepository.findAll();
    }
}
