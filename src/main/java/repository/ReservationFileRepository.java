package repository;

import models.Guest;
import models.Host;
import models.Reservation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ReservationFileRepository implements ReservationRepository {

    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private static final String DELIMITER = ",";
    private final String repository;
    private final int RESERVATION_SPLIT_FIELDS = 5;

    public ReservationFileRepository(String repository) {
        this.repository = repository;
    }

    @Override
    public List<Reservation> findById(String id) throws DataAccessException {
        ArrayList<Reservation> all = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(id)))) {

            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(DELIMITER, -1);
                if (fields.length == RESERVATION_SPLIT_FIELDS) {
                    all.add(deserialized(fields, id));
                }
            }
        } catch (FileNotFoundException ex) {
            //do nothing
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return all;
    }

    private String getFilePath(String id) {
        return Paths.get(repository, id + ".csv").toString();
    }

    private Reservation deserialized(String[] fields, String id) {
        Reservation reservation = new Reservation();
        reservation.setReservationId(Integer.parseInt(fields[0]));
        reservation.setStartDate(stringToDate(fields[1]));
        reservation.setEndDate(stringToDate(fields[2]));

        Guest guest = new Guest();
        guest.setId(fields[3]);
        reservation.setGuest(guest);

        Host host = new Host();
        host.setId(id);
        reservation.setHost(host);

        reservation.setTotal(new BigDecimal(fields[4]));

        return reservation;
    }

    private LocalDate stringToDate(String date) {
        try {
            return LocalDate.parse(date, Reservation.FORMATTER);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}
