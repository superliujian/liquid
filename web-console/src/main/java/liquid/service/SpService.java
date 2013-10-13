package liquid.service;

import liquid.persistence.domain.ServiceProvider;
import liquid.persistence.repository.SpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 12:56 AM
 */
@Service
public class SpService {

    @Autowired
    private SpRepository spRepository;

    public Iterable<ServiceProvider> findAll() {
        return spRepository.findAll();
    }
}
