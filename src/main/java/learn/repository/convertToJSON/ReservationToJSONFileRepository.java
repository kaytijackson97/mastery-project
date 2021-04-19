package learn.repository.convertToJSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import learn.models.Guest;
import learn.models.Host;
import learn.models.Reservation;
import learn.repository.DataAccessException;
import learn.repository.ReservationRepository;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationToJSONFileRepository implements ReservationToJSONRepository {

    private final String filePath;

    public ReservationToJSONFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void writeToJSON(File directory) throws DataAccessException {
        String JsonId = "";
        Map<String, List<Reservation>> allReservationsForHostMap = new HashMap<>();

        if (directory == null || directory.length() == 0) {
            return;
        }
        File[] directoryFiles = directory.listFiles();
        if (directoryFiles == null || directoryFiles.length == 0) {
            return;
        }


        for (int i = 1; i <= directoryFiles.length; i++) {
            String file = getFileNameNoExtension(directoryFiles[i - 1].getName());
            List<Reservation> reservations = findById(file);
            JsonId = "hostReservations" + i;
            allReservationsForHostMap.put(JsonId, reservations);
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

        try {

            writer.writeValue(new File(filePath), allReservationsForHostMap);

        } catch (IOException ex) {

            throw new DataAccessException(ex.getMessage());

        }
    }

    @Override
    public String getFileNameNoExtension(String fileName) {
        int extensionStart = fileName.lastIndexOf('.');
        return fileName.substring(0, extensionStart);
    }

    private List<Reservation> findById(String id) throws DataAccessException {
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
