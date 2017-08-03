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

    @JsonProperty
    private int planned_time;


    public ApartmentUnitDTO(long planId, int rooms, int bathrooms, double price, int planned_time) {
        this.planId = planId;
        this.rooms = rooms;
        this.bathrooms = bathrooms;
        this.price = price;
        this.planned_time = planned_time;
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

    public int getPlanned_time() {
        return planned_time;
    }

    public void setPlanned_time(int planned_time) {
        this.planned_time = planned_time;
    }

    @Override
    public String toString() {
        return "ApartmentUnitDTO{" +
                "planId=" + planId +
                ", rooms=" + rooms +
                ", bathrooms=" + bathrooms +
                ", price=" + price +
                ", planned_time=" + planned_time +
                '}';
    }
}
