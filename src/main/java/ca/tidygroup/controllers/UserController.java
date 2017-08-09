package ca.tidygroup.controllers;

import ca.tidygroup.manager.BookingManager;
import ca.tidygroup.model.Booking;
import ca.tidygroup.model.SecurityUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Secured("USER")
@Controller
@RequestMapping("/user")
public class UserController {

    private BookingManager bookingManager;

    @Autowired
    public UserController(BookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }

    private SecurityUserDetails getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return  (SecurityUserDetails) authentication.getPrincipal();
    }

    @GetMapping("/")
    public ModelAndView goToUsersSpace() {
        ModelAndView mv = new ModelAndView("user/user-test");
        List<Booking> bookings = bookingManager.getBookingsFor(getCurrentUser().getId());
        mv.addObject("bookings", bookings);
        return mv;
    }
}
