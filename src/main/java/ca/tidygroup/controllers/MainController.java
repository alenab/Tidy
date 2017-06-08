package ca.tidygroup.controllers;

import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class MainController {

    private BookingService bookingService;

    @Autowired
    public MainController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/services")
    public String services() {
        return "services";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/careers")
    public String careers() {
        return "careers";
    }

    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }

    @GetMapping("/book")
    public String book(Model model) {
        model.addAttribute("booking", new BookingForm());
        model.addAttribute("allOptions", bookingService.getAllCleaningOptions());
        model.addAttribute("allPlans", bookingService.getAllCleaningPlans());
        return "book";
    }

    @GetMapping("/thankyou")
    public String thankyou() {
        return "thankyou";
    }

    @PostMapping("/book")
    public String bookForm(@Valid @ModelAttribute("booking") BookingForm booking, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "book";
        }
        bookingService.add(booking);
        return "thankyou";
    }


}

