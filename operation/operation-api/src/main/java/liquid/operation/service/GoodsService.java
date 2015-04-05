package liquid.operation.service;

import liquid.operation.domain.Goods;

/**
 * Created by Tao Ma on 4/5/15.
 */
public interface GoodsService {
    Iterable<Goods> findAll();

    Goods find(Long id);
}
