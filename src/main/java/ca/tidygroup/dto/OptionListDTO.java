package ca.tidygroup.dto;

import ca.tidygroup.model.CleaningOption;

import java.util.ArrayList;

public class OptionListDTO {

    private ArrayList<CleaningOption> options;

    public ArrayList<CleaningOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<CleaningOption> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "OptionListDTO{" +
                "options=" + options +
                '}';
    }
}
