package domain;

import models.User;
import repository.DataAccessException;

public interface UserService {
    User findByEmail(String email) throws DataAccessException;
}
