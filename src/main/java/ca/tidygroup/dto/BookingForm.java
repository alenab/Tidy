package ca.tidygroup.dto;

import ca.tidygroup.model.CleaningOption;
import ca.tidygroup.model.CleaningPlan;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;
import java.util.List;

public class BookingForm {

    @NotBlank
    @Size(min = 2, max = 8)
    private String postCode;

    @NotBlank
    private String city;

    @NotBlank
    private String address;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    @Min(0)
    @Max(5)
    private String numberOfRooms;

    @Min(1)
    @Max(4)
    private String numberOfBathrooms;

    private String specialRequest;

    private String discount;

    @NotBlank
    private String cleaningDate;

    @NotBlank
    private String cleaningTime;

    private List<CleaningOption> cleaningOptions;

    private CleaningPlan cleaningPlan;

    private String price;

    public BookingForm() {
        setCity("Vancouver");
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(String numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public String getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(String numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCleaningDate() {
        return cleaningDate;
    }

    public void setCleaningDate(String cleaningDate) {
        this.cleaningDate = cleaningDate;
    }

    public String getCleaningTime() {
        return cleaningTime;
    }

    public void setCleaningTime(String cleaningTime) {
        this.cleaningTime = cleaningTime;
    }

    public List<CleaningOption> getCleaningOptions() {
        return cleaningOptions;
    }

    public void setCleaningOptions(List<CleaningOption> cleaningOptions) {
        this.cleaningOptions = cleaningOptions;
    }

    public CleaningPlan getCleaningPlan() {
        return cleaningPlan;
    }

    public void setCleaningPlan(CleaningPlan cleaningPlan) {
        this.cleaningPlan = cleaningPlan;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BookingForm{" +
                "postCode='" + postCode + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", numberOfRooms='" + numberOfRooms + '\'' +
                ", numberOfBathrooms='" + numberOfBathrooms + '\'' +
                ", specialRequest='" + specialRequest + '\'' +
                ", discount='" + discount + '\'' +
                ", cleaningDate='" + cleaningDate + '\'' +
                ", cleaningTime='" + cleaningTime + '\'' +
                ", cleaningOptions=" + cleaningOptions +
                ", cleaningPlan=" + cleaningPlan +
                ", price='" + price + '\'' +
                '}';
    }
}
