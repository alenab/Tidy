package ca.tidygroup.dto;

import java.time.LocalTime;
import java.util.List;

public class TimeLimitationDTO {

    private String date;

    private List<LocalTime> listTimeLimits;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<LocalTime> getListTimeLimits() {
        return listTimeLimits;
    }

    public void setListTimeLimits(List<LocalTime> listTimeLimits) {
        this.listTimeLimits = listTimeLimits;
    }
}
