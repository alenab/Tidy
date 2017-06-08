package ca.tidygroup.controllers;

import ca.tidygroup.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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
    public String prices() {
        return "admin/prices";
    }
}
