package ca.tidygroup.manager;

import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.model.Account;
import ca.tidygroup.model.Address;
import ca.tidygroup.model.Customer;
import ca.tidygroup.model.Role;
import ca.tidygroup.repository.AccountRepository;
import ca.tidygroup.repository.AddressRepository;
import ca.tidygroup.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class AccountManager {

    private AddressRepository addressRepository;

    private AccountRepository accountRepository;

    private CustomerRepository customerRepository;

    @Autowired
    public AccountManager(AddressRepository addressRepository, AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer getCustomer(BookingForm form) {
        Account account = accountRepository.findAccountByEmailIgnoreCase(form.getEmail());
        if (account == null) {
            account = createNewAccount(form.getEmail(), Role.USER);
        } else {
            if (account.getUserRole() != Role.USER) {
                throw new RuntimeException("Unfortunately specified email belongs to non-user account!");
            }
        }

        return createOrUpdateCustomer(account, form.getFirstName(), form.getLastName(), form.getPhone());
    }

    @Transactional
    public Address getAddress(BookingForm form) {
        Customer customer = getCustomer(form);
        List<Address> addresses = customer.getUserAddress();
        if (addresses != null) {
            for (Address address : addresses) {
                if (address.getAddress().equalsIgnoreCase(form.getAddress())) {
                    String aptNumber = Optional.ofNullable(address.getAptNumber()).orElse("");
                    if (aptNumber.equalsIgnoreCase(form.getAptNumber())) {
                        return address;
                    }
                }
            }
        }

        Address addr = new Address();
        addr.setAddress(form.getAddress());
        addr.setAptNumber(form.getAptNumber());
        addr.setCustomer(customer);
        return addressRepository.save(addr);
    }

    private Customer createOrUpdateCustomer(Account existingAccount, String fName, String lName, String phone) {
        Customer customer = customerRepository.findCustomerByAccount(existingAccount);
        if (customer == null) {
            customer = new Customer();
        }
        customer.setAccount(existingAccount);
        customer.setFirstName(fName);
        customer.setLastName(lName);
        customer.setPhoneNumber(phone);
        return customerRepository.save(customer);
    }

    private Account createNewAccount(String email, Role role) {
        final String lowerEmail = email.toLowerCase();
        Account account = new Account();
        account.setEmail(lowerEmail);
        account.setLogin(lowerEmail);
        account.setUserRole(role);
        return accountRepository.save(account);
    }
}
