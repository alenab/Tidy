package ca.tidygroup.dto;

import ca.tidygroup.model.ApartmentUnit;

import java.util.ArrayList;

public class ApartmentUnitListDTO {

    private ArrayList<ApartmentUnit> units;

    public ArrayList<ApartmentUnit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<ApartmentUnit> units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "ApartmentUnitsDTO{" +
                "units=" + units +
                '}';
    }
}
