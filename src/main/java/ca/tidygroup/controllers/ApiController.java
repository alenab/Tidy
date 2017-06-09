package ca.tidygroup.controllers;

import ca.tidygroup.dto.ApartmentUnitDTO;
import ca.tidygroup.service.PricingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RestController
public class ApiController {

    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    private PricingService pricingService;

    @Autowired
    public ApiController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @GetMapping("/price-matrix")
    public List<ApartmentUnitDTO> getAllPriceOptions() {
        if (log.isDebugEnabled()) {
            log.debug("All prices options requested");
        }
        return pricingService.getAllApartmentUnits();
    }
}
