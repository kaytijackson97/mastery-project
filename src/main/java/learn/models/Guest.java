package learn.models;

public class Guest extends User {

    private String first_name;

    public Guest() {
    }

    public Guest(String id, String first_name, String last_name, String email, String phone, String state) {
        super(id, last_name, email, phone, state);
        this.first_name = first_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    //parent methods
    @Override
    public String getFullName() {
        return String.format("%s %s", first_name, super.getFullName());
    }
}
