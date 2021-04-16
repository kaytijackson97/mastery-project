package learn.models;

public class Guest extends User {

    private String firstName;

    public Guest() {
    }

    public Guest(String id, String firstName, String lastName, String email, String phone, String state, boolean isDeleted) {
        super(id, lastName, email, phone, state, isDeleted);
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    //parent methods
    @Override
    public String getFullName() {
        return String.format("%s,%s", firstName, super.getFullName());
    }
}
