package ca.tidygroup.service;

import ca.tidygroup.dto.BookingDTO;
import ca.tidygroup.model.Booking;
import ca.tidygroup.model.CleaningOption;
import ca.tidygroup.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    private BookingRepository bookingRepository;

    @Autowired
    public AdminService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public List<BookingDTO> getAllBookings() {
        List<BookingDTO> result = new ArrayList<>();
        List<Booking> allBookings = bookingRepository.findAll();
        for (Booking booking : allBookings) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setId(booking.getId());
            bookingDTO.setCleaningPlan(booking.getCleaningPlan().getName());
            bookingDTO.setNumberOfRooms(booking.getNumberOfRooms());
            bookingDTO.setNumberOfBathrooms(booking.getNumberOfBathrooms());
            List<String> optionsName = new ArrayList<>();
            List<CleaningOption> list = booking.getAdditionalOptions();
            for (CleaningOption option : list) {
                optionsName.add(option.getName());
            }
            bookingDTO.setCleaningOptions(optionsName);
            bookingDTO.setSpecialRequest(booking.getSpecialRequest());
            bookingDTO.setCleaningTime(booking.getCleaningTime().toLocalDateTime());
            bookingDTO.setCity(booking.getAddressForClean().getCity());
            bookingDTO.setPostcode(booking.getAddressForClean().getPostcode());
            bookingDTO.setAddress(booking.getAddressForClean().getAddress());
            bookingDTO.setFirstName(booking.getAccount().getFirstName());
            bookingDTO.setLastName(booking.getAccount().getLastName());
            bookingDTO.setEmail(booking.getAccount().getEmail());
            bookingDTO.setPhone(booking.getAccount().getPhoneNumber());
            bookingDTO.setDiscount(booking.getDiscountPercent());
            bookingDTO.setPrice(booking.getPrice());
            bookingDTO.setFinalPrice(getFinalPrice(booking));
            result.add(bookingDTO);
            System.out.println(bookingDTO.getFinalPrice());

        }
        return result;
    }

    private double getFinalPrice(Booking booking) {
        double price = booking.getPrice();
        double priceWithDiscount = price - price * booking.getDiscountPercent()/ 100;
        return  priceWithDiscount +  priceWithDiscount * PricingService.TAX / 100;
    }

}
