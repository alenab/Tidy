package ca.tidygroup.controllers;

import ca.tidygroup.dto.BookingDTOCustomer;
import ca.tidygroup.dto.CustomerDTO;
import ca.tidygroup.manager.AccountManager;
import ca.tidygroup.model.Customer;
import ca.tidygroup.model.SecurityUserDetails;
import ca.tidygroup.service.AdminService;
import ca.tidygroup.service.BookingService;
import ca.tidygroup.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Secured("USER")
@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${tidy.google.api.key}")
    private String googleApiKey;

    private AdminService adminService;

    private AccountManager accountManager;

    private UserService userService;

    private BookingService bookingService;

    @Autowired
    public UserController(AdminService adminService, AccountManager accountManager, UserService userService, BookingService bookingService) {
        this.adminService = adminService;
        this.accountManager = accountManager;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    private SecurityUserDetails getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return (SecurityUserDetails) authentication.getPrincipal();
    }

    @GetMapping("/")
    public String goToUsersSpace(Model model) {
        Customer customer = accountManager.getCustomer(getCurrentUser().getId());
        CustomerDTO customerDTO = adminService.getCustomersDTO(customer);
        model.addAttribute("customer", customerDTO);
        model.addAttribute("allCustomerBookings", userService.getCustomerBookings(customer));
        model.addAttribute("allCards", adminService.getCustomersDTO(customer).getCards());
        return "user/user";
    }

    @GetMapping("/{id}")
    public String changeBooking(@PathVariable("id") Long id, Model model) {
        BookingDTOCustomer dto = userService.getBookingByID(id);
        model.addAttribute("userBooking", dto);
        model.addAttribute("allOptions", bookingService.getAllCleaningOptions());
        model.addAttribute("allPlans", bookingService.getAllCleaningPlans());
        model.addAttribute("allBedrooms", bookingService.getListOfBedrooms());
        model.addAttribute("googleApiKey", googleApiKey);
        model.addAttribute("paymentMethods", bookingService.getAllPaymentMethods());
        return "user/change_booking";
    }

    @PostMapping("/{id}/save")
    public String saveChangesOfBooking(@PathVariable("id") long id, BookingDTOCustomer bookingDTOCustomer) {
        userService.updateBooking(id, bookingDTOCustomer);
        return "redirect:/user/";
    }
}