package ui;

import models.Host;
import models.MainMenu;
import models.Reservation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class View {

    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MainMenu selectMainMenuOption() {
        int min = 0;
        int max = 0;
        int input = -1;
        displayHeader("Main Menu");

        ArrayList<MainMenu> mainMenuOptions = new ArrayList<>(Arrays.asList(MainMenu.values()));

        for (int i = 0; i < mainMenuOptions.size(); i++) {
            io.printf("%s. %s%n", i, mainMenuOptions.get(i).getTitle());
            max = Math.max(max, i);
        }

        String message = String.format("Select [%s - %s]: ", min, max);
        input = io.readInt(message, min, max);

        return mainMenuOptions.get(input);
    }

    public String chooseHost() {
        return io.readRequiredString("Host Email: ");
    }

    public void displayHeader(String message) {
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("Critical Error Occurred");
        io.println(ex.getMessage());
    }
    public void displayReservations(Host host, List<Reservation> reservations) {
        String hostLastName = host.getLast_name();
        String location = host.getCity() + ", " + host.getState();
        displayHeader(hostLastName + ": " + location);
        for (Reservation r : reservations) {
            io.printf("ID: %s, %s - %s, Guest: %s, %s, Email: %s%n",
                    r.getReservationId(),
                    r.getStartDate(),
                    r.getEndDate(),
                    r.getGuest().getLast_name(),
                    r.getGuest().getFirst_name(),
                    r.getGuest().getEmail());
        }
    }

}
