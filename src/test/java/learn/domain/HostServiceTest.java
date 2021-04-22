package learn.domain;

import learn.models.Host;
import learn.models.User;
import learn.repository.DataAccessException;
import learn.repository.HostRepositoryDouble;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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
    void shouldNotAcceptEmailWithComma() throws DataAccessException {
        Result<User> result = service.findByEmail("test@te,st.com");
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
        User user = makeHost();
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
        Host host = makeHost();
        host.setStandardRate(new BigDecimal("0.00"));
        host.setWeekendRate(new BigDecimal("0.00"));

        Result<User> result = service.addUser(host);
        assertFalse(result.isSuccess());
        assertEquals(2, result.getMessages().size());
    }

    @Test
    void shouldNotAddHostIfInvalidEmail() throws DataAccessException {
        User host = makeHost();
        host.setEmail(null);
        Result<User> result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setEmail(" ");
        result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setEmail("test");
        result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setEmail("test@");
        result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setEmail("test@test");
        result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setEmail("test@test@test.com");
        result = service.addUser(host);
        assertFalse(result.isSuccess());

    }

    @Test
    void shouldNotAddHostIfInvalidPhone() throws DataAccessException {
        User host = makeHost();
        host.setPhone(null);
        Result<User> result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setPhone(" ");
        result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setPhone("1234567890");
        result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setPhone("(123) 123456734");
        result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setPhone("aaaaaaaaaaaa");
        result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setPhone("a123a 1234567");
        result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setPhone("(123)1234567");
        result = service.addUser(host);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddHostIfNoName() throws DataAccessException {
        User host = makeHost();
        host.setLastName(" ");
        Result<User> result = service.addUser(host);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddHostIfNoState() throws DataAccessException {
        User host = makeHost();
        host.setState(" ");
        Result<User> result = service.addUser(host);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldAddHostWithValidState() throws DataAccessException {
        User host = makeHost();
        host.setState("MN");
        Result<User> result = service.addUser(host);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAddHostIfInvalidState() throws DataAccessException {
        User host = makeHost();
        host.setState(",,");
        Result<User> result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setState("Virginia");
        result = service.addUser(host);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldAddHostWithValidPostalCode() throws DataAccessException {
        Host host = makeHost();

        Result<User> result = service.addUser(host);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAddHostIfInvalidPostalCode() throws DataAccessException {
        Host host = makeHost();
        host.setPostalCode("-1234");
        Result<User> result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setPostalCode("Virginia");
        result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setPostalCode(" ");
        result = service.addUser(host);
        assertFalse(result.isSuccess());

        host = makeHost();
        host.setPostalCode(null);
        result = service.addUser(host);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddHostIfIdIsSet() throws DataAccessException {
        User host = makeHost();
        host.setId("Test");
        Result<User> result = service.addUser(host);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddHostIfDuplicate() throws DataAccessException {
        User host = makeHost();
        host.setEmail("kdeclerkdc@sitemeter.com");
        Result<User> result = service.addUser(host);
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
        User host = makeHost();
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

    //support methods
    private Host makeHost() {
        Host host = new Host();
        host.setLastName("Test");
        host.setEmail("Test@test.com");
        host.setPhone("(111) 1111111");

        host.setAddress("1 Test");
        host.setCity("Test");
        host.setState("TT");
        host.setPostalCode("11111");

        host.setStandardRate(new BigDecimal("100.00"));
        host.setWeekendRate(new BigDecimal("150.00"));
        host.setDeleted(false);
        
        return host;
    }
}

