package models;

public enum MainMenu {
    EXIT("Exit"),
    VIEW_RESERVATIONS("View Reservations By Host"),
    MAKE_RESERVATION("Make a New Reservation"),
    EDIT_RESERVATION("Edit an Existing Reservation"),
    DELETE_RESERVATION("Delete an Existing Reservation");

    private final String title;

    MainMenu(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
