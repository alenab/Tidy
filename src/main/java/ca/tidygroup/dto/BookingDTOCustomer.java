package ca.tidygroup.dto;

import ca.tidygroup.model.CleaningOption;
import ca.tidygroup.model.CleaningPlan;
import ca.tidygroup.model.PaymentMethod;
import ca.tidygroup.model.Status;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

public class BookingDTOCustomer {

    private long id;

    private CleaningPlan cleaningPlan;

    @Min(0)
    @Max(5)
    private int numberOfRooms;

    @Min(1)
    @Max(4)
    private int numberOfBathrooms;

    private List<CleaningOption> cleaningOptions;

    private String specialRequest;

    @NotBlank
    private String cleaningDate;

    @NotBlank
    private String cleaningTime;

    @NotBlank
    private String address;

    private String aptNumber;

    private double price;

    private double finalPrice;

    private Status status;

    private  int discount;

    private String nonce;

    private String getInNotes;

    private PaymentMethod paymentMethod;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getGetInNotes() {
        return getInNotes;
    }

    public void setGetInNotes(String getInNotes) {
        this.getInNotes = getInNotes;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
