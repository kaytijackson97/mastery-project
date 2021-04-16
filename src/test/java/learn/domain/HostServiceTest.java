package learn.domain;

import learn.models.Host;
import learn.models.User;
import learn.repository.DataAccessException;
import learn.repository.GuestRepositoryDouble;
import learn.repository.HostRepositoryDouble;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class HostServiceTest {

    private final Host host = new Host(null, "Test", "Test@test.com", "(111) 1111111", "1 Test", "Test", "TT", 11111, new BigDecimal("100.00"), new BigDecimal("150.00"), false);

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

    @Test
    void shouldAddValidHost() throws DataAccessException {
        User user = host;
        Result<User> result = service.addUser(user);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAddNullHost() throws DataAccessException {
        Result<User> result = service.addUser(null);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddHostIfNullFields() throws DataAccessException {
        User user = new Host();

        Result<User> result = service.addUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddIfInvalidState() throws DataAccessException {
        User user = service.findByEmail("kdeclerkdc@sitemeter.com").getPayload();

        user.setState("Virginia");
        Result<User> result = service.addUser(user);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
    }

    @Test
    void shouldNotAddHostIfRatesAreLessThanOrEqualToZero() throws DataAccessException {
        host.setStandardRate(new BigDecimal("0.00"));
        host.setWeekendRate(new BigDecimal("0.00"));

        User user = host;

        Result<User> result = service.addUser(user);
        assertFalse(result.isSuccess());
        assertEquals(2, result.getMessages().size());
    }

    @Test
    void shouldNotAddHostIfInvalidEmail() throws DataAccessException {
        host.setEmail(null);
        User user = host;
        Result<User> result = service.addUser(user);
        assertFalse(result.isSuccess());

        host.setEmail(" ");
        user = host;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        host.setEmail("test");
        user = host;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        host.setEmail("test@");
        user = host;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        host.setEmail("test@test");
        user = host;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        host.setEmail("test@test@test.com");
        user = host;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

    }

    @Test
    void shouldNotAddHostIfInvalidPhone() throws DataAccessException {
        host.setPhone(null);
        User user = host;
        Result<User> result = service.addUser(user);
        assertFalse(result.isSuccess());

        host.setPhone(" ");
        user = host;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        host.setPhone("1234567890");
        user = host;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        host.setPhone("(123) 123456734");
        user = host;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        host.setPhone("aaaaaaaaaaaa");
        user = host;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        host.setPhone("a123a 1234567");
        user = host;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        host.setPhone("(123)1234567");
        user = host;
        result = service.addUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddHostIfNoName() throws DataAccessException {
        host.setLastName(" ");
        User user = host;
        Result<User> result = service.addUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddHostIfIdIsSet() throws DataAccessException {
        host.setId("Test");
        User user = host;
        Result<User> result = service.addUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddHostIfDuplicate() throws DataAccessException {
        host.setEmail("kdeclerkdc@sitemeter.com");
        User user = host;
        Result<User> result = service.addUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldUpdateValidHost() throws DataAccessException {
        User user = service.findByEmail("kdeclerkdc@sitemeter.com").getPayload();
        user.setLastName("Test");

        Result<User> result = service.editUser(user);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateNullHost() throws DataAccessException {
        Result<User> result = service.editUser(null);
        assertFalse(result.isSuccess());

        User user = new Host();

        result = service.editUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldDeleteIfValid() throws DataAccessException {
        Result<User> result = service.deleteUser(host);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotDeleteIfInvalid() throws DataAccessException {
        Result<User> result = service.deleteUser(null);
        assertFalse(result.isSuccess());

        User user = new User();
        result = service.deleteUser(user);
        assertFalse(result.isSuccess());

        user.setEmail("Test");
        result = service.deleteUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotDeleteIfAlreadyDeleted() throws DataAccessException {
        User user = HostRepositoryDouble.DELETED_HOST;
        Result<User> result = service.deleteUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotBeFoundByEmailIfDeleted()  throws DataAccessException {
        Result<User> result = service.findByEmail(HostRepositoryDouble.DELETED_HOST.getEmail());
        assertNull(result.getPayload());
    }
}

