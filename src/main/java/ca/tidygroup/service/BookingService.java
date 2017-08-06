package ca.tidygroup.service;

import ca.tidygroup.dto.BookingDTOAdmin;
import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.event.ReducedBookingSlots;
import ca.tidygroup.event.ChargeEvent;
import ca.tidygroup.model.*;
import ca.tidygroup.repository.*;
import com.squareup.connect.models.TenderCardDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private PricingService pricingService;

    private BookingRepository bookingRepository;

    private AddressRepository addressRepository;

    private OptionRepository optionRepository;

    private CleaningPlanRepository cleaningPlanRepository;

    private DiscountRepository discountRepository;

    private ApartmentUnitRepository apartmentUnitRepository;

    private TimeLimitationsRepository timeLimitationsRepository;

    private WorkingHoursRepository workingHoursRepository;

    @Autowired
    public BookingService(PricingService pricingService, BookingRepository bookingRepository, AddressRepository addressRepository, OptionRepository optionRepository, CleaningPlanRepository cleaningPlanRepository, DiscountRepository discountRepository, ApartmentUnitRepository apartmentUnitRepository, TimeLimitationsRepository timeLimitationsRepository, WorkingHoursRepository workingHoursRepository) {
        this.pricingService = pricingService;
        this.bookingRepository = bookingRepository;
        this.addressRepository = addressRepository;
        this.optionRepository = optionRepository;
        this.cleaningPlanRepository = cleaningPlanRepository;
        this.discountRepository = discountRepository;
        this.apartmentUnitRepository = apartmentUnitRepository;
        this.timeLimitationsRepository = timeLimitationsRepository;
        this.workingHoursRepository = workingHoursRepository;
    }

    @EventListener
    public void handleChargeEvent(ChargeEvent event) {
        TenderCardDetails.StatusEnum status = event.getTransaction().getTenders().get(0).getCardDetails().getStatus();

        Booking booking = bookingRepository.findOne(event.getBookingId());

        switch (status) {
            case CAPTURED:
                booking.setStatus(Status.BILLING_SUCCESS);
                break;
            case VOIDED:
                booking.setStatus(Status.BILLING_CANCELED);
                break;
            case FAILED:
                booking.setStatus(Status.BILLING_FAILED);
                break;
            default:
                booking.setStatus(Status.BILLING_UNKNOWN);
        }
        bookingRepository.save(booking);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void reviewUpcomingBookingsForSlotLack(ReducedBookingSlots event) {
        ZonedDateTime yesterday = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS).minusDays(1);
        List<Booking> bookings = bookingRepository.findAllByCleaningTimeAfterAndStatusNotIn(yesterday,
                Status.CANCELLED, Status.COMPLETED);
        Map<ZonedDateTime, List<Booking>> map = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getCleaningTime));

        final int numberOfSlots = event.getNewNumberOfSlots();
        for (ZonedDateTime dateTime : map.keySet()) {
            List<Booking> duplicates = map.get(dateTime);
            if (duplicates.size() > numberOfSlots) {
                for (Booking conflictingBooking : duplicates) {
                    conflictingBooking.setStatus(Status.TIME_CONFLICT);
                    bookingRepository.save(conflictingBooking);
                    // TODO send new notification to show the actual conflicts to the system
                }
            }
        }
    }

    public void add(Customer customer, Address address, BookingForm bookingForm) {
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
        booking.setGetInNotes(bookingForm.getGetInNotes());

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
    }

    public List<CleaningOption> getAllCleaningOptions() {
        return optionRepository.findAll(new Sort(Sort.Direction.ASC, CleaningOption.ID_COL_NAME));
    }

    public List<CleaningOption> getCleaningOptionsByPlanId(Long planId) {
        CleaningPlan plan = cleaningPlanRepository.findOne(planId);
        return plan != null ? optionRepository.findAllByPlanListContainsOrderById(plan) : new ArrayList<>();
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
        booking.setGetInNotes(bookingDTOAdmin.getGetInNotes());
        booking.setAdminNotes(bookingDTOAdmin.getAdminNotes());
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
            for (int slot = 0; slot < workingHours.getNumberOfSlots(); slot++) {
                resultList.add(startHour.plusHours(i));
            }
        }

        if (date.equals(LocalDate.now())) {
            LocalTime currentStart = LocalTime.now().withMinute(0);
            long hoursToDelete = Duration.between(startHour,currentStart ).abs().toHours();
            for (int i = 0; i < hoursToDelete; i+= workingHours.getStep()) {
                resultList.remove(startHour.plusHours(i));
            }
        }

        List<TimeLimitations> timeLimitations = timeLimitationsRepository.findAllByDate(date);
        for (TimeLimitations limit : timeLimitations) {
            LocalTime start = limit.getStartTime();
            LocalTime end = limit.getEndTime();
            long limitHours = Duration.between(start, end).abs().toHours();
            for (int i = 0; i <= limitHours; i++) {
                // removing all time slots since time limitation is global
                resultList.removeAll(Collections.singletonList(start.plusHours(i)));
            }
        }

        List<Booking> bookingsList = getBookingsForDate(date);
        for(Booking booking : bookingsList) {
            LocalTime bookingStart = booking.getCleaningTime().toLocalTime();
            int step = apartmentUnitRepository.findApartmentUnitByCleaningPlanAndNumberOfBedroomsAndNumberOfBathrooms(
                    booking.getCleaningPlan(), booking.getNumberOfRooms(), booking.getNumberOfBathrooms()).getPlannedTime();
            for (int i = 0; i < step; i++) {
                resultList.remove(bookingStart.plusHours(i));
            }
        }
        // returning distinct results since user doesn't need to see the actual number of slots
        return resultList.stream().distinct().collect(Collectors.toList());
    }

    private List<Booking> getBookingsForDate(LocalDate date) {
        ZonedDateTime dayStart = date.atStartOfDay().atZone(ZoneId.systemDefault());
        ZonedDateTime dayEnd = date.atTime(23, 59, 59).atZone(ZoneId.systemDefault());
        return bookingRepository.findAllByCleaningTimeBetween(dayStart, dayEnd);
    }
}
