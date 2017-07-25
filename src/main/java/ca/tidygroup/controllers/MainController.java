package ca.tidygroup.controllers;

import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.dto.Message;
import ca.tidygroup.manager.AccountManager;
import ca.tidygroup.manager.BookingManager;
import ca.tidygroup.model.Account;
import ca.tidygroup.model.Discount;
import ca.tidygroup.service.BillingService;
import ca.tidygroup.service.BookingService;
import ca.tidygroup.service.MailingService;
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
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Value("${tidy.google.api.key}")
    private String googleApiKey;

    private BookingManager bookingManager;

    private AccountManager accountManager;

    private BookingService bookingService;

    private MailingService mailingService;

    private BillingService billingService;

    @Autowired
    public MainController(BookingManager bookingManager, AccountManager accountManager, BookingService bookingService, MailingService mailingService, BillingService billingService) {
        this.bookingManager = bookingManager;
        this.accountManager = accountManager;
        this.bookingService = bookingService;
        this.mailingService = mailingService;
        this.billingService = billingService;
    }

    @ModelAttribute
    public void bookingModelAttributes(Model model) {
        model.addAttribute("booking", new BookingForm());
        model.addAttribute("allOptions", bookingService.getAllCleaningOptions());
        model.addAttribute("allPlans", bookingService.getAllCleaningPlans());
        model.addAttribute("allBedrooms", bookingService.getListOfBedrooms());
        model.addAttribute("discount", new Discount());
        model.addAttribute("googleApiKey", googleApiKey);
        model.addAttribute("applicationId", billingService.getApplicationId());
        model.addAttribute("message", new Message());
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/send-message")
    public String sendMessage(Message message) {
        mailingService.sendEmailMessage(message);
        return "index";
    }

    @GetMapping("/services")
    public String services() {
        return "services";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/careers")
    public String careers() {
        return "careers";
    }

    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }

    @GetMapping("/book")
    public String book() {
        return "book";
    }

    @GetMapping("/thankyou")
    public String thankyou() {
        return "thankyou";
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

    @GetMapping("/login")
    public String loginGet(Model model) {
        model.addAttribute("account", new Account());
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}

