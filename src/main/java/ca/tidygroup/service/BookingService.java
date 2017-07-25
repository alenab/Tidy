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

import java.time.*;
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

    private CustomerRepository customerRepository;

    private TimeLimitationsRepository timeLimitationsRepository;

    private WorkingHoursRepository workingHoursRepository;

    @Autowired
    public BookingService(PricingService pricingService, BookingRepository bookingRepository, AddressRepository addressRepository, AccountRepository accountRepository, OptionRepository optionRepository, CleaningPlanRepository cleaningPlanRepository, MailingService mailingService, DiscountRepository discountRepository, ApartmentUnitRepository apartmentUnitRepository, CustomerRepository customerRepository, TimeLimitationsRepository timeLimitationsRepository, WorkingHoursRepository workingHoursRepository) {
        this.pricingService = pricingService;
        this.bookingRepository = bookingRepository;
        this.addressRepository = addressRepository;
        this.accountRepository = accountRepository;
        this.optionRepository = optionRepository;
        this.cleaningPlanRepository = cleaningPlanRepository;
        this.mailingService = mailingService;
        this.discountRepository = discountRepository;
        this.apartmentUnitRepository = apartmentUnitRepository;
        this.customerRepository = customerRepository;
        this.timeLimitationsRepository = timeLimitationsRepository;
        this.workingHoursRepository = workingHoursRepository;
    }

    @Transactional
    public void add(BookingForm bookingForm) {
        log.info("Adding new booking: {}", bookingForm);

        // using email as login

        Account account = new Account();
        Customer customer = new Customer();
        List<Address> addresses = new ArrayList<>();
        Address address = new Address();

        if (accountRepository.findAccountByEmail(bookingForm.getEmail().toLowerCase()) == null) {
            account.setLogin(bookingForm.getEmail().toLowerCase());
            account.setEmail(bookingForm.getEmail().toLowerCase());
            account.setUserRole(Role.USER);
            account = accountRepository.save(account);

            customer.setFirstName(bookingForm.getFirstName());
            customer.setLastName(bookingForm.getLastName());
            customer.setPhoneNumber(bookingForm.getPhone());
            customer.setAccount(account);

            address.setAddress(bookingForm.getAddress());
            address.setAptNumber(bookingForm.getAptNumber());
            addresses.add(address);


        } else {
            account = accountRepository.findAccountByEmail(bookingForm.getEmail().toLowerCase());
            customer = customerRepository.findCustomerByAccount(account);
            if (customer.getFirstName().equalsIgnoreCase(bookingForm.getFirstName()) &
                    customer.getLastName().equalsIgnoreCase(bookingForm.getLastName()) &
                    customer.getPhoneNumber().equals(bookingForm.getPhone())) {
                addresses = customer.getUserAddress();
                boolean isExistingAddress = false;
                for (Address addr : addresses) {
                    if (addr.getAddress().toLowerCase().equalsIgnoreCase(bookingForm.getAddress().toLowerCase())) {
                        address = addr;
                        isExistingAddress = true;
                        break;
                    }
                }
                if (!isExistingAddress) {
                    address.setAddress(bookingForm.getAddress());
                    addresses.add(address);
                }

            } else {

                customer = new Customer();

                customer.setFirstName(bookingForm.getFirstName());
                customer.setLastName(bookingForm.getLastName());
                customer.setPhoneNumber(bookingForm.getPhone());
                customer.setAccount(account);

                address.setAddress(bookingForm.getAddress());
                address.setAptNumber(bookingForm.getAptNumber());
                addresses.add(address);
            }
        }

        customer.setUserAddress(addresses);
        customer = customerRepository.save(customer);

        address.setCustomer(customer);
        address = addressRepository.save(address);

        Booking booking = new Booking();
        booking.setCustomer(customer);
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
        booking.setStatus(Status.NEW);
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
        log.info("Updating booking: {}", bookingDTOAdmin);

        Customer customer = bookingRepository.getOne(id).getCustomer();

        // using email as login
        Account account = customer.getAccount();
        account.setLogin(bookingDTOAdmin.getEmail().toLowerCase());
        account.setEmail(bookingDTOAdmin.getEmail().toLowerCase());

        customer.setFirstName(bookingDTOAdmin.getFirstName());
        customer.setLastName(bookingDTOAdmin.getLastName());
        customer.setPhoneNumber(bookingDTOAdmin.getPhone());
        customer.setAccount(account);

        Address address = bookingRepository.getOne(id).getAddressForClean();
        address.setAddress(bookingDTOAdmin.getAddress());
        address.setAptNumber(bookingDTOAdmin.getAptNumber());

        address.setCustomer(customer);
        address = addressRepository.save(address);

        Booking booking = bookingRepository.findOne(id);
        booking.setCustomer(customer);
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
        booking.setEmployeeId(bookingDTOAdmin.getEmployeeId());
        booking.setDuration(bookingDTOAdmin.getDuration());
        booking.setStatus(Status.NEW);
        bookingRepository.save(booking);
    }

    public void cancelledBooking (Long id) {
        Booking booking = bookingRepository.findOne(id);
        booking.setStatus(Status.CANCELLED);
        bookingRepository.save(booking);

    }

    public List<LocalTime> getFreeTime(LocalDate date) {
        List<LocalTime> resultList = new ArrayList<>();
        WorkingHours workingHours = workingHoursRepository.findAll().get(0);
        LocalTime startHour = workingHours.getStartTime();
        LocalTime endHour = workingHours.getEndTime();
        long hours = Duration.between(startHour, endHour).abs().toHours();
        for (int i = 0; i < hours; i+= workingHours.getStep()) {
            resultList.add(startHour.plusHours(i));
        }

        List<TimeLimitations> timeLimitations = timeLimitationsRepository.findAllByDate(date);
        for (TimeLimitations limit : timeLimitations) {
            LocalTime start = limit.getStartTime();
            LocalTime end = limit.getEndTime();
            long limitHours = Duration.between(start, end).abs().toHours();
            for (int i = 0; i < limitHours; i++) {
                resultList.remove(start.plusHours(i));
            }
        }

        List<Booking> bookingsList = getBookingsForDate(date);
        for(Booking booking : bookingsList) {
            LocalTime bookingStart = booking.getCleaningTime().toLocalTime();
            int step = workingHours.getStep();
            for (int i = 0; i < step; i++) {
                resultList.remove(bookingStart.plusHours(i));
            }
        }

        return resultList;
    }

    private List<Booking> getBookingsForDate(LocalDate date) {
        ZonedDateTime dayStart = date.atStartOfDay().atZone(ZoneId.systemDefault());
        ZonedDateTime dayEnd = date.atTime(23, 59, 59).atZone(ZoneId.systemDefault());
        return bookingRepository.findAllByCleaningTimeBetween(dayStart, dayEnd);
    }
}
