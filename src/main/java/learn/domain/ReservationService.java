package learn.domain;

import learn.models.Guest;
import learn.models.Host;
import learn.models.Reservation;
import learn.models.User;
import learn.repository.DataAccessException;
import learn.repository.GuestRepository;
import learn.repository.HostRepository;
import learn.repository.ReservationRepository;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
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

        Host host = hostRepository.findAll().stream()
                .filter(i -> i.getId().equalsIgnoreCase(id))
                .findFirst().orElse(null);

        if (host == null) {
            return null;
        }

        List<Reservation> result = reservationRepository.findById(id)
                .stream().filter(r -> r.getStartDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());

        if (result.size() == 0) {
            return null;
        }

        for (Reservation r : result) {
            r.setGuest(guestMap.get(r.getGuest().getId()));
        }
        return result;
    }

    public List<Reservation> findById(String hostId, String guestId) throws DataAccessException {
        List<Reservation> allHostsReservations = findById(hostId);
        if (allHostsReservations == null || allHostsReservations.size() == 0) {
            return null;
        }

        if (guestId == null) {
            return null;
        }

        return allHostsReservations.stream()
                .filter(i -> i.getGuest().getId().equalsIgnoreCase(guestId))
                .collect(Collectors.toList());
    }

    public Reservation findByReservationId(User host, int reservationId) throws DataAccessException {
        List<Reservation> all = findById(host.getId());
        Reservation reservation = all.stream()
                .filter(i -> i.getReservationId() == reservationId)
                .findFirst().orElse(null);

        if (reservation != null) {
            reservation.setHost(host);
        }

        return reservation;
    }

    public Result<Reservation> isReservationAvailable(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = validateFields(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        result = validateDates(reservation);
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

    public Result<Reservation> addReservation(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = isReservationAvailable(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        if (reservation.getReservationId() != 0) {
            result.addErrorMessage("Cannot set reservation Id.");
            return result;
        }

        reservation.setTotal(getPrice(reservation));
        reservation = reservationRepository.add(reservation);

        if (reservation == null) {
            result.addErrorMessage("Could not complete reservation.");
            return result;
        }

        result.setPayload(reservation);
        return result;
    }

    public Result<Reservation> updateReservation(Reservation reservation) throws DataAccessException {
        Result<Reservation> result = isReservationAvailable(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        boolean isSuccess = reservationRepository.update(reservation);
        if (!isSuccess) {
            result.addErrorMessage("Could not update reservation.");
        }

        result.setPayload(reservation);
        return result;
    }

    public Result<Reservation> deleteReservation(String hostId, int reservationId) throws DataAccessException {
        Result<Reservation> result = new Result<>();
        if (hostId == null) {
            result.addErrorMessage("Host Id is required.");
        }
        boolean isSuccess = reservationRepository.deleteById(hostId, reservationId);
        if (!isSuccess) {
            result.addErrorMessage("Could not delete reservation.");
        }
        return result;
    }

    public BigDecimal getPrice(Reservation reservation) {
        BigDecimal total = new BigDecimal("0.00");
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();
        BigDecimal standard_rate = reservation.getHost().getRates().get(0);
        BigDecimal weekend_rate = reservation.getHost().getRates().get(1);

        while (!startDate.isAfter(endDate)) {
            if (startDate.getDayOfWeek() == DayOfWeek.FRIDAY || startDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
                total = total.add(weekend_rate);
            } else {
                total = total.add(standard_rate);
            }
            startDate = startDate.plusDays(1);
        }
        return total;
    }

    private Result<Reservation> validateFields(Reservation reservation) {
        Result<Reservation> result = new Result<>();
        if (reservation == null) {
            result.addErrorMessage("Reservation cannot be null");
            return result;
        }

        if (reservation.getHost() == null) {
            result.addErrorMessage("Host cannot be null");
        }

        if (reservation.getGuest() == null) {
            result.addErrorMessage("Guest cannot be null");
        }

        if (reservation.getStartDate() == null || reservation.getEndDate() == null) {
            result.addErrorMessage("Dates cannot be null.");
        }
        return result;
    }

    private Result<Reservation> validateDates(Reservation reservation) {
        Result<Reservation> result = new Result<>();

        if (reservation.getStartDate().isBefore(LocalDate.now())
        || reservation.getStartDate().equals(LocalDate.now())) {
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
            if (r.getReservationId() == reservation.getReservationId()) {
                break;
            }

            if (r.getStartDate().equals(reservation.getStartDate()) || r.getEndDate().equals(reservation.getEndDate())) {
                result.addErrorMessage("Pre-existing reservation");
                return result;
            }

            LocalDate startDate = r.getStartDate().plusDays(1);
            while (startDate.isBefore(r.getEndDate())) {
                bookedDates.add(startDate);
                startDate = startDate.plusDays(1);
            }
        }

        if (bookedDates.contains(reservation.getStartDate()) || bookedDates.contains(reservation.getEndDate())) {
            result.addErrorMessage("Pre-existing Booking");
        }

        return result;
    }
}
