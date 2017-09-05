package ca.tidygroup.controllers;

import ca.tidygroup.dto.CommercialRequestMassage;
import ca.tidygroup.dto.EmailMessage;
import ca.tidygroup.model.Account;
import ca.tidygroup.service.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    private MailingService mailingService;

    @Autowired
    public MainController(MailingService mailingService) {
        this.mailingService = mailingService;
    }

    @ModelAttribute
    public void bookingModelAttributes(Model model) {
        model.addAttribute("message", new EmailMessage());
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/send-message")
    public String sendMessage(EmailMessage message) {
        mailingService.sendEmailMessage(message);
        return "index";
    }

    @GetMapping("/services")
    public String services() {
        return "services";
    }

    @GetMapping("/standardcleaning")
    public String standardcleaning() {
        return "standardcleaning";
    }

    @GetMapping("/deepcleaning")
    public String deepcleaning() {
        return "deepcleaning";
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

    @GetMapping("/thankyou")
    public String thankyou() {
        return "thankyou";
    }

    @GetMapping("/login")
    public String loginGet(Model model) {
        model.addAttribute("account", new Account());
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("/business")
    public String commercial(Model model) {
        model.addAttribute("request", new CommercialRequestMassage());
        return "business";
    }

    @PostMapping("/business/send-request")
    public String sendCommercialRequest(CommercialRequestMassage message, Model model) {
        model.addAttribute("request", new CommercialRequestMassage());
        mailingService.sendCommercialRequestMessage(message);
        return "business";
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }
}

