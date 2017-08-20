package ca.tidygroup.controllers;

import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.dto.EmailMessage;
import ca.tidygroup.manager.AccountManager;
import ca.tidygroup.manager.BookingManager;
import ca.tidygroup.model.Discount;
import ca.tidygroup.service.BillingService;
import ca.tidygroup.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class BookingController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    @Value("${tidy.google.api.key}")
    private String googleApiKey;

    private BookingManager bookingManager;

    private AccountManager accountManager;

    private BillingService billingService;

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingManager bookingManager, AccountManager accountManager, BillingService billingService, BookingService bookingService) {
        this.bookingManager = bookingManager;
        this.accountManager = accountManager;
        this.billingService = billingService;
        this.bookingService = bookingService;
    }

    @ModelAttribute
    public void bookingModelAttributes(Model model) {
        model.addAttribute("booking", new BookingForm());
        model.addAttribute("allPlans", bookingService.getAllCleaningPlans());
        model.addAttribute("allBedrooms", bookingService.getListOfBedrooms());
        model.addAttribute("discount", new Discount());
        model.addAttribute("googleApiKey", googleApiKey);
        model.addAttribute("applicationId", billingService.getApplicationId());
        model.addAttribute("message", new EmailMessage());
        model.addAttribute("paymentMethods", bookingService.getAllPaymentMethods());
    }

    @GetMapping("/book")
    public String book() {
        return "book";
    }

    @PostMapping("/book")
    public String bookForm(@Valid @ModelAttribute("booking") BookingForm booking, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("There were binding errors at attempt to process booking form: {}", bindingResult.getAllErrors());
            return "book";
        }
        bookingManager.handleNewBooking(booking);

        String billingCustomerId = accountManager.getCustomer(booking).getBillingCustomerId();
        String customerCard = billingService.createCustomerCard(billingCustomerId, booking.getNonce());
        log.debug("Customer card is created: {}", customerCard);
        return "thankyou";
    }

    @PostMapping("/applyActivationCode")
    public String applyActivationCode(@ModelAttribute("discount") Discount discount,
                                      @ModelAttribute("booking") BookingForm booking, BindingResult bindingResult) {
        boolean isApplied = bookingService.applyActivationCode(booking, discount.getCode());
        if (bindingResult.hasErrors() || !isApplied) {
            log.warn("There were binding errors at attempt to apply discount: {}. Is discount applied: {}",
                    bindingResult.getAllErrors(), isApplied);
        }
        return "book";
    }
}
