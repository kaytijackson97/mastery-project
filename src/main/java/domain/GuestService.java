package domain;

import models.Guest;
import repository.DataAccessException;
import repository.GuestRepository;

public class GuestService implements UserService {

    private final GuestRepository repository;

    public GuestService(GuestRepository guestRepository) {
        this.repository = guestRepository;
    }

    @Override
    public Guest findByEmail(String email) throws DataAccessException {
        return repository.findByEmail(email);
    }
}
