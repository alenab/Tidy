package ca.tidygroup.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "rooms_number")
    private int numberOfRooms;

    @Column(name = "bathrooms_number")
    private int numberOfBathrooms;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address addressForClean;

    @Column(name = "cleaning_time")
    private ZonedDateTime cleaningTime;

    @Column(name = "create_booking", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createBooking;

    @ManyToOne
    @JoinColumn(name="plan_id")
    private CleaningPlan cleaningPlan;

    @ManyToMany
    @JoinTable(name = "booking_add_Options",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id"))
    private List<CleaningOption> additionalOptions;

    @Column(name = "special_request")
    private String specialRequest;

    @Column(name = "discount")
    private int discountPercent;

    @Column(name = "price")
    private double price;

    @Column(name="duration")
    private double duration;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "get_in_notes")
    private String getInNotes;

    @Column(name = "admin_notes")
    private String adminNotes;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    public Address getAddressForClean() {
        return addressForClean;
    }

    public void setAddressForClean(Address addressForClean) {
        this.addressForClean = addressForClean;
    }

    public ZonedDateTime getCleaningTime() {
        return cleaningTime;
    }

    public void setCleaningTime(ZonedDateTime cleaningTime) {
        this.cleaningTime = cleaningTime;
    }

    public LocalDateTime getCreateBooking() {
        return createBooking;
    }

    public void setCreateBooking(LocalDateTime createBooking) {
        this.createBooking = createBooking;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public List<CleaningOption> getAdditionalOptions() {
        return additionalOptions;
    }

    public void setAdditionalOptions(List<CleaningOption> additionalOptions) {
        this.additionalOptions = additionalOptions;
    }

    public CleaningPlan getCleaningPlan() {
        return cleaningPlan;
    }

    public void setCleaningPlan(CleaningPlan cleaningPlan) {
        this.cleaningPlan = cleaningPlan;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getGetInNotes() {
        return getInNotes;
    }

    public void setGetInNotes(String getInNotes) {
        this.getInNotes = getInNotes;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "numberOfRooms=" + numberOfRooms +
                ", numberOfBathrooms=" + numberOfBathrooms +
                ", addressForClean=" + addressForClean +
                ", cleaningTime=" + cleaningTime +
                ", createBooking=" + createBooking +
                ", cleaningPlan=" + cleaningPlan +
                ", additionalOptions=" + additionalOptions +
                ", specialRequest='" + specialRequest + '\'' +
                ", discountPercent=" + discountPercent +
                ", price=" + price +
                ", getInNotes='" + getInNotes + '\'' +
                '}';
    }
}
