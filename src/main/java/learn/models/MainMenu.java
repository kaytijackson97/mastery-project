package learn.models;

public enum MainMenu {
    EXIT("Exit"),
    VIEW_RESERVATIONS("View Reservations By Host"),
    MAKE_RESERVATION("Make a New Reservation"),
    MAKE_HOST("Make a new Host"),
    MAKE_GUEST("Make a new Guest"),
    EDIT_RESERVATION("Edit an Existing Reservation"),
    EDIT_HOST("Edit an Existing Host"),
    EDIT_GUEST("Edit an Existing Guest"),
    DELETE_RESERVATION("Delete an Existing Reservation"),
    DELETE_HOST("Delete an Existing Host"),
    DELETE_GUEST("Delete an Existing Guest");

    private final String title;

    MainMenu(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
