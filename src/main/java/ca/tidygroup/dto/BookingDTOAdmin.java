package ca.tidygroup.dto;

import ca.tidygroup.model.CleaningOption;
import ca.tidygroup.model.CleaningPlan;
import ca.tidygroup.model.Status;

import java.util.List;

public class BookingDTOAdmin {

    private long id;

    private CleaningPlan cleaningPlan;

    private int numberOfRooms;

    private int numberOfBathrooms;

    private List<CleaningOption> cleaningOptions;

    private String specialRequest;

    private String cleaningDate;

    private String cleaningTime;

    private String address;

    private String aptNumber;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private int discount;

    private double price;

    private double finalPrice;

    private Long employeeId;

    private double duration;

    private Status status;

    private String getInNotes;

    private String adminNotes;

    private String billingCustomerId;

    private String plannedTime;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CleaningPlan getCleaningPlan() {
        return cleaningPlan;
    }

    public void setCleaningPlan(CleaningPlan cleaningPlan) {
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

    public List<CleaningOption> getCleaningOptions() {
        return cleaningOptions;
    }

    public void setCleaningOptions(List<CleaningOption> cleaningOptions) {
        this.cleaningOptions = cleaningOptions;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAptNumber() {
        return aptNumber;
    }

    public void setAptNumber(String aptNumber) {
        this.aptNumber = aptNumber;
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

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public double getDuration() {
        return duration;
    }

    public String getGetInNotes() {
        return getInNotes;
    }

    public void setGetInNotes(String getInNotes) {
        this.getInNotes = getInNotes;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getBillingCustomerId() {
        return billingCustomerId;
    }

    public String getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(String plannedTime) {
        this.plannedTime = plannedTime;
    }

    public void setBillingCustomerId(String billingCustomerId) {
        this.billingCustomerId = billingCustomerId;
    }

    @Override
    public String toString() {
        return "BookingDTOAdmin{" +
                "id=" + id +
                ", cleaningPlan=" + cleaningPlan +
                ", numberOfRooms=" + numberOfRooms +
                ", numberOfBathrooms=" + numberOfBathrooms +
                ", cleaningOptions=" + cleaningOptions +
                ", specialRequest='" + specialRequest + '\'' +
                ", cleaningDate='" + cleaningDate + '\'' +
                ", cleaningTime='" + cleaningTime + '\'' +
                ", address='" + address + '\'' +
                ", aptNumber='" + aptNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", discount=" + discount +
                ", price=" + price +
                ", finalPrice=" + finalPrice +
                ", employee='" + employeeId + '\'' +
                ", duration=" + duration + '\'' +
                ", getInNotes=" + getInNotes + '\'' +
                '}';
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }
}