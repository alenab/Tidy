package ca.tidygroup.model;

import javax.persistence.*;

@Entity
@Table(name = "option")
public class CleaningOption {

    public static final String ID_COL_NAME = "id";

    @Id
    @Column(name = ID_COL_NAME, columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CleaningOption{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
