package ca.tidygroup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApartmentUnitDTO {

    @JsonProperty
    private long planId;

    @JsonProperty
    private int rooms;

    @JsonProperty
    private int bathrooms;

    @JsonProperty
    private double price;

    public ApartmentUnitDTO(long planId, int rooms, int bathrooms, double price) {
        this.planId = planId;
        this.rooms = rooms;
        this.bathrooms = bathrooms;
        this.price = price;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ApartmentUnitDTO{" +
                "planId=" + planId +
                ", rooms=" + rooms +
                ", bathrooms=" + bathrooms +
                ", price=" + price +
                '}';
    }
}
