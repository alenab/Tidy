package ca.tidygroup.controllers;

import ca.tidygroup.dto.ApartmentUnitDTO;
import ca.tidygroup.model.CleaningOption;
import ca.tidygroup.service.BookingService;
import ca.tidygroup.service.PricingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/free-time/{date}")
    public List<String> getFreeTime(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return bookingService.getFreeTime(date).stream()
                .map(time -> time.format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toList());
    }

    @GetMapping("/options/{planId}")
    public List<CleaningOption> getOptionsByPlan(@PathVariable Long planId) {
        return bookingService.getCleaningOptionsByPlanId(planId);

    }
}
