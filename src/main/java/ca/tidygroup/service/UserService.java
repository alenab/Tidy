package ca.tidygroup.service;

import ca.tidygroup.dto.BookingDTOCustomer;
import ca.tidygroup.model.Booking;
import ca.tidygroup.model.Customer;
import ca.tidygroup.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private BookingRepository bookingRepository;
    private PricingService pricingService;

    public UserService(BookingRepository bookingRepository, PricingService pricingService) {
        this.bookingRepository = bookingRepository;
        this.pricingService = pricingService;
    }

    public List<BookingDTOCustomer> getCustomerBookings(Customer customer) {
        List<BookingDTOCustomer> result = new ArrayList<>();
        List<Booking> bookingList = bookingRepository.findAllByCustomer(customer);
        for (Booking booking : bookingList) {
            BookingDTOCustomer bookingDTOCustomer = new BookingDTOCustomer();
            bookingDTOCustomer.setId(booking.getId());
            bookingDTOCustomer.setCleaningPlan(booking.getCleaningPlan());
            bookingDTOCustomer.setNumberOfBathrooms(booking.getNumberOfBathrooms());
            bookingDTOCustomer.setCleaningOptions(booking.getAdditionalOptions());
            bookingDTOCustomer.setSpecialRequest(booking.getSpecialRequest());
            bookingDTOCustomer.setCleaningDate(booking.getCleaningTime().format(DateTimeFormatter.ISO_LOCAL_DATE));
            bookingDTOCustomer.setCleaningTime(booking.getCleaningTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
            bookingDTOCustomer.setAddress(booking.getAddressForClean().getAddress());
            bookingDTOCustomer.setAptNumber(booking.getAddressForClean().getAptNumber());
            bookingDTOCustomer.setFinalPrice(pricingService.getFinalPrice(booking));
            bookingDTOCustomer.setStatus(booking.getStatus());
            result.add(bookingDTOCustomer);
        }
        return result;
    }
}
