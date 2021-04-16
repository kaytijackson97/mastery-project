package learn.repository;

import learn.models.Guest;
import learn.models.User;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository {

    private final ArrayList<Guest> guests = new ArrayList<>();
    public static final Guest GUEST = new Guest("663","Wallis","Kuhl",
            "wkuhlie@patch.com","(704) 3740857","NC", false);
    public static final Guest DELETED_GUEST = new Guest("790","Wallis","Kuhl",
            "testDeleted@test.com","(704) 3740857","NC", true);

    public GuestRepositoryDouble() {
        guests.add(GUEST);
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
        return guests;
    }

    @Override
    public List<Guest> findAllNotDeleted() throws DataAccessException {
        return null;
    }

    @Override
    public Guest add(User user) throws DataAccessException {
        Guest guest = createGuestFromUser(user);
        List<Guest> all = findAll();
        int nextId = all.size();
        String guestId = String.format("%s", nextId);

        guest.setId(guestId);
        return guest;
    }

    @Override
    public boolean update(User user) throws DataAccessException {
        return findByEmail(user.getEmail()) != null;
    }

    @Override
    public boolean deleteByEmail(String email) throws DataAccessException {
        return false;
    }

    private Guest createGuestFromUser(User user) throws DataAccessException {
        Guest guest = new Guest();

        String[] nameFields = user.getFullName().split(",", -1);
        guest.setFirstName(nameFields[0]);
        guest.setLastName(user.getLastName());
        guest.setEmail(user.getEmail());
        guest.setPhone(user.getPhone());

        guest.setState(user.getState());

        return guest;
    }
}
