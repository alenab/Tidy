package ca.tidygroup.manager;

import ca.tidygroup.dto.BookingForm;
import ca.tidygroup.model.Address;
import ca.tidygroup.model.Customer;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountManagerTest {

    @Autowired
    private AccountManager manager;

    @MockBean
    private BookingForm form;

    @Test
    public void checkGetCustomerUsingNotExistingEmail() {
        String email = "not-existing@mail.com";
        when(form.getEmail()).thenReturn(email);
        Customer customer = manager.getCustomer(form);
        Assert.assertEquals(email, customer.getAccount().getEmail());
    }

    @Test
    public void checkGetCustomerUsingExistingEmail() {
        String email = "existing@mail.com";
        when(form.getEmail()).thenReturn(email);

        Customer customer = manager.getCustomer(form);
        Assume.assumeThat(customer.getAccount().getEmail(), Matchers.equalTo(email));

        Assert.assertEquals(customer.getId(), manager.getCustomer(form).getId());
    }

    @Test
    public void checkGetNewAddressForNewCustomer() {
        final String email = "new-customer@mail.com";
        final String expectedAddress = "Address we are looking for";
        when(form.getEmail()).thenReturn(email);
        when(form.getAddress()).thenReturn(expectedAddress);

        Address address = manager.getAddress(form);
        Assert.assertNotNull(address);

        Assert.assertEquals(expectedAddress, address.getAddress());
    }

    @Test
    public void checkGetExistingAddressForCustomer() {
        final String email = "existing-customer@mail.com";
        final String[] expectedAddress = {
                "New York, Address 1",
                "Boston, Address 2",
                "Vancouver, Address 3"
        };
        when(form.getEmail()).thenReturn(email);
        when(form.getAptNumber()).thenReturn("");
        when(form.getAddress())
                .thenReturn(expectedAddress[0])
                .thenReturn(expectedAddress[1])
                .thenReturn(expectedAddress[2])
                .thenReturn(expectedAddress[0]);

        for (int i = 0; i < expectedAddress.length; i++) {
            manager.getAddress(form);
        }

        Address address = manager.getAddress(form);
        Assert.assertNotNull(address);

        Assert.assertEquals(expectedAddress[0], address.getAddress());
    }
}
