package ca.tidygroup.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cleaning_plan")
public class CleaningPlan {

    @Id
    @Column(name = "id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "cleaningPlan")
    private List<ApartmentUnit> units;

    public List<ApartmentUnit> getUnits() {
        return units;
    }

    public void setUnits(List<ApartmentUnit> units) {
        this.units = units;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
