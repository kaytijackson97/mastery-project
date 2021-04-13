package models;

import java.math.BigDecimal;

public class Host extends User{
    private String id;
    private String address;
    private String city;
    private int postal_code;
    private BigDecimal standard_rate;
    private BigDecimal weekend_rate;

    public Host() {
    }

    public Host(String id, String last_name, String email, String phone, String address, String city, String state, int postal_code, BigDecimal standard_rate, BigDecimal weekend_rate) {
        super(id, last_name, email, phone, state);
        this.address = address;
        this.city = city;
        this.postal_code = postal_code;
        this.standard_rate = standard_rate;
        this.weekend_rate = weekend_rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(int postal_code) {
        this.postal_code = postal_code;
    }

    public BigDecimal getStandard_rate() {
        return standard_rate;
    }

    public void setStandard_rate(BigDecimal standard_rate) {
        this.standard_rate = standard_rate;
    }

    public BigDecimal getWeekend_rate() {
        return weekend_rate;
    }

    public void setWeekend_rate(BigDecimal weekend_rate) {
        this.weekend_rate = weekend_rate;
    }
}
