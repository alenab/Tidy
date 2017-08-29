package ca.tidygroup.model;

public enum PaymentMethod {

    CREDIT_CARD("credit card"),
    CASH("cash"),
    CHECK("check");

    private final String name;

    PaymentMethod(String name) {
    this.name = name;
    }

    public String getName() {
        return name;
    }
}
