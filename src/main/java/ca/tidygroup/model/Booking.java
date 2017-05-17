package ca.tidygroup.model;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table (name = "booking")
public class Booking {

    public static final int TAX = 12;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

//    private Account account;

    @Column(name = "rooms_number")
    private int numberOfRooms;

    @Column(name = "bathrooms_number")
    private int numberOfBathrooms;


//    private Address addressForClean;

    @Column(name="cleaning_time")
    private ZonedDateTime cleaningTime;

    @Column(name = "booking_time")
    private ZonedDateTime bookingTime;

//    private CleaningPlan cleaningPlan;
//    private List<CleaningOption> additionalOptions;

    @Column(name = "special_request")
    private String specialRequest;

    private int discountPercent;

    private double price;

    public double getPriceWithDiscount() {
        return price - price * discountPercent / 100;
    }

    public double getFinalPrice() {
        double price = getPriceWithDiscount();
        return price + price * TAX / 100;
    }

}
