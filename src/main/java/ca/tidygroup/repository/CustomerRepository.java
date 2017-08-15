package ca.tidygroup.repository;

import ca.tidygroup.model.Account;
import ca.tidygroup.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findCustomerByAccount(Account account);
    Customer findById(long id);
    Customer findByAccount_Id(long accountId);
}
