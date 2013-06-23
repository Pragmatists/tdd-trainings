package tdd.customerSearch;

import static java.util.Arrays.asList;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CustomerCriteria {

    // Contact:
    private String firstname;
    private String lastname;
    private Boolean male;
    
    // Address:
    private String street;
    private String zipCode;
    private String city;
    private String country;
    
    // Customer:
    private String number;
    private String name;
    private Boolean active;
    private Date registrationFrom;
    private Date registrationTo;

    // Product:
    private String serialNumber;
    private Set<ProductType> type;
    private Integer productCount;
    private Integer totalPrice;
    
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public Boolean getMale(){
        return male;
    }
    public String getStreet() {
        return street;
    }
    public String getZipCode() {
        return zipCode;
    }
    public String getCity() {
        return city;
    }
    public String getCountry() {
        return country;
    }
    public String getNumber() {
        return number;
    }
    public String getName() {
        return name;
    }
    public Boolean getActive() {
        return active;
    }
    public Date getRegistrationFrom() {
        return registrationFrom;
    }
    public Date getRegistrationTo() {
        return registrationTo;
    }
    public String getSerialNumber() {
        return serialNumber;
    }
    public Set<ProductType> getType() {
        return type;
    }
    public Integer getProductCount() {
        return productCount;
    }
    public Integer getTotalPriceGreaterThan() {
        return totalPrice;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setMale(Boolean male) {
        this.male = male;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public void setRegistrationFrom(Date registrationFrom) {
        this.registrationFrom = registrationFrom;
    }
    public void setRegistrationTo(Date registrationTo) {
        this.registrationTo = registrationTo;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public void setType(Set<ProductType> type) {
        this.type = type;
    }
    public void setType(ProductType... types) {
        setType(new HashSet<ProductType>(asList(types)));
    }
    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }
    public void setTotalPriceGreaterThan(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
    
}
