package ca.tidygroup.dto;

public class CardDTO {

    private String brand;

    private String last4;

    private String expDate;

//    private boolean isDefault;

    public String getCardbrand() {
        return brand;
    }

    public void setCardbrand(String brand) {
        this.brand = brand;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

}
