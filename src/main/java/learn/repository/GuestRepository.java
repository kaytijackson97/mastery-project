package learn.repository;

import learn.models.Guest;

import java.util.List;

public interface GuestRepository {
    Guest findByEmail(String email) throws DataAccessException;

    List<Guest> findAll() throws DataAccessException;
}
