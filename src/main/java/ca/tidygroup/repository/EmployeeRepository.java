package ca.tidygroup.repository;

import ca.tidygroup.model.Account;
import ca.tidygroup.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    List<Employee> findAllEmployeeByActive(boolean isActive);
    Employee findEmployeeByAccount(Account account);
    Employee getEmployeeById(long id);
}
