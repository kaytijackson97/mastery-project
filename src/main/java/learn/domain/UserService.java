package learn.domain;

import learn.models.User;
import learn.repository.DataAccessException;

public interface UserService {
    Result<User> findByEmail(String email) throws DataAccessException;

    Result<User> validateEmail(String email);
}
