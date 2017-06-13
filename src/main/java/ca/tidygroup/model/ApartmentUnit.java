package ca.tidygroup.model;

import javax.persistence.*;

@Entity
@Table(name="apartment_unit")
public class ApartmentUnit {

    public static final String ID_COL_NAME = "id";

    @Id
    @Column(name = ID_COL_NAME, columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name="number_of_bedrooms")
    private int numberOfBedrooms;

    @Column(name="number_of_bathrooms")
    private int numberOfBathrooms;

    @ManyToOne
    @JoinColumn(name = "cleaning_plan_id")
    private CleaningPlan cleaningPlan;

    @Column(name = "price")
    private Double price;

    public CleaningPlan getCleaningPlan() {
        return cleaningPlan;
    }

    public void setCleaningPlan(CleaningPlan cleaningPlan) {
        this.cleaningPlan = cleaningPlan;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(int numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(int numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    @Override
    public String toString() {
        return "ApartmentUnit{" +
                "numberOfBedrooms=" + numberOfBedrooms +
                ", numberOfBathrooms=" + numberOfBathrooms +
                ", cleaningPlan=" + cleaningPlan +
                ", price=" + price +
                '}';
    }
}
