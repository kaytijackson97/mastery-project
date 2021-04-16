package learn.repository;

import learn.models.Guest;
import learn.models.User;

import java.util.List;

public interface GuestRepository {
    Guest findByEmail(String email) throws DataAccessException;

    List<Guest> findAll() throws DataAccessException;

    List<Guest> findAllNotDeleted() throws DataAccessException;

    Guest add(User user) throws DataAccessException;

    boolean update(User user) throws DataAccessException;

    boolean deleteByEmail(String email) throws DataAccessException;

}
