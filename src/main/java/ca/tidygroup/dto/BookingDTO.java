package ca.tidygroup.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BookingDTO {

    private long id;

    private String cleaningPlan;

    private int numberOfRooms;

    private int numberOfBathrooms;

    private List<String> cleaningOptions;

    private String specialRequest;

    private LocalDateTime cleaningTime;

    private String postcode;

    private String city;

    private String address;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private int discount;

    private double price;

    private double finalPrice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCleaningPlan() {
        return cleaningPlan;
    }

    public void setCleaningPlan(String cleaningPlan) {
        this.cleaningPlan = cleaningPlan;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(int numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public List<String> getCleaningOptions() {
        return cleaningOptions;
    }

    public void setCleaningOptions(List<String> cleaningOptions) {
        this.cleaningOptions = cleaningOptions;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public LocalDateTime getCleaningTime() {
        return cleaningTime;
    }

    public void setCleaningTime(LocalDateTime cleaningTime) {
        this.cleaningTime = cleaningTime;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }
}