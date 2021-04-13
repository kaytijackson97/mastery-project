package ui;

import domain.HostService;
import domain.ReservationService;
import models.Host;
import models.MainMenu;
import models.Reservation;
import repository.DataAccessException;

import java.util.List;

public class Controller {

    private final View view;
    private final ReservationService reservationService;
    private final HostService hostService;

    public Controller(View view, ReservationService reservationService, HostService hostService) {
        this.view = view;
        this.reservationService = reservationService;
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
                    view.displayHeader(MainMenu.MAKE_RESERVATION.getTitle());
                    break;

                case EDIT_RESERVATION:
                    view.displayHeader(MainMenu.EDIT_RESERVATION.getTitle());
                    break;

                case DELETE_RESERVATION:
                    view.displayHeader(MainMenu.DELETE_RESERVATION.getTitle());
                    break;
            }
        } while (mainMenu != MainMenu.EXIT);
    }

    private void viewReservations() throws DataAccessException {
        view.displayHeader(MainMenu.VIEW_RESERVATIONS.getTitle());
        String hostEmail = view.chooseHost();
        Host host = hostService.findByEmail(hostEmail);
        List<Reservation> reservations = reservationService.findById(host.getId());
        view.displayReservations(host, reservations);

    }
}
