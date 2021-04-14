package repository;

import models.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository {

    private final ArrayList<Reservation> reservations = new ArrayList<>();
    private final String hostId = "2e72f86c-b8fe-4265-b4f1-304dea8762db";
    private final LocalDate startDate = LocalDate.of(2021, 10, 12);
    private final LocalDate endDate = LocalDate.of(2021, 10, 14);
    private final Reservation reservation = new Reservation(HostRepositoryDouble.HOST, GuestRepositoryDouble.GUEST, startDate, endDate);
    private final Reservation reservation1 = new Reservation(HostRepositoryDouble.HOST, GuestRepositoryDouble.GUEST, startDate.plusWeeks(1), endDate.plusWeeks(1));


    public ReservationRepositoryDouble() {
        reservation.setReservationId(1);
        reservation1.setReservationId(2);
        reservations.add(reservation);
        reservations.add(reservation1);
    }

    @Override
    public List<Reservation> findById(String id) throws DataAccessException {
        return reservations.stream()
                .filter(r -> r.getHost().getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
    }

    @Override
    public Reservation add(Reservation reservation) throws DataAccessException {
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataAccessException {
        return findById(reservation.getHost().getId()) != null;
    }

    @Override
    public boolean deleteById(String hostId, int reservationId) throws DataAccessException {
        return findById(hostId) != null;
    }

}
