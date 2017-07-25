package ca.tidygroup.manager;

import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.model.Account;
import ca.tidygroup.model.Role;
import ca.tidygroup.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountManagerTestNegative {

    private static final String EMAIL = "some-random@employee.com";

    @Autowired
    private AccountManager manager;

    @MockBean
    private BookingForm form;

    @MockBean
    private Account employee;

    @MockBean
    private AccountRepository repository;

    @Before
    public void before() {
        when(employee.getUserRole()).thenReturn(Role.EMPLOYEE);
        when(employee.getEmail()).thenReturn(EMAIL);
        when(employee.getLogin()).thenReturn(EMAIL);
        when(repository.findAccountByEmailIgnoreCase(EMAIL)).thenReturn(employee);
        when(form.getEmail()).thenReturn(EMAIL);
    }

    @Test(expected = RuntimeException.class)
    public void checkBookingAttemptUsingNotUserAccount() {
        manager.getCustomer(form);
    }
}
