package learn.ui;

import learn.models.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        input = io.readRequiredInt(message, min, max);

        return mainMenuOptions.get(input);
    }

    public User addGuest() {
        Guest guest = new Guest();
        guest.setFirstName(io.readRequiredString("First Name: "));
        guest.setLastName(io.readRequiredString("Last Name: "));
        guest.setEmail(io.readRequiredEmail("Email: "));
        guest.setPhone(io.readRequiredPhone("Phone: "));

        guest.setState(cleanField(io.readRequiredState("State Abbreviation: ")));

        return guest;
    }

    public Reservation editReservation(Reservation reservation) {
        displayHeader("Editing Reservation " + reservation.getReservationId());

        LocalDate startDate = io.readDate("Start (" + reservation.getStartDate() + "): ", reservation.getStartDate());
        LocalDate endDate = io.readDate("End (" + reservation.getEndDate() + "): ", reservation.getEndDate(), startDate);

        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);

        return reservation;
    }

    public User editHost(User user) {
        Host host = userToHost(user);
        displayHeader("Editing Host: " + host.getEmail());

        String lastName = io.readString("Last Name (" + host.getLastName() + "): ");
        if (!lastName.isBlank()) {
            host.setLastName(lastName);
        }

        String phone = io.readPhone("Phone (" + host.getPhone() + "): ", host.getPhone());
        host.setPhone(phone);

        String address = io.readString("Address (" + host.getAddress() + "): ");
        if (!address.isBlank()) {
            host.setAddress(address);
        }

        String city = io.readString("City (" + host.getCity() + "): ");
        if (!city.isBlank()) {
            host.setCity(city);
        }

        String state = io.readState("State (" + host.getState() + "): ", host.getState());
        host.setState(state);

        String postalCode = io.readPostalCode("Postal Code (" + host.getPostalCode() + "): ", host.getPostalCode());
        host.setPostalCode(postalCode);

        BigDecimal standardRate = io.readBigDecimal("Standard Rate (" + host.getStandardRate() + "): ", host.getStandardRate());
        host.setStandardRate(standardRate);

        BigDecimal weekendRate = io.readBigDecimal("Weekend Rate (" + host.getWeekendRate() + "): ", host.getWeekendRate());
        host.setWeekendRate(weekendRate);

        return host;
    }

    public User editGuest(User user) {
        Guest guest = userToGuest(user);
        displayHeader("Editing Host: " + guest.getEmail());

        String lastName = io.readString("Last Name (" + guest.getLastName() + "): ");
        if (!lastName.isBlank()) {
            guest.setLastName(lastName);
        }

        String phone = io.readPhone("Phone (" + guest.getPhone() + "): ", guest.getPhone());
        guest.setPhone(phone);

        String state = io.readState("State (" + guest.getState() + "): ", guest.getState());
        guest.setState(state);

        return guest;
    }

    //choose
    public String chooseString(String prompt) {
        return io.readRequiredString(prompt);
    }

    public String chooseEmail() {
        return io.readRequiredEmail("Email: ");
    }

    public String choosePhone() {
        return io.readRequiredPhone("Phone (ex. (123) 4567890): ");
    }

    public String chooseState() {
        return io.readRequiredState("State Abbreviation: ");
    }

    public String choosePostalCode() {
        return io.readRequiredPostalCode("Postal Code: ");
    }

    public BigDecimal chooseRate(String prompt) {
        return io.readRequiredBigDecimal(prompt);
    }

    public String chooseUser(String userType) {
        return io.readRequiredEmail(userType + " Email: ");
    }

    public int chooseReservation(List<Reservation> reservations) {
        int choice = -1;
        do {
            choice = io.readRequiredInt("Reservation ID: ");
            for (Reservation r : reservations) {
                if (r.getReservationId() == choice) {
                    return choice;
                }
            }
            io.println("Reservation ID does not exist");
        } while (true);
    }

    public LocalDate chooseStartDate() {
        return io.readRequiredDate("Start Date: ");
    }

    public LocalDate chooseEndDate(LocalDate startDate) {
        return io.readRequiredDate("End Date: ", startDate);
    }

    public boolean chooseToDelete(int reservationId) {
        return io.readBoolean("Are you sure you would like to delete reservation " + reservationId + "? [y/n]: ");
    }

    public boolean chooseToDelete(User user) {
        return io.readBoolean("Are you sure you would like to delete " + user.getEmail() + "? [y/n]: ");
    }

    //display
    public void displayHeader(String message) {
        io.println("");
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
        }
        for (String m : messages) {
            io.println(m);
        }
        io.println("");
    }

    public void displayStatus(boolean status, String message) {
        displayStatus(status, List.of(message));
    }

    public void displayReservations(User host, List<Reservation> reservations) {
        String hostLastName = host.getFullName();
        String location = host.getFullAddress();
        displayHeader(hostLastName + ": " + location);
        if (reservations == null || reservations.size() == 0) {
            io.println("No reservations found.");
            return;
        }
        for (Reservation r : reservations) {
            if (r.getGuest().isDeleted()) {
                continue;
            }

            io.printf("ID: %s, %s - %s, Guest: %s, Email: %s%n",
                    r.getReservationId(),
                    r.getStartDate(),
                    r.getEndDate(),
                    r.getGuest().getFullName(),
                    r.getGuest().getEmail());
        }
    }

    public boolean displaySummary(Reservation reservation) {
        displayHeader("Summary");
        io.printf("Start Date: %s%n", reservation.getStartDate());
        io.printf("End Date: %s%n", reservation.getEndDate());
        io.printf("Total: %s%n", reservation.getTotal());
        return io.readBoolean("Is this okay? [y/n]: ");
    }

    //support methods
    private String cleanField(String input) {
        return input.replace(",", "@@@");
    }

    private Host userToHost(User user) {
        Host host = new Host();

        host.setId(user.getId());
        host.setLastName(user.getLastName());
        host.setEmail(user.getEmail());
        host.setPhone(user.getPhone());

        String[] addressFields = user.getFullAddress().split(",", -1);
        host.setAddress(addressFields[0]);
        host.setCity(addressFields[1]);
        host.setState(user.getState());
        host.setPostalCode(addressFields[3]);

        host.setStandardRate(user.getRates().get(0));
        host.setWeekendRate(user.getRates().get(1));
        return host;
    }

    private Guest userToGuest(User user) {
        Guest guest = new Guest();

        guest.setId(user.getId());

        String[] nameFields = user.getFullName().split(",", -1);
        guest.setFirstName(nameFields[0]);
        guest.setLastName(user.getLastName());
        guest.setEmail(user.getEmail());
        guest.setPhone(user.getPhone());

        guest.setState(user.getState());

        return guest;
    }

}
