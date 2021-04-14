package learn.domain;

import learn.repository.DataAccessException;

public interface UserService {
    Result findByEmail(String email) throws DataAccessException;
}
