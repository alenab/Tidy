package ca.tidygroup.service;

import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.model.*;
import ca.tidygroup.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private PricingService pricingService;

    private BookingRepository bookingRepository;

    private AddressRepository addressRepository;

    private AccountRepository accountRepository;

    private OptionRepository optionRepository;

    private CleaningPlanRepository cleaningPlanRepository;

    private MailingService mailingService;

    private DiscountRepository discountRepository;

    @Autowired
    public BookingService(PricingService pricingService, BookingRepository bookingRepository, AddressRepository addressRepository, AccountRepository accountRepository, OptionRepository optionRepository, CleaningPlanRepository cleaningPlanRepository, MailingService mailingService, DiscountRepository discountRepository) {
        this.pricingService = pricingService;
        this.bookingRepository = bookingRepository;
        this.addressRepository = addressRepository;
        this.accountRepository = accountRepository;
        this.optionRepository = optionRepository;
        this.cleaningPlanRepository = cleaningPlanRepository;
        this.mailingService = mailingService;
        this.discountRepository = discountRepository;
    }

    @Transactional
    public void add(BookingForm bookingForm) {
        log.info("Adding new booking: {}", bookingForm);
        Address address = new Address();
        address.setPostcode(bookingForm.getPostCode());
        address.setCity(bookingForm.getCity());
        address.setAddress(bookingForm.getAddress());
        address = addressRepository.save(address);

        Account account = new Account();
        account.setFirstName(bookingForm.getFirstName());
        account.setLastName(bookingForm.getLastName());
        account.setEmail(bookingForm.getEmail());
        account.setPhoneNumber(bookingForm.getPhone());
        ArrayList<Address> addresses = new ArrayList<>();
        addresses.add(address);
        account.setUserAddress(addresses);
        accountRepository.save(account);

        Booking booking = new Booking();
        booking.setAccount(account);
        booking.setAddressForClean(address);
        booking.setNumberOfRooms(Integer.parseInt(bookingForm.getNumberOfRooms()));
        booking.setNumberOfBathrooms(Integer.parseInt(bookingForm.getNumberOfBathrooms()));
        booking.setSpecialRequest(bookingForm.getSpecialRequest());
        String discount = bookingForm.getDiscount();
        booking.setDiscountPercent(discount != null ? Integer.parseInt(discount) : 0);
        booking.setCleaningTime(ZonedDateTime.of(LocalDate.parse(bookingForm.getCleaningDate()),
                LocalTime.parse(bookingForm.getCleaningTime()), ZoneId.systemDefault()));
        booking.setCleaningPlan(bookingForm.getCleaningPlan());
        booking.setAdditionalOptions(bookingForm.getCleaningOptions());

        // validate that price which was shown to the end user on web page is the same as calculated on the server side
        double serverPrice = pricingService.getPrice(bookingForm);
        double clientPrice = Double.parseDouble(bookingForm.getPrice());
        if (Math.abs(serverPrice - clientPrice) >= 0.01) {
            log.warn("Prices on the server and client are different. Server: {}; Client: {}", serverPrice, clientPrice);
            throw new RuntimeException("The prices are changed! Try again");
        }

        booking.setPrice(serverPrice);
        bookingRepository.save(booking);

        mailingService.sendEmail(bookingForm);
    }

    public List<CleaningOption> getAllCleaningOptions() {
        return optionRepository.findAll();
    }

    public List<CleaningPlan> getAllCleaningPlans() {
        return cleaningPlanRepository.findAll();
    }

    public boolean applyActivationCode(BookingForm form, String code) {
        Discount discount = discountRepository.findByCodeEqualsIgnoringCase(code);
        if (discount != null) {
            log.info("Discount code {} applied successfully. Discount is: {}%", code, discount.getPercent());
            form.setDiscount(String.valueOf(discount.getPercent()));
            return true;
        }
        log.info("Discount code {} not found", code);
        return false;
    }
}
