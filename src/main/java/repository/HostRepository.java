package repository;

import models.Host;

public interface HostRepository {
    Host findByEmail(String email) throws DataAccessException;
}
