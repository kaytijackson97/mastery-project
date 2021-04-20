package learn.repository;

import learn.models.Guest;
import learn.models.Host;
import learn.models.Reservation;
import learn.repository.convertToJSON.ReservationToJSONRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    private static final String SEED_FILE = "./data/reservations-seed-file.csv";
    private static final String TEST_FILE = "./data/reservations-test-folder/2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    public static final String TEST_DIRECTORY_FOLDER = "./data/reservations-test-folder";
    private final LocalDate startDate = getRandomDate(LocalDate.now());
    private final LocalDate endDate = startDate.plusDays(3);


    private final String testHostId = "2e72f86c-b8fe-4265-b4f1-304dea8762db";

    ReservationToJSONRepository reservationToJSONRepository = new ReservationToJSONRepositoryDouble();
    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIRECTORY_FOLDER, reservationToJSONRepository);

    @BeforeEach
    void setUp() throws IOException {
        Path seedPath = Paths.get(SEED_FILE);
        Path testPath = Paths.get(TEST_FILE);

        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldReturnAllReservationIfValidId() throws DataAccessException {
        List<Reservation> actual = repository.findById(testHostId);
        assertEquals(3, actual.size());
    }

    @Test
    void shouldNotReturnAnyReservationsIfInvalid() throws DataAccessException {
        List<Reservation> actual = repository.findById("Test");
        assertEquals(0, actual.size());
    }

    @Test
    void shouldNotReturnAnyReservationIfIdIsNullOrBlank() throws DataAccessException {
        List<Reservation> actual = repository.findById(null);
        assertEquals(0, actual.size());

        actual = repository.findById(" ");
        assertEquals(0, actual.size());

        actual = repository.findById("");
        assertEquals(0, actual.size());
    }

    @Test
    void shouldAddValidReservation() throws DataAccessException {
        List<Reservation> all = repository.findById(testHostId);
        Reservation reservation = makeReservation(testHostId, GuestRepositoryDouble.GUEST.getId());

        reservation = repository.add(reservation);
        List<Reservation> actual = repository.findById(testHostId);

        assertNotNull(reservation);
        assertEquals(all.size() + 1, actual.size());
    }

    @Test
    void shouldNotMakeReservationIfNull() throws DataAccessException {
        List<Reservation> all = repository.findById(testHostId);
        Reservation reservation = repository.add(null);
        List<Reservation> actual = repository.findById(testHostId);

        assertNull(reservation);
        assertEquals(all.size(), actual.size());
    }

    @Test
    void shouldReturnMakeNewFileIfNoPreviousReservations() throws DataAccessException {
        Reservation reservation = makeReservation("Test Id", GuestRepositoryDouble.GUEST.getId());
        reservation = repository.add(reservation);
        assertNotNull(reservation);
    }

    @Test
    void shouldNotMakeReservationIfNullFieldsInReservation() throws DataAccessException {
        Reservation reservation = new Reservation();
        assertNull(repository.add(reservation));
    }

    @Test
    void shouldUpdateIfValid() throws DataAccessException {
        List<Reservation> reservations = repository.findById(testHostId);
        Reservation reservation = makeReservation(testHostId, GuestRepositoryDouble.GUEST.getId());
        reservation.setReservationId(reservations.get(0).getReservationId());

        assertTrue(repository.update(reservation));

    }

    @Test
    void shouldNotUpdateIfNoExistingReservation() throws DataAccessException {
        Reservation reservation = makeReservation(testHostId, GuestRepositoryDouble.GUEST.getId());
        reservation.setReservationId(-1);

        assertFalse(repository.update(reservation));
    }

    @Test
    void shouldNotUpdateNullReservation() throws DataAccessException {
        assertFalse(repository.update(null));
    }

    @Test
    void shouldDeleteIfValid() throws DataAccessException {
        Reservation reservation = repository.findById(testHostId).get(0);
        assertTrue(repository.deleteById(HostRepositoryDouble.HOST_ID, reservation.getReservationId()));
    }

    @Test
    void shouldNotDeleteIfInvalid() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(-1);

        assertFalse(repository.deleteById(HostRepositoryDouble.HOST_ID, reservation.getReservationId()));
    }

    //support method
    private Reservation makeReservation(String hostId, String guestId) {
        Reservation reservation = new Reservation();
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);

        Host host = new Host();
        host.setId(hostId);
        reservation.setHost(host);

        Guest guest = new Guest();
        guest.setId(guestId);
        reservation.setGuest(guest);

        reservation.setTotal(new BigDecimal("400.00"));
        return reservation;
    }

    private LocalDate getRandomDate(LocalDate startDate) {
        Random random = new Random();
        int year = random.nextInt(5) + 2;
        int month = random.nextInt(11) + 1;
        int day = random.nextInt(27) + 1;
        return LocalDate.of(startDate.getYear() + year, month, day);
    }

}