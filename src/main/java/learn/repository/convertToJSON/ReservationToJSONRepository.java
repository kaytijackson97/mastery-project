package learn.repository.convertToJSON;

import learn.models.Reservation;
import learn.repository.DataAccessException;

import java.io.File;
import java.util.List;

public interface ReservationToJSONRepository {

    List<Reservation> findAll(File directory) throws DataAccessException;

    void writeToJSON() throws DataAccessException;
}
