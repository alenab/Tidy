package ca.tidygroup.repository;

import ca.tidygroup.model.Account;
import ca.tidygroup.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    Employee findEmployeeByAccount(Account account);
}
