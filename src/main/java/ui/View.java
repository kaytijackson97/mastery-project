package ui;

import models.Host;
import models.MainMenu;
import models.Reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    public Reservation editReservation(Reservation reservation) {
        displayHeader("Editing Reservation " + reservation.getReservationId());

        LocalDate startDate = io.readDate("Start (" + reservation.getStartDate() + "): ", reservation.getStartDate());
        LocalDate endDate = io.readDate("End (" + reservation.getStartDate() + "): ", reservation.getEndDate(), startDate);

        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);

        return reservation;
    }

    public String chooseHost() {
        return io.readRequiredString("Host Email: ");
    }

    public String chooseGuest() {
        return io.readRequiredString("Guest Email: ");
    }

    public LocalDate chooseStartDate() {
        return io.readRequiredDate("Start Date: ");
    }

    public LocalDate chooseEndDate(LocalDate startDate) {
        return io.readRequiredDate("End Date: ", startDate);
    }

    public Reservation chooseReservation(List<Reservation> reservations) {
        int choice = -1;
        do {
            choice = io.readInt("Reservation ID: ");
            if (reservations.get(choice) != null) {
                return reservations.get(choice);
            }
        } while (true);
    }

    public boolean chooseToDelete(Reservation reservation) {
        return io.readBoolean("Are you sure you would like to delete reservation " + reservation.getReservationId() + "? [y/n]: ");
    }

    public void displayHeader(String message) {
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("Critical Error Occurred");
        io.println(ex.getMessage());
    }

    public void displayStatus(boolean status, List<String> messages) {
        if (status) {
            displayHeader("Success");
        } else {
            displayHeader("Error");
            for (String m : messages) {
                io.println(m);
            }
        }
    }

    public void displayStatus(boolean status, String message) {
        displayStatus(status, List.of(message));
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

    public boolean displaySummary(Reservation reservation) {
        displayHeader("Summary");
        io.printf("Start Date: %s", reservation.getStartDate());
        io.printf("End Date: %s", reservation.getEndDate());
        io.printf("Total: %s", reservation.getTotal());
        return io.readBoolean("Is this okay? [y/n]: ");
    }

}
