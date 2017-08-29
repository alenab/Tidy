package ca.tidygroup.service;

import ca.tidygroup.dto.BookingDTOCustomer;
import ca.tidygroup.model.Address;
import ca.tidygroup.model.Booking;
import ca.tidygroup.model.Customer;
import ca.tidygroup.repository.AddressRepository;
import ca.tidygroup.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private BookingRepository bookingRepository;
    private PricingService pricingService;
    private AddressRepository addressRepository;

    public UserService(BookingRepository bookingRepository, PricingService pricingService, AddressRepository addressRepository) {
        this.bookingRepository = bookingRepository;
        this.pricingService = pricingService;
        this.addressRepository = addressRepository;
    }

    public List<BookingDTOCustomer> getCustomerBookings(Customer customer) {
        List<BookingDTOCustomer> result = new ArrayList<>();
        List<Booking> bookingList = bookingRepository.findAllByCustomerOrderByCleaningTime(customer);
        for (Booking booking : bookingList) {
            BookingDTOCustomer bookingDTOCustomer = getBookingDTOCustomer(booking);
            result.add(bookingDTOCustomer);
        }
        return result;
    }

    public BookingDTOCustomer getBookingByID(long id) {
        Booking booking = bookingRepository.getOne(id);
        return getBookingDTOCustomer(booking);
    }

    public BookingDTOCustomer getBookingDTOCustomer(Booking booking) {
        BookingDTOCustomer bookingDTOCustomer = new BookingDTOCustomer();
        bookingDTOCustomer.setId(booking.getId());
        bookingDTOCustomer.setCleaningPlan(booking.getCleaningPlan());
        bookingDTOCustomer.setNumberOfRooms(booking.getNumberOfRooms());
        bookingDTOCustomer.setNumberOfBathrooms(booking.getNumberOfBathrooms());
        bookingDTOCustomer.setCleaningOptions(booking.getAdditionalOptions());
        bookingDTOCustomer.setSpecialRequest(booking.getSpecialRequest());
        bookingDTOCustomer.setCleaningDate(booking.getCleaningTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
        bookingDTOCustomer.setCleaningTime(booking.getCleaningTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
        bookingDTOCustomer.setAddress(booking.getAddressForClean().getAddress());
        bookingDTOCustomer.setAptNumber(booking.getAddressForClean().getAptNumber());
        bookingDTOCustomer.setPrice(booking.getPrice());
        bookingDTOCustomer.setFinalPrice(pricingService.getFinalPrice(booking));
        bookingDTOCustomer.setStatus(booking.getStatus());
        bookingDTOCustomer.setDiscount(booking.getDiscountPercent());
        bookingDTOCustomer.setNonce(booking.getCreateBooking().toString());
        bookingDTOCustomer.setGetInNotes(booking.getGetInNotes());
        bookingDTOCustomer.setPaymentMethod(booking.getPaymentMethod());
        return bookingDTOCustomer;
    }

    public void updateBooking (Long id, BookingDTOCustomer bookingDTOCustomer) {
        Booking booking = bookingRepository.getOne(id);
        booking.setCleaningPlan(bookingDTOCustomer.getCleaningPlan());
        booking.setNumberOfRooms(bookingDTOCustomer.getNumberOfRooms());
        booking.setNumberOfBathrooms(bookingDTOCustomer.getNumberOfBathrooms());
        booking.setAdditionalOptions(bookingDTOCustomer.getCleaningOptions());
        booking.setSpecialRequest(bookingDTOCustomer.getSpecialRequest());

        Customer customer = booking.getCustomer();
        List<Address> addresses = customer.getUserAddress();
        Address addr = new Address();
        if (addresses != null) {
            for (Address address : addresses) {
                if (address.getAddress().equalsIgnoreCase(bookingDTOCustomer.getAddress())) {
                    String aptNumber = Optional.ofNullable(address.getAptNumber()).orElse("");
                    if (aptNumber.equalsIgnoreCase(bookingDTOCustomer.getAptNumber())) {
                       addr = address;
                    }
                }
            }
        }

        addr.setAddress(bookingDTOCustomer.getAddress());
        addr.setAptNumber(bookingDTOCustomer.getAptNumber());
        addr.setCustomer(customer);
        addressRepository.save(addr);
        booking.setAddressForClean(addr);


        booking.setGetInNotes(bookingDTOCustomer.getGetInNotes());
        booking.setCleaningTime(ZonedDateTime.of(LocalDate.parse(bookingDTOCustomer.getCleaningDate()),
                LocalTime.parse(bookingDTOCustomer.getCleaningTime()), ZoneId.systemDefault()));
        booking.setPaymentMethod(bookingDTOCustomer.getPaymentMethod());
        booking.setPrice(bookingDTOCustomer.getPrice());
        bookingRepository.save(booking);
    }
}
