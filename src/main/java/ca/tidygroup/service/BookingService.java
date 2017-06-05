package ca.tidygroup.service;

import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.model.*;
import ca.tidygroup.repository.*;
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

    private BookingRepository bookingRepository;

    private AddressRepository addressRepository;

    private AccountRepository accountRepository;

    private OptionRepository optionRepository;

    private CleaningPlanRepository cleaningPlanRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, AddressRepository addressRepository, AccountRepository accountRepository, OptionRepository optionRepository, CleaningPlanRepository cleaningPlanRepository) {
        this.bookingRepository = bookingRepository;
        this.addressRepository = addressRepository;
        this.accountRepository = accountRepository;
        this.optionRepository = optionRepository;
        this.cleaningPlanRepository = cleaningPlanRepository;
    }

    @Transactional
    public void add(BookingForm bookingForm) {
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
        booking.setDiscountPercent(Integer.parseInt(bookingForm.getDiscount()));
        booking.setCleaningTime(ZonedDateTime.of(LocalDate.parse(bookingForm.getCleaningDate()),
                LocalTime.parse(bookingForm.getCleaningTime()), ZoneId.systemDefault()));
        booking.setCleaningPlan(bookingForm.getCleaningPlan());
        booking.setAdditionalOptions(bookingForm.getCleaningOptions());
        bookingRepository.save(booking);
    }

    public List<CleaningOption> getAllCleaningOptions() {
        return optionRepository.findAll();
    }

    public List<CleaningPlan> getAllCleaningPlans() {
        return cleaningPlanRepository.findAll();
    }
}
