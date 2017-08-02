package ca.tidygroup.dto;

import javax.validation.constraints.Min;

public class WorkingHoursDTO {

    private String startTime;

    private String endTime;

    @Min(1)
    private int step;

    @Min(1)
    private int slotAmount;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getSlotAmount() {
        return slotAmount;
    }

    public void setSlotAmount(int slotAmount) {
        this.slotAmount = slotAmount;
    }
}
