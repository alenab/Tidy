package ca.tidygroup.event;

import com.squareup.connect.models.Transaction;

public class ChargeEvent {

    private final Transaction transaction;

    private final long bookingId;

    public ChargeEvent(Transaction transaction, long bookingId) {
        this.transaction = transaction;
        this.bookingId = bookingId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public long getBookingId() {
        return bookingId;
    }
}
