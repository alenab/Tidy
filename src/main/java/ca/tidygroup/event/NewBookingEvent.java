package ca.tidygroup.event;

import ca.tidygroup.dto.BookingForm;

public class NewBookingEvent {

    private final BookingForm bookingForm;

    public NewBookingEvent(BookingForm bookingForm) {
        this.bookingForm = bookingForm;
    }

    public BookingForm getBookingForm() {
        return bookingForm;
    }
}
