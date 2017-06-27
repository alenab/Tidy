package ca.tidygroup.controllers;

import ca.tidygroup.dto.ApartmentUnitListDTO;
import ca.tidygroup.dto.BookingDTOAdmin;
import ca.tidygroup.dto.EmployeeDTO;
import ca.tidygroup.dto.OptionListDTO;
import ca.tidygroup.repository.EmployeeRepository;
import ca.tidygroup.service.AdminService;
import ca.tidygroup.service.BookingService;
import ca.tidygroup.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private AdminService adminService;
    private PricingService pricingService;
    private BookingService bookingService;

    @Autowired
    public AdminController(AdminService adminService, PricingService pricingService, BookingService bookingService) {
        this.adminService = adminService;
        this.pricingService = pricingService;
        this.bookingService = bookingService;
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

    @GetMapping("/bookings/{id}")
    public String changeBooking(@PathVariable("id") long id, Model model) {
        BookingDTOAdmin dto = adminService.getBookingById(id);
        model.addAttribute("adminBooking", dto);
        model.addAttribute("allOptions", bookingService.getAllCleaningOptions());
        model.addAttribute("allPlans", bookingService.getAllCleaningPlans());
        model.addAttribute("allBedrooms", bookingService.getListOfBedrooms());
        return "admin/change_booking";
    }

    @PostMapping("/bookings/{id}/save")
    public String saveChangesOfBooking(@PathVariable("id") long id, BookingDTOAdmin bookingDTOAdmin) {
        bookingService.updateBooking(id, bookingDTOAdmin);
        return "redirect:/admin/bookings";
    }

    @GetMapping("/employee")
    public String employees(Model model) {
        model.addAttribute("allEmployees", adminService.getAllEmployeeDTO());
        return "admin/employee";
    }

}
