package ca.tidygroup.dto;

import ca.tidygroup.model.CleaningOption;
import ca.tidygroup.model.CleaningPlan;
import ca.tidygroup.model.Status;

import java.util.List;

public class BookingDTOCustomer {

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

    private double finalPrice;

    private Status status;

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

}
