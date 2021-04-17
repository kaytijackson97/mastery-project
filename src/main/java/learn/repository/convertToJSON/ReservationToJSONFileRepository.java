package learn.repository.convertToJSON;

import learn.models.Reservation;
import learn.repository.DataAccessException;
import learn.repository.ReservationRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationToJSONFileRepository implements ReservationToJSONRepository {

    private final ReservationRepository repository;

    public ReservationToJSONFileRepository(ReservationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Reservation> findAll(File directory) throws DataAccessException {
        File[] filesInDirectory = directory.listFiles();
        List<Reservation> allReservations = new ArrayList<>();
        if (filesInDirectory == null || filesInDirectory.length == 0) {
            return null;
        }

        for (File f : filesInDirectory) {
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {

            } catch (FileNotFoundException ex) {
                //do nothing
            } catch (IOException ex) {
                throw new DataAccessException(ex.getMessage());
            }
        }
        return allReservations;
    }

    @Override
    public void writeToJSON() throws DataAccessException {
        return;
    }
}
