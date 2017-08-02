package ca.tidygroup.event;

public class ReducedBookingSlots {

    private final int newNumberOfSlots;

    public ReducedBookingSlots(int newNumberOfSlots) {
        this.newNumberOfSlots = newNumberOfSlots;
    }

    public int getNewNumberOfSlots() {
        return newNumberOfSlots;
    }
}
