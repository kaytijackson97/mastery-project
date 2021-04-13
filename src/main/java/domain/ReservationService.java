package domain;

import models.Guest;
import models.Reservation;
import org.springframework.cglib.core.Local;
import repository.DataAccessException;
import repository.GuestRepository;
import repository.HostRepository;
import repository.ReservationRepository;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final HostRepository hostRepository;

    public ReservationService(ReservationRepository reservationRepository, GuestRepository guestRepository, HostRepository hostRepository) {
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
    }

    public List<Reservation> findById(String id) throws DataAccessException {

        Map<String, Guest> guestMap = guestRepository.findAll().stream()
                .collect(Collectors.toMap(Guest::getId, i -> i));

        List<Reservation> result = reservationRepository.findById(id);

        if (result == null || result.size() == 0) {
            return null;
        }

        for (Reservation r : result) {
            r.setGuest(guestMap.get(r.getGuest().getId()));
        }

        return result;
    }

    public Result<Reservation> isReservationAvailable(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = validateDates(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        result = validateIsNotAlreadyBooked(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        reservation.setTotal(getPrice(reservation));
        result.setPayload(reservation);

        return result;
    }

    public Result<Reservation> makeBooking(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = validateDates(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        result = validateIsNotAlreadyBooked(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        reservation = reservationRepository.add(reservation);

        if (reservation == null) {
            result.addErrorMessage("Could not complete booking");
            return result;
        }

        result.setPayload(reservation);
        return result;
    }

    private Result<Reservation> validateDates(Reservation reservation) {
        Result<Reservation> result = new Result<>();

        if (reservation.getStartDate() == null || reservation.getEndDate() == null) {
            result.addErrorMessage("Dates cannot be null.");
        }

        if (reservation.getStartDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Start date must be in the future");
        }

        if (reservation.getEndDate().isBefore(reservation.getStartDate())
        || reservation.getStartDate().equals(reservation.getEndDate())) {
            result.addErrorMessage("Start date must be before end date.");
        }

        return result;
    }

    private Result<Reservation> validateIsNotAlreadyBooked(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = new Result<>();

        List<Reservation> reservations = reservationRepository.findById(reservation.getHost().getId());
        List<LocalDate> bookedDates = new ArrayList<>();
        for (Reservation r : reservations) {

            LocalDate startDate = r.getStartDate();

            while (!startDate.isAfter(r.getEndDate())) {
                bookedDates.add(startDate);
                startDate = startDate.plusDays(1);
            }
        }

        if (bookedDates.contains(reservation.getStartDate()) || bookedDates.contains(reservation.getEndDate())) {
            result.addErrorMessage("Pre-existing Booking");
        }

        return result;
    }

    private BigDecimal getPrice(Reservation reservation) {
        BigDecimal total = new BigDecimal("0.00");
        LocalDate startDate = reservation.getStartDate();
        ArrayList<LocalDate> reservedDates = new ArrayList<>();

        while (!startDate.isAfter(reservation.getEndDate())) {
            if (startDate.getDayOfWeek() == DayOfWeek.SATURDAY || startDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                total = total.add(reservation.getHost().getWeekend_rate());
            } else {
                total = total.add(reservation.getHost().getStandard_rate());
            }
            startDate = startDate.plusDays(1);
        }
        return total;
    }
}
