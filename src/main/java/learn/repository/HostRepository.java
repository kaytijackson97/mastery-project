package learn.repository;

import learn.models.Host;

import java.util.List;

public interface HostRepository {
    Host findByEmail(String email) throws DataAccessException;
    List<Host> findAll() throws DataAccessException;
}
