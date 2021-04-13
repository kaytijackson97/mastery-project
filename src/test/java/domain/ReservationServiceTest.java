package domain;

import models.Reservation;
import org.junit.jupiter.api.Test;
import repository.DataAccessException;
import repository.GuestRepositoryDouble;
import repository.HostRepositoryDouble;
import repository.ReservationRepositoryDouble;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service = new ReservationService(
            new ReservationRepositoryDouble(),
            new GuestRepositoryDouble(),
            new HostRepositoryDouble());

    //come back to this if still needed
//    @Test
//    void shouldReturnAllReservations() throws DataAccessException {
//        List<Reservation> actual = service.findById(HostRepositoryDouble.HOST_ID);
//        assertEquals(1, actual.size());
//    }



}