package liquid.service;

import liquid.persistence.domain.GoodsEntity;
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
    private GoodsService goodsService;

    public Iterable<GoodsEntity> findAll() {
        return goodsService.findAll();
    }

    public GoodsEntity find(Long id) {
        return goodsService.find(id);
    }
}
