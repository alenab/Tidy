package ca.tidygroup.service;

import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.model.Account;
import ca.tidygroup.model.Address;
import ca.tidygroup.repository.AccountRepository;
import ca.tidygroup.repository.AddressRepository;
import ca.tidygroup.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void add(BookingForm booking) {
        Address address = new Address();
        address.setPostcode(booking.getPostCode());
        address.setCity(booking.getCity());
        address.setAddress(booking.getAddress());
        address = addressRepository.save(address);

        Account account = new Account();
        account.setFirstName(booking.getFirstName());
        account.setLastName(booking.getLastName());
        account.setEmail(booking.getEmail());
        account.setPhoneNumber(booking.getPhone());
        ArrayList<Address> addresses = new ArrayList<>();
        addresses.add(address);
        account.setUserAddress(addresses);
        accountRepository.save(account);
    }

    public String getAddressByPostCode(String postCode) {
        Address address = addressRepository.findOneByPostcodeEquals(postCode);
        return address.getAddress();
    }
}
