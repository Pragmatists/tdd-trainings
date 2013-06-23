package tdd.customerSearch;

import java.util.Date;

public class Customer {

    private final String customerNumber;
    private String name;
    private Boolean active;
    private Date registrationDate;

    public Customer(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getName() {
        return name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void getRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

}
