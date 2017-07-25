package ca.tidygroup.controllers;

import ca.tidygroup.service.BillingService;
import com.squareup.connect.models.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/billing")
public class BillingController {

    private static final Logger log = LoggerFactory.getLogger(BillingController.class);

    private BillingService billingService;

    @Autowired
    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @ModelAttribute
    public void modelDefaults(Model model) {
        model.addAttribute("applicationId", billingService.getApplicationId());
        model.addAttribute("transactions", billingService.listTransactions());
        model.addAttribute("listCustomers", billingService.listCustomers());
    }

    @GetMapping
    public String testing() {
        return "billing/testing";
    }

    @PostMapping("/process-card")
    public String processCard(@ModelAttribute(name = "nonce") String cardNonce,
                              @ModelAttribute(name = "email") String email,
                              BindingResult result) {
        if (result.hasErrors()) {
            log.error("Binding errors: {}", result.getAllErrors());
            return "redirect:/admin/billing?hasErrors";
        }
        String customerId = billingService.createCustomer(email, 46L); // FIXME
        log.debug("Customer is created: {}", customerId);
        String cardId = billingService.createCustomerCard(customerId, cardNonce);
        log.debug("Card is created: {}", cardId);
        return "redirect:/admin/billing";
    }

    @PostMapping("/purchase")
    public String purchase(@ModelAttribute(name = "amount") Integer amount,
                           @ModelAttribute(name = "email") String email,
                           BindingResult result) {
        if (result.hasErrors()) {
            log.error("Binding errors: {}", result.getAllErrors());
            return "redirect:/admin/billing?hasErrors";
        }
        String response = billingService.charge(amount, email);
        log.debug("Purchase attempt: {}", response);
        return "redirect:/admin/billing";
    }

    @PostMapping("/bill")
    public String bill(@ModelAttribute(name = "price") Double amount,
                       @ModelAttribute(name = "billingCustomerId") String billingCustomerId,
                       BindingResult result) {
        if (result.hasErrors()) {
            log.error("Binding errors: {}", result.getAllErrors());
            return "redirect:/admin/bookings?binding-errors";
        }
        String transactionId = billingService.bill(amount, billingCustomerId);
        Transaction transaction = billingService.getTransaction(transactionId);
        String status = "error";
        if (transaction != null) {
            log.debug("Transaction: {}", transaction);
            status = transaction.getTenders().get(0).getCardDetails().getStatus().toString();
        }
        return "redirect:/admin/bookings?" + status;
    }
}
