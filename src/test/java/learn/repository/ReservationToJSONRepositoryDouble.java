package learn.repository;

import learn.repository.convertToJSON.ReservationToJSONRepository;

import java.io.File;

public class ReservationToJSONRepositoryDouble implements ReservationToJSONRepository {
    @Override
    public void writeToJSON(File directory) throws DataAccessException {

    }

    @Override
    public String getFileNameNoExtension(String fileName) throws DataAccessException {
        return null;
    }
}
