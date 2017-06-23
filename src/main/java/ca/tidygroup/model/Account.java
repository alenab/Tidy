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

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    private Role userRole;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
