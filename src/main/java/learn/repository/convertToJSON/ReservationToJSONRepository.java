package learn.repository.convertToJSON;

import learn.repository.DataAccessException;

import java.io.File;

public interface ReservationToJSONRepository {

    void writeToJSON(File directory) throws DataAccessException;

    String getFileNameNoExtension(String fileName) throws DataAccessException;
}
