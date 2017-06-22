package ca.tidygroup.service;

import ca.tidygroup.dto.BookingDTOAdmin;
import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.model.*;
import ca.tidygroup.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    private ApartmentUnitRepository apartmentUnitRepository;

    @Autowired
    public BookingService(PricingService pricingService, BookingRepository bookingRepository, AddressRepository addressRepository, AccountRepository accountRepository, OptionRepository optionRepository, CleaningPlanRepository cleaningPlanRepository, MailingService mailingService, DiscountRepository discountRepository, ApartmentUnitRepository apartmentUnitRepository) {
        this.pricingService = pricingService;
        this.bookingRepository = bookingRepository;
        this.addressRepository = addressRepository;
        this.accountRepository = accountRepository;
        this.optionRepository = optionRepository;
        this.cleaningPlanRepository = cleaningPlanRepository;
        this.mailingService = mailingService;
        this.discountRepository = discountRepository;
        this.apartmentUnitRepository = apartmentUnitRepository;
    }

    @Transactional
    public void add(BookingForm bookingForm) {
        log.info("Adding new booking: {}", bookingForm);

//        address = addressRepository.save(address);

        Account account = new Account();
        account.setFirstName(bookingForm.getFirstName());
        account.setLastName(bookingForm.getLastName());
        account.setEmail(bookingForm.getEmail());
        account.setPhoneNumber(bookingForm.getPhone());

        ArrayList<Address> addresses = new ArrayList<>();
        Address address = new Address();
        address.setPostcode(bookingForm.getPostCode());
        address.setCity(bookingForm.getCity());
        address.setAddress(bookingForm.getAddress());

        addresses.add(address);
        account.setUserAddress(addresses);
        // using email as login
        account.setLogin(bookingForm.getEmail());
        account.setUserRole(Role.USER);
        account = accountRepository.save(account);

        address.setAccount(account);
        address = addressRepository.save(address);

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
        return optionRepository.findAll(new Sort(Sort.Direction.ASC, CleaningOption.ID_COL_NAME));
    }

    public List<Integer> getListOfBedrooms() {
        return apartmentUnitRepository.getListOfBedrooms();
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


    @Transactional
    public void updateBooking(long id, BookingDTOAdmin bookingDTOAdmin) {
        Account account = bookingRepository.getOne(id).getAccount();

        account.setFirstName(bookingDTOAdmin.getFirstName());
        account.setLastName(bookingDTOAdmin.getLastName());
        account.setEmail(bookingDTOAdmin.getEmail());
        account.setPhoneNumber(bookingDTOAdmin.getPhone());

        Address address = bookingRepository.getOne(id).getAddressForClean();
        address.setPostcode(bookingDTOAdmin.getPostcode());
        address.setCity(bookingDTOAdmin.getCity());
        address.setAddress(bookingDTOAdmin.getAddress());

        // using email as login

        address.setAccount(account);
        address = addressRepository.save(address);

        Booking booking = bookingRepository.findOne(id);
        booking.setAccount(account);
        booking.setAddressForClean(address);
        booking.setNumberOfRooms(bookingDTOAdmin.getNumberOfRooms());
        booking.setNumberOfBathrooms(bookingDTOAdmin.getNumberOfBathrooms());
        booking.setSpecialRequest(bookingDTOAdmin.getSpecialRequest());
        booking.setDiscountPercent(bookingDTOAdmin.getDiscount());
        booking.setCleaningTime(ZonedDateTime.of(LocalDate.parse(bookingDTOAdmin.getCleaningDate()),
                LocalTime.parse(bookingDTOAdmin.getCleaningTime()), ZoneId.systemDefault()));
        booking.setCleaningPlan(bookingDTOAdmin.getCleaningPlan());
        booking.setAdditionalOptions(bookingDTOAdmin.getCleaningOptions());
        booking.setPrice(bookingDTOAdmin.getPrice());
        bookingRepository.save(booking);
    }
}
