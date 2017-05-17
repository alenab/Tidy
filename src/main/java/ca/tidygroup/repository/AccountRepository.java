package ca.tidygroup.repository;

import ca.tidygroup.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Alena on 15.05.2017.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
