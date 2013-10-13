package liquid.service;

import liquid.persistence.domain.Cargo;
import liquid.persistence.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/13/13
 * Time: 6:30 PM
 */
@Service
public class CargoTypeService {

    @Autowired
    private CargoRepository cargoRepository;

    public Iterable<Cargo> findAll() {
        return cargoRepository.findAll();
    }

    public Cargo find(long id) {
        return cargoRepository.findOne(id);
    }
}
