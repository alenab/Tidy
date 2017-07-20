package ca.tidygroup.controllers;

import ca.tidygroup.dto.ApartmentUnitDTO;
import ca.tidygroup.service.BookingService;
import ca.tidygroup.service.PricingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@RestController
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    private PricingService pricingService;
    private BookingService bookingService;

    @Autowired
    public ApiController(PricingService pricingService, BookingService bookingService) {
        this.pricingService = pricingService;
        this.bookingService = bookingService;
    }

    @GetMapping("/price-matrix")
    public List<ApartmentUnitDTO> getAllPriceOptions() {
        if (log.isDebugEnabled()) {
            log.debug("All prices options requested");
        }
        return pricingService.getAllApartmentUnits();
    }

    @GetMapping("/getFreeTime/{date}")
    public List<String> getFreeTime(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate date) {
        List<LocalTime> timesList =  bookingService.getFreeTime(date);
        List<String> result = new ArrayList<>();
        for(LocalTime time : timesList) {
            result.add(time.toString());
        }
        return result;
    }
}
