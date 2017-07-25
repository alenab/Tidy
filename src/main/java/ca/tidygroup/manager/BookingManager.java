package ca.tidygroup.manager;

import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.model.Address;
import ca.tidygroup.model.Customer;
import ca.tidygroup.service.BookingService;
import ca.tidygroup.service.MailingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingManager {

    private static final Logger log = LoggerFactory.getLogger(BookingManager.class);

    private MailingService mailingService;

    private BookingService service;

    private AccountManager accountManager;

    @Autowired
    public BookingManager(MailingService mailingService, BookingService service, AccountManager accountManager) {
        this.mailingService = mailingService;
        this.service = service;
        this.accountManager = accountManager;
    }

    public void handleNewBooking(BookingForm form) {
        log.info("New booking request: {}", form);
        Customer customer = accountManager.getCustomer(form);
        Address address = accountManager.getAddress(form);
        service.add(customer, address, form);

        mailingService.sendEmail(form);
    }
}
