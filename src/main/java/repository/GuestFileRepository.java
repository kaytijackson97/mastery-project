package repository;

import models.Guest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuestFileRepository implements GuestRepository{

    private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";
    private final String filePath;
    private static final String DELIMITER = ",";

    public GuestFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Guest findByEmail(String email) throws DataAccessException {
        List<Guest> allGuests = findAll();
        return allGuests.stream()
                .filter(g -> g.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
    }

    @Override
    public List<Guest> findAll() throws DataAccessException {
        ArrayList<Guest> guests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (!line.isBlank()) {
                    Guest guest = deserialized(line);
                    guests.add(guest);
                }
            }
        } catch (FileNotFoundException ex) {
            //do nothing
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return guests;
    }

    private Guest deserialized(String line) {
        String[] fields = line.split(DELIMITER);
        Guest guest = new Guest();
        guest.setId(fields[0]);
        guest.setFirst_name(fields[1]);
        guest.setLast_name(fields[2]);
        guest.setEmail(fields[3]);
        guest.setPhone(fields[4]);
        guest.setState(fields[5]);

        return guest;
    }
}
