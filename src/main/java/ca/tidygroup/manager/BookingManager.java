package ca.tidygroup.manager;

import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.event.NewBookingEvent;
import ca.tidygroup.model.Address;
import ca.tidygroup.model.Customer;
import ca.tidygroup.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class BookingManager {

    private static final Logger log = LoggerFactory.getLogger(BookingManager.class);

    private final ApplicationEventPublisher publisher;

    private BookingService bookingService;

    private AccountManager accountManager;

    @Autowired
    public BookingManager(ApplicationEventPublisher publisher, BookingService bookingService, AccountManager accountManager) {
        this.publisher = publisher;
        this.bookingService = bookingService;
        this.accountManager = accountManager;
    }

    public void handleNewBooking(BookingForm form) {
        log.info("New booking request: {}", form);
        Customer customer = accountManager.getCustomer(form);
        Address address = accountManager.getAddress(form);
        bookingService.add(customer, address, form);

        publisher.publishEvent(new NewBookingEvent(form));
    }
}
