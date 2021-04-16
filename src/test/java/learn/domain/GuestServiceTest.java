package learn.domain;

import learn.models.Guest;
import learn.models.Host;
import learn.models.User;
import learn.repository.DataAccessException;
import learn.repository.GuestRepositoryDouble;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuestServiceTest {

    private final Guest guest = new Guest(null, "Test", "Test", "Test@test.com", "(111) 1111111", "TT", false);


    GuestService service = new GuestService(new GuestRepositoryDouble());

    @Test
    void shouldAcceptValidEmail() throws DataAccessException {
        Result<User> result = service.findByEmail("wkuhlie@patch.com");
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldIgnoreCasing() throws DataAccessException {
        Result<User> result = service.findByEmail("wkuhlie@patch.COM");
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
    void shouldAddValidGuest() throws DataAccessException {
        Result<User> result = service.addUser(guest);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAddNullGuest() throws DataAccessException {
        Result<User> result = service.addUser(null);
        assertFalse(result.isSuccess());

        User user = new Host();

        result = service.addUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddGuestIfInvalidEmail() throws DataAccessException {
        Guest newGuest = guest;
        newGuest.setEmail(null);
        User user = newGuest;
        Result<User> result = service.addUser(user);
        assertFalse(result.isSuccess());

        newGuest.setEmail(" ");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        newGuest.setEmail("test");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        newGuest.setEmail("test@");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        newGuest.setEmail("test@test");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        newGuest.setEmail("test@test@test.com");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

    }

    @Test
    void shouldNotAddGuestIfInvalidPhone() throws DataAccessException {
        Guest newGuest = guest;
        newGuest.setPhone(null);
        User user = newGuest;
        Result<User> result = service.addUser(user);
        assertFalse(result.isSuccess());

        newGuest.setPhone(" ");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        newGuest.setPhone("1234567890");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        newGuest.setPhone("(123) 123456734");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        newGuest.setPhone("aaaaaaaaaaaa");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        newGuest.setPhone("(aaa) aaaaaaa");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        newGuest.setPhone("(111)11111111");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        newGuest.setPhone("a123a 1234567");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());

        newGuest.setPhone("(123)1234567");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddGuestIfNoName() throws DataAccessException {
        guest.setFirstName(" ");
        User user = guest;
        Result<User> result = service.addUser(user);
        assertFalse(result.isSuccess());

        Guest newGuest = guest;
        newGuest.setLastName(" ");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddGuestIfIdIsSet() throws DataAccessException {
        Guest newGuest = guest;
        newGuest.setId("Test");
        Result<User> result = service.addUser(newGuest);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddHostIfDuplicate() throws DataAccessException {
        Guest newGuest = guest;
        newGuest.setEmail("wkuhlie@patch.com");
        Result<User> result = service.addUser(newGuest);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldUpdateValidGuest() throws DataAccessException {
        User user = service.findByEmail("wkuhlie@patch.com").getPayload();
        user.setLastName("Test");

        Result<User> result = service.editUser(user);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateNullGuest() throws DataAccessException {
        Result<User> result = service.editUser(null);
        assertFalse(result.isSuccess());

        User user = new Guest();
        result = service.editUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldDeleteIfValid() throws DataAccessException {
        Result<User> result = service.deleteUser(guest);
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
        User user = GuestRepositoryDouble.DELETED_GUEST;
        Result<User> result = service.deleteUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotBeFoundByEmailIfDeleted()  throws DataAccessException {
        Result<User> result = service.findByEmail(GuestRepositoryDouble.DELETED_GUEST.getEmail());
        assertNull(result.getPayload());
    }

}
