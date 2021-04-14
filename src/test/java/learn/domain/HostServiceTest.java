package learn.domain;

import learn.models.User;
import learn.repository.DataAccessException;
import learn.repository.HostRepositoryDouble;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HostServiceTest {

    HostService service = new HostService(new HostRepositoryDouble());

    @Test
    void shouldAcceptValidEmail() throws DataAccessException {
        Result<User> result = service.findByEmail("kdeclerkdc@sitemeter.com");
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldIgnoreCasing() throws DataAccessException {
        Result<User> result = service.findByEmail("kdeclerkdc@sitemeter.COM");
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAcceptInvalidEmail() throws DataAccessException {
        Result<User> result = service.findByEmail("test@test.com");
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAcceptNullEmail() throws DataAccessException {
        Result<User> result = service.findByEmail(null);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAcceptValidEmailWithoutAtSymbol() throws DataAccessException {
        Result<User> result = service.findByEmail("test");
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAcceptValidEmailWithMoreThanOneSymbol() throws DataAccessException {
        Result<User> result = service.findByEmail("test@test@test.com");
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAcceptValidEmailWithoutPeriodAfterAtSymbol() throws DataAccessException {
        Result<User> result = service.findByEmail("test@test");
        assertFalse(result.isSuccess());
    }
}
