package learn.repository;

import learn.models.Host;
import learn.models.User;

import java.util.List;

public interface HostRepository {
    Host findByEmail(String email) throws DataAccessException;

    List<Host> findAll() throws DataAccessException;

    List<Host> findAllNotDeleted() throws DataAccessException;

    Host add(User user) throws DataAccessException;

    boolean update(User user) throws DataAccessException;

    boolean deleteByEmail(String email) throws DataAccessException;

}
