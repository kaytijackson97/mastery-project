package learn.models;

import java.math.BigDecimal;
import java.util.List;

public class Host extends User {
    private String id;
    private String address;
    private String city;
    private int postalCode;
    private BigDecimal standardRate;
    private BigDecimal weekendRate;

    public Host() {
    }

    public Host(String id, String last_name, String email, String phone, String address, String city, String state, int postalCode, BigDecimal standardRate, BigDecimal weekendRate, boolean isDeleted) {
        super(id, last_name, email, phone, state, isDeleted);
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.standardRate = standardRate;
        this.weekendRate = weekendRate;
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

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public BigDecimal getStandardRate() {
        return standardRate;
    }

    public void setStandardRate(BigDecimal standardRate) {
        this.standardRate = standardRate;
    }

    public BigDecimal getWeekendRate() {
        return weekendRate;
    }

    public void setWeekendRate(BigDecimal weekendRate) {
        this.weekendRate = weekendRate;
    }

    @Override
    public String getFullAddress() {
        return String.format("%s,%s,%s,%s", address, city, super.getFullAddress(), postalCode);
    }

    @Override
    public List<BigDecimal> getRates() {
        List<BigDecimal> rates = super.getRates();
        rates.add(standardRate);
        rates.add(weekendRate);
        return rates;
    }
}
