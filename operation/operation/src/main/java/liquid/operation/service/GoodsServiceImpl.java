package liquid.operation.service;

import liquid.operation.domain.Goods;
import liquid.operation.repository.GoodsRepository;
import liquid.service.AbstractCachedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 7/1/14.
 */
@Service
public class GoodsServiceImpl extends AbstractCachedService<Goods, GoodsRepository>
        implements InternalGoodsService {
    @Override
    public void doSaveBefore(Goods entity) { }

    @Override
    public Page<Goods> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
