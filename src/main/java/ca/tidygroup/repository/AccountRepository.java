package ca.tidygroup.repository;

import ca.tidygroup.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByLoginIgnoringCase(String login);
    Account findAccountByEmailIgnoreCase(String email);
}
