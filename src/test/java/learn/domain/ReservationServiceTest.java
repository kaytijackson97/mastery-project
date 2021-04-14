package learn.domain;

import learn.repository.DataAccessException;
import learn.models.Reservation;
import learn.repository.GuestRepositoryDouble;
import learn.repository.HostRepositoryDouble;
import learn.repository.ReservationRepositoryDouble;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
        assertNull(actual);
    }

    @Test
    void shouldFindAllReservationsWithHostIdAndGuestId() throws DataAccessException {
        List<Reservation> all = service.findById(HostRepositoryDouble.HOST_ID, GuestRepositoryDouble.GUEST.getId());
        assertEquals(2, all.size());
    }

    @Test
    void shouldNotReturnPastReservations() throws DataAccessException {
        List<Reservation> actual = service.findById(HostRepositoryDouble.HOST_ID);
        assertEquals(2, actual.size());

        Reservation reservation = service.findByReservationId(HostRepositoryDouble.HOST, 3);
        assertNull(reservation);
    }

    @Test
    void shouldReturnNullIfInvalidHostOrGuestId() throws DataAccessException {
        List<Reservation> actual = service.findById("Test Host", "Test Guest");
        assertNull(actual);
    }

    @Test
    void shouldAddValidReservation() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setStartDate(LocalDate.of(2021, 10,16));
        reservation.setEndDate(LocalDate.of(2021, 10,18));
        reservation.setTotal(service.getPrice(reservation));

        Result<Reservation> result = service.isReservationAvailable(reservation);
        assertTrue(result.isSuccess());

        result = service.addReservation(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldAddReservationIfStartDateIsOnEndDateOfExisting() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setStartDate(LocalDate.of(2021, 10,14));
        reservation.setEndDate(LocalDate.of(2021, 10,15));
        reservation.setTotal(service.getPrice(reservation));

        Result<Reservation> result = service.isReservationAvailable(reservation);
        assertTrue(result.isSuccess());

        result = service.addReservation(reservation);
        assertTrue(result.isSuccess());

        reservation.setStartDate(LocalDate.of(2021, 10,11));
        reservation.setEndDate(LocalDate.of(2021, 10,12));
        reservation.setTotal(service.getPrice(reservation));

        result = service.isReservationAvailable(reservation);
        assertTrue(result.isSuccess());

        result = service.addReservation(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAddReservationIfSetId() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(10);
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setStartDate(LocalDate.of(2021, 10,16));
        reservation.setEndDate(LocalDate.of(2021, 10,18));
        reservation.setTotal(service.getPrice(reservation));

        Result<Reservation> result = service.isReservationAvailable(reservation);
        assertTrue(result.isSuccess());

        result = service.addReservation(reservation);
        assertFalse(result.isSuccess());

    }

    @Test
    void shouldNotMakeReservationIfNull() throws DataAccessException {
        Result<Reservation> result = service.isReservationAvailable(null);
        assertFalse(result.isSuccess());

        result = service.addReservation(null);
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

        result = service.isReservationAvailable(reservation);
        assertFalse(result.isSuccess());
        assertEquals(3, result.getMessages().size());

        result = service.addReservation(reservation);
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

        Result<Reservation> result = service.isReservationAvailable(reservation);
        assertFalse(result.isSuccess());

        result = service.addReservation(reservation);
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

        Result<Reservation> result = service.isReservationAvailable(reservation);
        assertFalse(result.isSuccess());

        result = service.addReservation(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotMakeReservationIfOnExistingReservation() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setStartDate(LocalDate.of(2021, 10,12));
        reservation.setEndDate(LocalDate.of(2021, 10,14));
        reservation.setTotal(service.getPrice(reservation));

        Result<Reservation> result = service.isReservationAvailable(reservation);
        assertFalse(result.isSuccess());

        result = service.addReservation(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldChargeWeekendRateOnFridayAndSaturday() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setStartDate(LocalDate.of(2021, 10,15));
        reservation.setEndDate(LocalDate.of(2021, 10,16));
        BigDecimal actual = service.getPrice(reservation);

        BigDecimal expected = new BigDecimal("500.00");
        assertEquals(expected, actual);
    }

    @Test
    void shouldChargeStandardRateSundayThroughThursday() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setHost(HostRepositoryDouble.HOST);
        reservation.setGuest(GuestRepositoryDouble.GUEST);
        reservation.setStartDate(LocalDate.of(2021, 10,17));
        reservation.setEndDate(LocalDate.of(2021, 10,21));
        BigDecimal actual = service.getPrice(reservation);

        BigDecimal expected = new BigDecimal("1000.00");
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateReservationIfValid() throws DataAccessException {
        List<Reservation> reservations = service.findById(HostRepositoryDouble.HOST.getId());
        Reservation reservation = reservations.get(0);

        reservation.setStartDate(LocalDate.of(2023, 10, 8));
        reservation.setEndDate(LocalDate.of(2023, 10, 10));
        Result<Reservation> result = service.updateReservation(reservation);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateReservationIfNull() throws DataAccessException {
        Result<Reservation> result = service.updateReservation(null);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateReservationIfFieldsAreNull() throws DataAccessException {
        List<Reservation> reservations = service.findById(HostRepositoryDouble.HOST.getId());
        Reservation reservation = reservations.get(0);

        reservation.setHost(null);
        reservation.setGuest(null);
        reservation.setStartDate(null);
        reservation.setEndDate(null);

        Result<Reservation> result = service.updateReservation(reservation);
        assertFalse(result.isSuccess());
        assertEquals(3, result.getMessages().size());
    }

    @Test
    void shouldNotUpdateIfReservationDateIsInPast() throws DataAccessException {
        List<Reservation> reservations = service.findById(HostRepositoryDouble.HOST.getId());
        Reservation reservation = reservations.get(0);
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.of(2021, 10,18));

        Result<Reservation> result = service.updateReservation(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateIfReservationIfStartDateIsAfterEndDate() throws DataAccessException {
        List<Reservation> reservations = service.findById(HostRepositoryDouble.HOST.getId());
        Reservation reservation = reservations.get(0);
        reservation.setStartDate(LocalDate.of(2021, 10,18));
        reservation.setEndDate(LocalDate.of(2021, 10,16));

        Result<Reservation> result = service.updateReservation(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateIfStartDateAndEndDateAreSame() throws DataAccessException {
        List<Reservation> reservations = service.findById(HostRepositoryDouble.HOST.getId());
        Reservation reservation = reservations.get(0);
        reservation.setStartDate(LocalDate.of(2021, 10,18));
        reservation.setEndDate(LocalDate.of(2021, 10,18));

        Result<Reservation> result = service.updateReservation(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotUpdateIfExistingReservation() throws DataAccessException {
        List<Reservation> reservations = service.findById(HostRepositoryDouble.HOST.getId());
        Reservation reservation = reservations.get(0);
        reservation.setStartDate(reservations.get(1).getStartDate());
        reservation.setEndDate(reservations.get(1).getEndDate());
    }

    @Test
    void shouldAllowNewReservationOnOldDates() throws DataAccessException {
        List<Reservation> reservations = service.findById(HostRepositoryDouble.HOST.getId());
        Reservation reservation = reservations.get(0);
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();

        reservation.setStartDate(startDate.plusMonths(8));
        reservation.setEndDate(endDate.plusMonths(8));
        Result<Reservation> result = service.updateReservation(reservation);

        assertTrue(result.isSuccess());

        Reservation newReservation = new Reservation();
        newReservation.setHost(HostRepositoryDouble.HOST);
        newReservation.setGuest(GuestRepositoryDouble.GUEST);
        newReservation.setStartDate(LocalDate.of(2021, 10,16));
        newReservation.setEndDate(LocalDate.of(2021, 10,18));
        newReservation.setTotal(service.getPrice(reservation));
        result = service.addReservation(newReservation);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldDeleteIfValid() throws DataAccessException {
        List<Reservation> reservations = service.findById(HostRepositoryDouble.HOST.getId());
        Reservation reservation = reservations.get(1);

        Result<Reservation> result = service.deleteReservation(reservation.getHost().getId(), reservation.getReservationId());
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotDeleteIfInvalid() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(10);
        reservation.setHost(HostRepositoryDouble.HOST);

        Result<Reservation> result = service.deleteReservation(reservation.getHost().getId(), reservation.getReservationId());
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotDeleteIfHostIdNull() throws DataAccessException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(0);

        Result<Reservation> result = service.deleteReservation(null, reservation.getReservationId());
        assertFalse(result.isSuccess());
    }

}