package repository;

import models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository{

    private ArrayList<Guest> guests = new ArrayList<>();
    public static final Guest GUEST = new Guest("663","Wallis","Kuhl",
            "wkuhlie@patch.com","(704) 3740857","NC");

    public GuestRepositoryDouble() {
        guests.add(GUEST);
    }

    @Override
    public Guest findByEmail(String email) throws DataAccessException {
        return null;
    }

    @Override
    public List<Guest> findAll() throws DataAccessException {
        return guests;
    }
}
