package learn.repository.convertToJSON;

import learn.models.Host;
import learn.models.Reservation;
import learn.repository.DataAccessException;

import java.io.File;
import java.util.List;

public interface ReservationToJSONRepository {

    void writeToJSON(File directory) throws DataAccessException;

    String getFileNameNoExtension(String fileName) throws DataAccessException;
}
