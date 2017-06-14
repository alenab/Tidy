package ca.tidygroup.controllers;

import ca.tidygroup.dto.ApartmentUnitListDTO;
import ca.tidygroup.dto.OptionListDTO;
import ca.tidygroup.service.AdminService;
import ca.tidygroup.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private AdminService adminService;
    private PricingService pricingService;

    @Autowired
    public AdminController(AdminService adminService, PricingService pricingService) {
        this.adminService = adminService;
        this.pricingService = pricingService;
    }

    @GetMapping("/")
    public String admin() {
        return "admin/admin";
    }

    @GetMapping("/bookings")
    public String bookings(Model model) {
        model.addAttribute("allBookings", adminService.getAllBookings());
        return "admin/bookings";
    }


    @GetMapping("/prices")
    public String prices(Model model) {
        ApartmentUnitListDTO dto = new ApartmentUnitListDTO();
        dto.setUnits(new ArrayList<>(pricingService.getAllApartUnits()));
        model.addAttribute("allUnits", dto);
        return "admin/prices";
    }

    @PostMapping("/prices-save")
    public String savePrices(@ModelAttribute("allUnits") ApartmentUnitListDTO allUnits) {
        pricingService.updatePrices(allUnits);
        return "admin/admin";
    }

    @GetMapping("/options_prices")
    public String optionsPrices(Model model) {
        OptionListDTO dto = new OptionListDTO();
        dto.setOptions(new ArrayList<>(pricingService.getAllOptions()));
        model.addAttribute("allOptions", dto);
        return "admin/options_prices";
    }

    @PostMapping("/options-prices-save")
    public String savePrices(@ModelAttribute("allOptions") OptionListDTO allOptions) {
        pricingService.updateOptionsPrices(allOptions);
        return "admin/admin";
    }

}
