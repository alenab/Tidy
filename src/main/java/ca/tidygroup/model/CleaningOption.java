package ca.tidygroup.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

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

    @Column(name = "img_name")
    private String imgName;

    @JsonIgnore
    @ManyToMany
    @JoinTable (name = "option_plan",
            joinColumns = @JoinColumn(name="option_id"),
            inverseJoinColumns = @JoinColumn(name = "plan_id"))
    private List<CleaningPlan> planList;


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

    public List<CleaningPlan> getPlanList() {
        return planList;
    }

    public void setPlanList(List<CleaningPlan> planList) {
        this.planList = planList;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    @Override
    public String toString() {
        return "CleaningOption{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imgName='" + imgName + '\'' +
                '}';
    }
}
