package domain;

import models.Reservation;
import org.junit.jupiter.api.Test;
import repository.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service = new ReservationService(
            new ReservationRepositoryDouble(),
            new GuestRepositoryDouble(),
            new HostRepositoryDouble());

    @Test
    void shouldFindAllReservationsIfValidHostId() throws DataAccessException {
        List<Reservation> all = service.findById(HostRepositoryDouble.HOST_ID);
        assertEquals(2, all.size());
    }

    @Test
    void shouldNotReturnAnyReservationsIfInvalidId() throws DataAccessException {
        List<Reservation> actual = service.findById("Test");
        assertEquals(0, actual.size());
    }

    @Test
    void shouldMakeValidReservation() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setStartDate(LocalDate.of(2021, 10,16));
        reservation.setEndDate(LocalDate.of(2021, 10,18));
        reservation.setTotal(service.getPrice(reservation));
        Result<Reservation> result = service.addReservation(reservation);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotMakeReservationIfNull() throws DataAccessException {
        Result<Reservation> result = service.addReservation(null);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotMakeReservationIfFieldsAreNull() throws DataAccessException {
        Result<Reservation> result = new Result<>();
        Reservation reservation = new Reservation();

        reservation.setHost(null);
        reservation.setGuest(null);
        reservation.setStartDate(null);
        reservation.setEndDate(null);

        result = service.addReservation(reservation);

        assertFalse(result.isSuccess());
        assertEquals(3, result.getMessages().size());
    }

    @Test
    void shouldNotMakeReservationIfNullFieldsInReservation() throws DataAccessException {
        Reservation reservation = new Reservation();
        Result<Reservation> result = service.addReservation(reservation);
        assertFalse(result.isSuccess());
        assertEquals(3, result.getMessages().size());
    }

    @Test
    void shouldNotMakeReservationIfStartDateIsAfterEndDate() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setEndDate(LocalDate.of(2021, 10,16));
        reservation.setStartDate(LocalDate.of(2021, 10,18));
        reservation.setTotal(service.getPrice(reservation));
        Result<Reservation> result = service.addReservation(reservation);

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotMakeReservationIfInPast() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.of(2021, 10,18));
        reservation.setTotal(service.getPrice(reservation));
        Result<Reservation> result = service.addReservation(reservation);

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotMakeBookingIfOnExistingBooking() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setStartDate(LocalDate.of(2021, 10,12));
        reservation.setEndDate(LocalDate.of(2021, 10,14));
        reservation.setTotal(service.getPrice(reservation));
        Result<Reservation> result = service.addReservation(reservation);

        assertFalse(result.isSuccess());
    }

}