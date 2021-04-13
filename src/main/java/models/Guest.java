package models;

public class Guest extends User{

    private String first_name;

    public Guest() {
    }

    public Guest(String id, String first_name, String last_name, String phone, String email, String state) {
        super(id, last_name, phone, email, state);
        this.first_name = first_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
}
