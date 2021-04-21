package learn.ui;

import learn.domain.*;
import learn.models.*;
import learn.repository.DataAccessException;

import java.time.LocalDate;
import java.util.List;

public class Controller {

    private final View view;
    private final ReservationService reservationService;
    private final HostService hostService;
    private final GuestService guestService;

    public Controller(View view, ReservationService reservationService, GuestService guestService, HostService hostService) {
        this.view = view;
        this.reservationService = reservationService;
        this.guestService = guestService;
        this.hostService = hostService;
    }

    public void run() {
        view.displayHeader("Welcome");
        try {
            runAppLoop();
        } catch (DataAccessException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye!");
    }

    public void runAppLoop() throws DataAccessException {
        MainMenu mainMenu;
        do {
            mainMenu = view.selectMainMenuOption();

            switch (mainMenu) {
                case VIEW_RESERVATIONS:
                    view.displayHeader(MainMenu.VIEW_RESERVATIONS.getTitle());
                    viewReservations();
                    break;

                case MAKE_RESERVATION:
                    makeReservation();
                    break;

                case MAKE_HOST:
                    Host host = makeHost();
                    makeUser(hostService, host);
                    break;

                case MAKE_GUEST:
                    User guest = makeGuest();
                    makeUser(guestService, guest);
                    break;

                case EDIT_RESERVATION:
                    editReservation();
                    break;

                case EDIT_HOST:
                    view.displayHeader(MainMenu.EDIT_HOST.getTitle());
                    editHost(hostService);
                    break;

                case EDIT_GUEST:
                    view.displayHeader(MainMenu.EDIT_GUEST.getTitle());
                    editGuest(guestService);
                    break;

                case DELETE_RESERVATION:
                    deleteReservation();
                    break;

                case DELETE_HOST:
                    view.displayHeader(MainMenu.DELETE_HOST.getTitle());
                    deleteUser(hostService, "Host");
                    break;

                case DELETE_GUEST:
                    view.displayHeader(MainMenu.DELETE_GUEST.getTitle());
                    deleteUser(guestService, "Guest");
                    break;
            }
        } while (mainMenu != MainMenu.EXIT);
    }

    //view
    private User viewReservations() throws DataAccessException {
        User host = getUser(hostService, "Host");
        if (host == null) {
            return null;
        }

        List<Reservation> reservations = reservationService.findById(host.getId());
        view.displayReservations(host, reservations);
        return host;
    }

    //create
    private void makeReservation() throws DataAccessException {
        view.displayHeader(MainMenu.MAKE_RESERVATION.getTitle());

        User guest = getUser(guestService, "Guest");
        if (guest == null) {
            return;
        }

        User host = viewReservations();

        LocalDate startDate = view.chooseStartDate();
        LocalDate endDate = view.chooseEndDate(startDate);

        Reservation reservation = new Reservation(host, guest, startDate, endDate);
        Result<Reservation> result = reservationService.isReservationAvailable(reservation);

        if (!result.isSuccess()) {
            view.displayStatus(false, result.getMessages());
            return;
        }

        boolean isGoingToBook = view.displaySummary(result.getPayload());
        if (!isGoingToBook) {
            view.displayStatus(false, "Reservation was not created.");
            return;
        }

        result = reservationService.addReservation(reservation);
        if (result.isSuccess()) {
            view.displayStatus(true, "Reservation was created.");
        } else {
            view.displayStatus(false, result.getMessages());
        }
    }

    private Host makeHost() {
        view.displayHeader(MainMenu.MAKE_HOST.getTitle());
        Host host = new Host();

        host.setLastName(view.chooseString("Last Name: "));
        host.setEmail(view.chooseEmail());
        host.setPhone(view.choosePhone());

        host.setAddress(view.chooseString("Address: "));
        host.setCity(view.chooseString("City: "));
        host.setState(view.chooseState());
        host.setPostalCode(view.choosePostalCode());

        host.setStandardRate(view.chooseRate("Standard Rate: "));
        host.setWeekendRate(view.chooseRate("Weekend Rate: "));
        return host;
    }

    private Guest makeGuest() {
        view.displayHeader(MainMenu.MAKE_GUEST.getTitle());
        Guest guest = new Guest();

        guest.setFirstName(view.chooseString("First Name: "));
        guest.setLastName(view.chooseString("Last Name: "));

        guest.setEmail(view.chooseEmail());
        guest.setPhone(view.choosePhone());

        guest.setState(view.chooseState());

        return guest;
    }

    private void makeUser(UserService userService, User user) throws DataAccessException {
        Result<User> result = userService.addUser(user);
        if (result.isSuccess()) {
            view.displayStatus(true, "Host created.");
        } else {
            view.displayStatus(false, result.getMessages());
        }
    }

    //update
    private void editReservation() throws DataAccessException {
        view.displayHeader(MainMenu.EDIT_RESERVATION.getTitle());

        User host = getUser(hostService, "Host");
        if (host == null) {
            return;
        }

        User guest = getUser(guestService, "Guest");
        if (guest == null) {
            return;
        }

        List<Reservation> reservations = reservationService.findById(host.getId(), guest.getId());
        if (reservations == null || reservations.size() == 0) {
            view.displayStatus(false, "No existing reservations.");
            return;
        }

        view.displayReservations(host, reservations);

        int reservationId = view.chooseReservation(reservations);
        Reservation reservation = reservationService.findByReservationId(host, reservationId);

        reservation = view.editReservation(reservation);
        Result<Reservation> result = reservationService.isReservationAvailable(reservation);

        if (!result.isSuccess()) {
            view.displayStatus(false, result.getMessages());
            return;
        }

        boolean isGoingToBook = view.displaySummary(result.getPayload());
        if (!isGoingToBook) {
            view.displayStatus(false, "Reservation was not updated.");
            return;
        }

        result = reservationService.updateReservation(reservation);
        if (result.isSuccess()) {
            view.displayStatus(true, "Reservation was updated.");
        } else {
            view.displayStatus(false, result.getMessages());
        }
    }

    private void editHost(UserService userService) throws DataAccessException {
        User user = getUser(userService, "Host");
        if (user == null) {
            return;
        }

        user = view.editHost(user);
        Result<User> result = userService.editUser(user);

        if (result.isSuccess()) {
            view.displayStatus(true, "Host updated.");
        } else {
            view.displayStatus(false, result.getMessages());
        }
    }

    private void editGuest(UserService userService) throws DataAccessException {
        User user = getUser(userService, "Guest");
        if (user == null) {
            return;
        }

        user = view.editGuest(user);
        Result<User> result = userService.editUser(user);

        if (result.isSuccess()) {
            view.displayStatus(true, "Guest updated.");
        } else {
            view.displayStatus(false, result.getMessages());
        }
    }

    private void deleteReservation() throws DataAccessException {
        view.displayHeader(MainMenu.DELETE_RESERVATION.getTitle());

        User host = getUser(hostService, "Host");
        if (host == null) {
            return;
        }

        User guest = getUser(guestService, "Guest");
        if (guest == null) {
            return;
        }

        List<Reservation> reservations = reservationService.findById(host.getId(), guest.getId());
        view.displayReservations(host, reservations);

        int reservationId = view.chooseReservation(reservations);
        boolean isGoingToDelete = view.chooseToDelete(reservationId);

        if (!isGoingToDelete) {
            return;
        }

        Result<Reservation> result = reservationService.deleteReservation(host.getId(), reservationId);
        if (result.isSuccess()) {
            view.displayStatus(true, "Reservation was deleted.");
        } else {
            view.displayStatus(false, result.getMessages());
        }
    }

    private void deleteUser(UserService userService, String userType) throws DataAccessException {
        User user = getUser(userService, userType);
        if (user == null) {
            return;
        }

        boolean isGoingToDelete = view.chooseToDelete(user);
        if (!isGoingToDelete) {
            return;
        }

        Result<User> result = userService.deleteUser(user);
        if (result.isSuccess()) {
            view.displayStatus(true, userType + " was deleted.");
        } else {
            view.displayStatus(false, result.getMessages());
        }
    }

    //support methods
    private User getUser(UserService userService, String userType) throws DataAccessException {
        String email = view.chooseUser(userType);
        Result<User> result = userService.findByEmail(email);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getMessages());
            return null;
        }
        return result.getPayload();
    }
}
