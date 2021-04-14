package learn.repository;

import learn.models.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findById(String id) throws DataAccessException;

    Reservation add(Reservation reservation) throws DataAccessException;

    boolean update(Reservation reservation) throws DataAccessException;

    boolean deleteById(String hostId, int reservationId) throws DataAccessException;
}
