import domain.GuestService;
import domain.HostService;
import domain.ReservationService;
import repository.*;
import ui.ConsoleIO;
import ui.Controller;
import ui.View;

public class App {
    public static void main(String[] args) {
        ConsoleIO io = new ConsoleIO();
        View view = new View(io);

        ReservationRepository reservationRepository = new ReservationFileRepository("./data/reservations");
        HostRepository hostRepository = new HostFileRepository("./data/hosts.csv");
        GuestRepository guestRepository = new GuestFileRepository("./data/guests.csv");

        ReservationService reservationService = new ReservationService(reservationRepository, guestRepository, hostRepository);
        HostService hostService = new HostService(hostRepository);
        GuestService guestService = new GuestService(guestRepository);
        Controller controller = new Controller(view, reservationService, hostService, guestService);

        controller.run();
    }
}

