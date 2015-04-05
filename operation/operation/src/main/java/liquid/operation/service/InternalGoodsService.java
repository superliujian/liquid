package liquid.operation.service;

import liquid.operation.domain.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by Tao Ma on 4/5/15.
 */
public interface InternalGoodsService extends GoodsService {
    Page<Goods> findAll(Pageable pageable);

    Goods save(Goods goods);
}
