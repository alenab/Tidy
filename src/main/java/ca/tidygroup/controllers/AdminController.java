package ca.tidygroup.controllers;

import ca.tidygroup.dto.*;
import ca.tidygroup.service.AdminService;
import ca.tidygroup.service.BookingService;
import ca.tidygroup.service.PricingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

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
        dto.setOptions(new ArrayList<>(bookingService.getAllCleaningOptions()));
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
        model.addAttribute("employees", adminService.getEmployees());
        return "admin/change_booking";
    }

    @PostMapping("/bookings/{id}/save")
    public String saveChangesOfBooking(@PathVariable("id") long id, BookingDTOAdmin bookingDTOAdmin) {
        bookingService.updateBooking(id, bookingDTOAdmin);
        return "redirect:/admin/bookings";
    }

    @PostMapping("/booking/{id}/cancel")
    public String cancelBooking(@PathVariable("id") long id) {
        bookingService.cancelledBooking(id);
        return "redirect:/admin/bookings";
    }

    @GetMapping("/employee")
    public String employees(Model model) {
        model.addAttribute("allEmployees", adminService.getAllEmployeeDTO());
        return "admin/employee";
    }

    @GetMapping("/employee/add")
    public String addEmployee(Model model) {
        model.addAttribute("employee", new EmployeeDTO());
        return "admin/add_employee";
    }

    @PostMapping("/employee/save")
    public String saveEmployee(@Valid @ModelAttribute("employee") EmployeeDTO employeeDTO, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("There were binding errors at attempt to process employee: {}", bindingResult.getAllErrors());
            return "admin/employee";
        }
        adminService.addEmployee(employeeDTO);
        model.addAttribute("allEmployees", adminService.getAllEmployeeDTO());
        return "redirect:/admin/employee";
    }

    @GetMapping("/employee/{id}")
    public String changeEmployee(@PathVariable("id") long id, Model model) {
        EmployeeDTO employeeDTO = adminService.getEmployeeById(id);
        model.addAttribute("employee", employeeDTO);
        return "admin/change_employee";
    }

    @PostMapping("/employee/{id}/save")
    public String saveChangesOfEmployee(@PathVariable("id") long id, EmployeeDTO employeeDTO) {
        adminService.updateEmployee(id, employeeDTO);
        return "redirect:/admin/employee";
    }

    @PostMapping("/employee/{id}/delete")
    public String deleteEmployee(@PathVariable("id") long id) {
        adminService.deleteEmployee(id);
        return "redirect:/admin/employee";
    }

    @GetMapping("/employee/archive")
    public String archiveOfEmployee(Model model) {
        model.addAttribute("archiveOfEmployees", adminService.getNoActiveEmployeeDTO());
        return "admin/archive_employees";
    }

    @GetMapping("/customer")
    public String customer(Model model) {
        model.addAttribute("allCustomers", adminService.getAllCustomersDTO());
        return "admin/customer";
    }

    @GetMapping("/working_hours")
    public String workingHours(Model model) {
        model.addAttribute("workingHoursDTO", adminService.getWorkingHoursDTO());
        return "admin/working_hours";
    }

    @PostMapping("/working_hours-save")
    public String saveWorkingHours(@Valid @ModelAttribute("workingHoursDTO") WorkingHoursDTO workingHoursDTO) {
        adminService.updateWorkingHours(workingHoursDTO);
        return "redirect:/admin/";
    }

    @GetMapping("/time_limitations")
    public String timeLimitations() {
        return "admin/time_limitations";
    }

    @GetMapping("/discount")
    public String discount(Model model) {
        model.addAttribute("allCodes", adminService.getAllDiscounts());
        return "admin/discount";
    }

    @GetMapping("/discount/add")
    public String addDiscount(Model model) {
        model.addAttribute("discount", new DiscountDTO());
        return "admin/add_discount";
    }

    @PostMapping("/discount/save")
    public String saveDiscount(@Valid @ModelAttribute("discount") DiscountDTO discountDTO, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("There were binding errors at attempt to process discount: {}", bindingResult.getAllErrors());
            return "admin/employee";
        }
        adminService.addDiscount(discountDTO);
        model.addAttribute("allCodes", adminService.getAllDiscounts());
        return "redirect:/admin/discount";
    }

    @GetMapping("/discount/{id}")
    public String changeDiscountStatus(@PathVariable("id") long id, Model model) {
        DiscountDTO discountDTO = adminService.getDiscountById(id);
        model.addAttribute("discount", discountDTO);
        return "admin/change_discount";
    }

    @PostMapping("/discount/{id}/save")
    public String saveChangesOfDiscount(@PathVariable("id") long id, DiscountDTO discountDTO) {
        adminService.updateDiscountStatus(id, discountDTO);
        return "redirect:/admin/discount";
    }
}
