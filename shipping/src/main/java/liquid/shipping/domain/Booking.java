package liquid.shipping.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by redbrick9 on 8/16/14.
 */
public class Booking {
    private List<BookingItem> bookingItems = new ArrayList<>();

    public List<BookingItem> getBookingItems() {
        return bookingItems;
    }

    public void setBookingItems(List<BookingItem> bookingItems) {
        this.bookingItems = bookingItems;
    }
}
