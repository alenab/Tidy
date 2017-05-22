package ca.tidygroup.service;

import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.model.Account;
import ca.tidygroup.model.Address;
import ca.tidygroup.model.Booking;
import ca.tidygroup.repository.AccountRepository;
import ca.tidygroup.repository.AddressRepository;
import ca.tidygroup.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

@Service
public class BookingService {

    private BookingRepository bookingRepository;

    private AddressRepository addressRepository;

    private AccountRepository accountRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, AddressRepository addressRepository, AccountRepository accountRepository) {
        this.bookingRepository = bookingRepository;
        this.addressRepository = addressRepository;
        this.accountRepository = accountRepository;
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
         bookingRepository.save(booking);

    }

    public String getAddressByPostCode(String postCode) {
        Address address = addressRepository.findOneByPostcodeEquals(postCode);
        return address.getAddress();
    }
}
