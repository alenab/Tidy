package ca.tidygroup.service;

import com.squareup.connect.ApiClient;
import com.squareup.connect.ApiException;
import com.squareup.connect.Configuration;
import com.squareup.connect.api.CustomersApi;
import com.squareup.connect.api.LocationsApi;
import com.squareup.connect.api.TransactionsApi;
import com.squareup.connect.auth.OAuth;
import com.squareup.connect.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class BillingService {

    private static final Logger log = LoggerFactory.getLogger(BillingService.class);

    @Value("${squareup.application.id}")
    private String applicationId;

    @Value("${squareup.access.token}")
    private String accessToken;

    private String locationId = null;

    private CustomersApi customersApi;

    private TransactionsApi transactionsApi;

    @PostConstruct
    public void init() {
        ApiClient client = Configuration.getDefaultApiClient();
        OAuth oauth2 = (OAuth) client.getAuthentication("oauth2");
        oauth2.setAccessToken(accessToken);

        customersApi = new CustomersApi();
        transactionsApi = new TransactionsApi();
    }

    public String getApplicationId() {
        return applicationId;
    }

    private synchronized String getLocationId() {
        if (locationId == null) {
            LocationsApi locationsApi = new LocationsApi();
            try {
                ListLocationsResponse response = locationsApi.listLocations();
                List<Location> list = response.getLocations();
                if (list.size() > 0) {
                    locationId = list.stream()
                            .filter(loc -> loc.getCapabilities().contains(Location.CapabilitiesEnum.PROCESSING))
                            .findAny()
                            .orElseThrow(RuntimeException::new).getId();
                }
            } catch (ApiException e) {
                log.error("Error at attempt to get location", e);
            }
        }
        log.debug("Going to use locationId: {}", locationId);
        return locationId;
    }

    /*
     * returns customer id
     */
    public String createCustomer(String email, long tidyCustomerId) {
        CreateCustomerRequest request = new CreateCustomerRequest();
        try {
            CreateCustomerResponse response = customersApi.createCustomer(request.emailAddress(email)
                    .referenceId(String.valueOf(tidyCustomerId)));
            // todo handle errors
            return response.getCustomer().getId();
        } catch (ApiException e) {
            log.error("Error during customer creation", e);
        }
        return null;
    }

    /*
     * returns card id
     */
    public String createCustomerCard(String customerId, String cardNonce) {
        CreateCustomerCardRequest body = new CreateCustomerCardRequest();
        body.cardNonce(cardNonce);
        try {
            CreateCustomerCardResponse response = customersApi.createCustomerCard(customerId, body);
            // todo handle errors
            return response.getCard().getId();
        } catch (ApiException e) {
            log.error("Error during customer card creation", e);
        }
        return null;
    }

    public List<Customer> listCustomers() {
        try {
            // todo handle errors and cursor
            return customersApi.listCustomers(null).getCustomers();
        } catch (ApiException e) {
            log.error("Error at attempt to list customers", e);
        }
        return Collections.emptyList();
    }

    public String charge(long amount, String email) {
        ChargeRequest body = new ChargeRequest();

        Money money = new Money();
        money.currency(Money.CurrencyEnum.CAD);
        money.amount(amount * 100L); // amount is set in cents
        body.amountMoney(money);

        // unique key associated with transaction, could be safely use to retry transaction and avoid double payment
        String idempotencyKey = UUID.randomUUID().toString();
        body.idempotencyKey(idempotencyKey);

        String customerId = getCustomerId(email);
        body.customerId(customerId);
        String customerCardId = getCustomerCardId(customerId);
        body.customerCardId(customerCardId);

        log.debug("Going to charge customer: {} with card: {}. Amount: {}. Email: {}", customerId, customerCardId,
                amount, email);
        try {
            // todo handle errors
            return transactionsApi.charge(getLocationId(), body).getTransaction().toString();
        } catch (ApiException e) {
            log.error("Error during charging", e);
        }
        return "null"; // fixme
    }

    /*
     * Method for one time purchase without customer creation
     */
    public String charge(long amount, String cardNonce, String email) {
        ChargeRequest body = new ChargeRequest();

        Money money = new Money();
        money.currency(Money.CurrencyEnum.CAD);
        money.amount(amount * 100L); // amount is set in cents
        body.amountMoney(money);

        body.cardNonce(cardNonce);

        String idempotencyKey = UUID.randomUUID().toString();
        body.idempotencyKey(idempotencyKey);

        if (email != null && !email.isEmpty()) {
            body.buyerEmailAddress(email);
        }

        ChargeResponse response = null;
        try {
            // todo handle errors
            response = transactionsApi.charge(getLocationId(), body);
        } catch (ApiException e) {
            log.error("Error during charging", e);
        }
        return response != null ? response.toString() : "null"; // fixme
    }

    private String getCustomerCardId(String customerId) {
        try {
            List<Card> cards = customersApi.retrieveCustomer(customerId).getCustomer().getCards();
            if (cards.size() > 0) {
                return cards.get(0).getId();
            }
        } catch (ApiException e) {
            log.error("Error during getting existing customer info: {}. Ex: {}", customerId, e.getMessage());
        }
        return null;
    }

    private String getCustomerId(String email) {
        try {
            String cursor = null;
            do {
                ListCustomersResponse response = customersApi.listCustomers(cursor);
                String id = response.getCustomers().stream()
                        .filter(cus -> cus.getEmailAddress().equalsIgnoreCase(email))
                        .map(Customer::getId)
                        .findAny()
                        .orElse(null);
                if (id != null) {
                    return id;
                }
                cursor = response.getCursor();
            } while (cursor != null);
        } catch (ApiException e) {
            log.error("Error during searching for user by email: {}. Ex: {}", email, e.getMessage());
        }
        return null;
    }

    public List<Transaction> listTransactions() {
        try {
            ListTransactionsResponse response = transactionsApi.listTransactions(getLocationId(), null, null, null, null);
            return response.getTransactions();
        } catch (ApiException e) {
            log.error("Error during listing transactions", e);
        }
        return Collections.emptyList();
    }
}
