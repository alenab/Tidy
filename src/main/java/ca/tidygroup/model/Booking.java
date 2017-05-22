package ca.tidygroup.model;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Table (name = "booking")
public class Booking {

    public static final int TAX = 12;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "rooms_number")
    private int numberOfRooms;

    @Column(name = "bathrooms_number")
    private int numberOfBathrooms;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address addressForClean;

    @Column(name="cleaning_time")
    private ZonedDateTime cleaningTime;

    @Column(name = "create_booking", insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createBooking;

//    private CleaningPlan cleaningPlan;
//    private List<CleaningOption> additionalOptions;

    @Column(name = "special_request")
    private String specialRequest;

    @Column(name = "discount")
    private int discountPercent;

    private double price;

    public double getPriceWithDiscount() {
        return price - price * discountPercent / 100;
    }

    public double getFinalPrice() {
        double price = getPriceWithDiscount();
        return price + price * TAX / 100;
    }

    public static int getTAX() {
        return TAX;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
