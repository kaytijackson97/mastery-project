package models;

public class User {
    private String id;
    private String last_name;
    private String email;
    private String phone;
    private String state;

    public User() {
    }

    public User(String id,String last_name, String email, String phone, String state) {
        this.id = id;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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
}
