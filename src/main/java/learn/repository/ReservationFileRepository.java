package learn.repository;

import learn.models.Guest;
import learn.models.Host;
import learn.models.Reservation;

import java.io.*;
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

    @Override
    public Reservation add(Reservation reservation) throws DataAccessException {
        if (reservation == null) {
            return null;
        }

        if (reservation.getStartDate() == null || reservation.getEndDate() == null || reservation.getHost() == null
        || reservation.getGuest() == null || reservation.getTotal() == null) {
            return null;
        }

        String hostId = reservation.getHost().getId();
        List<Reservation> all = findById(hostId);
        int nextInt = all.size();

        reservation.setReservationId(nextInt);
        all.add(reservation);
        writeAll(all, hostId);

        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataAccessException {
        if (reservation == null) {
            return false;
        }

        List<Reservation> all = findById(reservation.getHost().getId());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getReservationId() == reservation.getReservationId()) {
                all.set(i, reservation);
                writeAll(all, reservation.getHost().getId());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteById(String hostId, int reservationId) throws DataAccessException {

        List<Reservation> all = findById(hostId);
        if (all == null || all.size() == 0) {
            return false;
        }

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getReservationId() == reservationId) {
                all.remove(i);
                writeAll(all, hostId);
                return true;
            }
        }
        return false;
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

    private String serialized(Reservation reservation) {
        StringBuilder builder = new StringBuilder(100);

        builder.append(reservation.getReservationId()).append(DELIMITER);
        builder.append(reservation.getStartDate()).append(DELIMITER);
        builder.append(reservation.getEndDate()).append(DELIMITER);
        builder.append(reservation.getGuest().getId()).append(DELIMITER);
        builder.append(reservation.getTotal());

        return builder.toString();
    }

    private LocalDate stringToDate(String date) {
        try {
            return LocalDate.parse(date, Reservation.FORMATTER);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private void writeAll(List<Reservation> reservations, String hostId) throws DataAccessException {
        try (PrintWriter writer = new PrintWriter(getFilePath(hostId))) {
            writer.println(HEADER);

            for (Reservation r : reservations) {
                writer.println(serialized(r));
            }
        } catch (FileNotFoundException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}
