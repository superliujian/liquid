package liquid.shipping.service;

import liquid.service.AbstractService;
import liquid.shipping.persistence.domain.BookingEntity;
import liquid.shipping.persistence.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 8/15/14.
 */
@Service
public class BookingService extends AbstractService<BookingEntity, BookingRepository> {

    @Autowired
    private BookingRepository bookingRepository;

    public Iterable<BookingEntity> findByOrderId(Long orderId) {
        return bookingRepository.findByOrderId(orderId);
    }

    @Override
    public void doSaveBefore(BookingEntity entity) { }
}
