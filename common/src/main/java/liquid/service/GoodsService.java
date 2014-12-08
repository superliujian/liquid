package liquid.service;

import liquid.persistence.domain.GoodsEntity;
import liquid.persistence.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 7/1/14.
 */
@Service
public class GoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    public Iterable<GoodsEntity> findAll() {
        return goodsRepository.findAll();
    }

    public Page<GoodsEntity> findAll(Pageable pageable) {
        return goodsRepository.findAll(pageable);
    }

    public GoodsEntity save(GoodsEntity goods) {
        return goodsRepository.save(goods);
    }

    public GoodsEntity find(Long id) {
        return goodsRepository.findOne(id);
    }
}
