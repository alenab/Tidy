package ca.tidygroup.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name = "id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToOne
    @JoinColumn(name="account_id")
    private Account account;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "customer")
    private List<Address> userAddress;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Address> getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(List<Address> userAddress) {
        this.userAddress = userAddress;
    }
}
