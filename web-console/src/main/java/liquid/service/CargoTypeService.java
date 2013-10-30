package liquid.service;

import liquid.persistence.domain.Goods;
import liquid.persistence.repository.GoodsRepository;
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
    private GoodsRepository goodsRepository;

    public Iterable<Goods> findAll() {
        return goodsRepository.findAll();
    }

    public Goods find(long id) {
        return goodsRepository.findOne(id);
    }
}
