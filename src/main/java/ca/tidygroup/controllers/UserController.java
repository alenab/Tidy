package ca.tidygroup.controllers;

import ca.tidygroup.manager.AccountManager;
import ca.tidygroup.model.Customer;
import ca.tidygroup.model.SecurityUserDetails;
import ca.tidygroup.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Secured("USER")
@Controller
@RequestMapping("/user")
public class UserController {

    private AdminService adminService;

    private AccountManager accountManager;

   @Autowired
    public UserController(AdminService adminService, AccountManager accountManager) {
        this.adminService = adminService;
       this.accountManager = accountManager;
   }

    private SecurityUserDetails getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return  (SecurityUserDetails) authentication.getPrincipal();
    }

    @GetMapping("/")
    public String goToUsersSpace(Model model) {
        Customer customer = accountManager.getCustomer(getCurrentUser().getId());
        model.addAttribute("allCustomerBookings", adminService.getCustomerBookings(customer));
        model.addAttribute("allCards", adminService.getCustomersDTO(customer).getCards());
        return "user/user";
    }
}
