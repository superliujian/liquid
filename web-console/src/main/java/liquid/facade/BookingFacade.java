package liquid.facade;

import liquid.order.persistence.domain.OrderEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.service.ShipmentService;
import liquid.transport.web.domain.Booking;
import liquid.transport.web.domain.BookingItem;
import liquid.transport.web.domain.TransMode;
import liquid.transport.persistence.domain.SpaceBookingEntity;
import liquid.transport.persistence.domain.LegEntity;
import liquid.transport.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by redbrick9 on 8/16/14.
 */
@Service
public class BookingFacade {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ShipmentService shipmentService;


    public Booking computeBooking(Long orderId) {
        Booking booking = new Booking();

        Iterable<SpaceBookingEntity> bookingEntities = bookingService.findByOrderId(orderId);
        Iterable<ShipmentEntity> routes = shipmentService.findByOrderId(orderId);
        for (ShipmentEntity route : routes) {
            Collection<LegEntity> legs = route.getLegs();
            for (LegEntity leg : legs) {
                switch (TransMode.valueOf(leg.getTransMode())) {
                    case BARGE:
                    case VESSEL:
                        BookingItem bookingItem = new BookingItem();
                        bookingItem.setLegId(leg.getId());
                        bookingItem.setSource(leg.getSrcLoc().getName());
                        bookingItem.setDestination(leg.getDstLoc().getName());
                        bookingItem.setContainerQuantity(route.getContainerQty());

                        for (SpaceBookingEntity bookingEntity : bookingEntities) {
                            if (bookingEntity.getLeg().getId().equals(leg.getId())) {
                                bookingItem.setId(bookingEntity.getId());
                                bookingItem.setShipownerId(bookingEntity.getShipowner().getId());
                                bookingItem.setShipownerName(bookingEntity.getShipowner().getName());
                                bookingItem.setBookingNo(bookingEntity.getBookingNo());
                            }
                        }
                        booking.getBookingItems().add(bookingItem);
                        break;
                }
            }
        }
        return booking;
    }

    public void save(Long orderId, Booking booking) {
        List<SpaceBookingEntity> bookingEntities = new ArrayList<>();

        for (BookingItem bookingItem : booking.getBookingItems()) {
            SpaceBookingEntity bookingEntity = new SpaceBookingEntity();
            bookingEntity.setId(bookingItem.getId());
            bookingEntity.setOrder(OrderEntity.newInstance(OrderEntity.class, orderId));
            bookingEntity.setLeg(LegEntity.newInstance(LegEntity.class, bookingItem.getLegId()));
            bookingEntity.setShipowner(ServiceProviderEntity.newInstance(ServiceProviderEntity.class, bookingItem.getShipownerId()));
            bookingEntity.setBookingNo(bookingItem.getBookingNo());
            bookingEntities.add(bookingEntity);
        }

        bookingService.save(bookingEntities);
    }
}
