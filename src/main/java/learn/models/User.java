package learn.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String lastName;
    private String email;
    private String phone;
    private String state;
    private boolean isDeleted;

    public User() {
    }

    public User(String id,String lastName, String email, String phone, String state, boolean isDeleted) {
        this.id = id;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.state = state;
        this.isDeleted = isDeleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    //parent class methods
    public String getFullName() {
        return lastName;
    }

    public String getFullAddress() {
        return state;
    }

    public List<BigDecimal> getRates() {
        return new ArrayList<>();
    }
}
