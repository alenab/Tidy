package ca.tidygroup.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "account", uniqueConstraints = @UniqueConstraint(columnNames = {"email", "login"}))
public class Account implements Serializable {

    private static final long serialVersionUID = -1234033283573161796L;

    @Id
    @Column(name = "id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany
    @JoinColumn(name = "account_id")
    private List<Address> userAddress;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role userRole;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role role) {
        this.userRole = role;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getId() == account.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
