package models;

public class Guest extends User{
    private final int guest_id;
    private final String first_name;

    public Guest(int guest_id, String first_name, String last_name, String phone, String email, String state) {
        super(last_name, phone, email, state);
        this.guest_id = guest_id;
        this.first_name = first_name;
    }
}
