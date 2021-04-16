package learn.repository;

import learn.models.Guest;
import learn.models.Host;
import learn.models.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GuestFileRepository implements GuestRepository{

    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";
    private final String filePath;
    private static final String DELIMITER = ",";
    private static final String DELIMITER_REPLACEMENT = "@@@";

    public GuestFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Guest findByEmail(String email) throws DataAccessException {
        return findAllNotDeleted().stream()
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

    @Override
    public List<Guest> findAllNotDeleted() throws DataAccessException {
        return findAll().stream()
                .filter(h -> !h.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Guest add(User user) throws DataAccessException {
        if (user == null) {
            return null;
        }

        Guest guest = createGuestFromUser(user);

        List<Guest> all = findAll();
        all.add(guest);
        writeAll(all);
        return guest;
    }

    @Override
    public boolean update(User user) throws DataAccessException {
        if (user == null) {
            return false;
        }

        Guest guest = createGuestFromUser(user);

        List<Guest> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getEmail().equalsIgnoreCase(guest.getEmail())) {
                all.set(i, guest);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteByEmail(String email) throws DataAccessException {
        List<Guest> all = findAllNotDeleted();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getEmail().equalsIgnoreCase(email)) {
                Guest guest = all.get(i);
                guest.setDeleted(true);
                all.set(i, guest);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    public Guest createGuestFromUser(User user) throws DataAccessException {
        Guest guest = new Guest();

        List<Guest> all = findAll();
        int nextId = all.size();
        String guestId = String.format("%s", nextId);

        guest.setId(guestId);
        String[] nameFields = user.getFullName().split(DELIMITER, -1);
        guest.setFirstName(nameFields[0]);
        guest.setLastName(user.getLastName());
        guest.setEmail(user.getEmail());
        guest.setPhone(user.getPhone());

        guest.setState(user.getState());

        return guest;
    }

    private String serialized(Guest guest) {
        StringBuilder builder = new StringBuilder(100);

        builder.append(guest.getId()).append(DELIMITER);
        builder.append(cleanField(guest.getFirstName())).append(DELIMITER);
        builder.append(cleanField(guest.getLastName())).append(DELIMITER);
        builder.append(guest.getEmail()).append(DELIMITER);
        builder.append(guest.getPhone()).append(DELIMITER);

        builder.append(guest.getState()).append(DELIMITER);
        builder.append(guest.isDeleted());

        return builder.toString();
    }

    private Guest deserialized(String line) {
        String[] fields = line.split(DELIMITER);
        Guest guest = new Guest();

        guest.setId(fields[0]);
        guest.setFirstName(fields[1]);
        guest.setLastName(fields[2]);
        guest.setEmail(fields[3]);
        guest.setPhone(fields[4]);
        guest.setState(fields[5]);
        guest.setDeleted(fields[6].equals("true"));

        return guest;
    }

    private void writeAll(List<Guest> guests) throws DataAccessException {
        try (PrintWriter writer = new PrintWriter(filePath)) {

            writer.println(HEADER);

            for (Guest g : guests) {
                writer.println(serialized(g));
            }

        } catch (FileNotFoundException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private String cleanField(String field) {
        return field.replace(DELIMITER, DELIMITER_REPLACEMENT)
                .replace("/r", "")
                .replace("/n", "");
    }
}
