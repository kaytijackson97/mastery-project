package learn.ui;

import learn.domain.GuestService;
import learn.domain.HostService;
import learn.domain.ReservationService;
import learn.domain.Result;
import learn.repository.DataAccessException;
import learn.models.Guest;
import learn.models.Host;
import learn.models.MainMenu;
import learn.models.Reservation;

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
                    viewReservations();
                    break;

                case MAKE_RESERVATION:
                    makeReservation();
                    break;

                case EDIT_RESERVATION:
                    editReservation();
                    break;

                case DELETE_RESERVATION:
                    deleteReservation();
                    break;
            }
        } while (mainMenu != MainMenu.EXIT);
    }

    private void viewReservations() throws DataAccessException {
        view.displayHeader(MainMenu.VIEW_RESERVATIONS.getTitle());

        Host host = getHost();
        if (host == null) {
            return;
        }

        List<Reservation> reservations = reservationService.findById(host.getId());
        view.displayReservations(host, reservations);

    }

    private void makeReservation() throws DataAccessException {
        view.displayHeader(MainMenu.MAKE_RESERVATION.getTitle());

        Host host = getHost();
        if (host == null) {
            return;
        }

        Guest guest = getGuest();
        if (guest == null) {
            return;
        }

        List<Reservation> reservations = reservationService.findById(host.getId());
        view.displayReservations(host, reservations);

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

    private void editReservation() throws DataAccessException {
        view.displayHeader(MainMenu.EDIT_RESERVATION.getTitle());

        Host host = getHost();
        if (host == null) {
            return;
        }

        Guest guest = getGuest();
        if (guest == null) {
            return;
        }

        List<Reservation> reservations = reservationService.findById(host.getId(), guest.getId());
        if (reservations == null || reservations.size() == 0) {
            view.displayStatus(false, "No existing reservations.");
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

    private void deleteReservation() throws DataAccessException {
        view.displayHeader(MainMenu.DELETE_RESERVATION.getTitle());

        Host host = getHost();
        Guest guest = getGuest();
        if (host == null || guest == null) {
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

    //support methods
    private Host getHost() throws DataAccessException {
        String hostEmail = view.chooseHost();
        Result<Host> result = hostService.findByEmail(hostEmail);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getMessages());
            return null;
        }
        return result.getPayload();
    }

    private Guest getGuest() throws DataAccessException {
        String guestEmail = view.chooseGuest();
        Result<Guest> result = guestService.findByEmail(guestEmail);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getMessages());
            return null;
        }
        return result.getPayload();
    }
}
