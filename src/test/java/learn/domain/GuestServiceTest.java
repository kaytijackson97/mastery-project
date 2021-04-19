package learn.domain;

import learn.models.Guest;
import learn.models.Host;
import learn.models.User;
import learn.repository.DataAccessException;
import learn.repository.GuestRepositoryDouble;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuestServiceTest {

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
        Guest guest = makeGuest();
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
        Guest newGuest = makeGuest();
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
        User newGuest = makeGuest();
        newGuest.setPhone(null);
        Result<User> result = service.addUser(newGuest);
        assertFalse(result.isSuccess());

        newGuest = makeGuest();
        newGuest.setPhone(null);
        result = service.addUser(newGuest);
        assertFalse(result.isSuccess());

        newGuest = makeGuest();
        newGuest.setPhone("1234567890");
        result = service.addUser(newGuest);
        assertFalse(result.isSuccess());

        newGuest = makeGuest();
        newGuest.setPhone("(123) 123456734");
        result = service.addUser(newGuest);
        assertFalse(result.isSuccess());

        newGuest = makeGuest();
        newGuest.setPhone("aaaaaaaaaaaa");
        result = service.addUser(newGuest);
        assertFalse(result.isSuccess());

        newGuest = makeGuest();
        newGuest.setPhone("(aaa) aaaaaaa");
        result = service.addUser(newGuest);
        assertFalse(result.isSuccess());

        newGuest = makeGuest();
        newGuest.setPhone("(111)11111111");
        result = service.addUser(newGuest);
        assertFalse(result.isSuccess());

        newGuest = makeGuest();
        newGuest.setPhone("a123a 1234567");
        result = service.addUser(newGuest);
        assertFalse(result.isSuccess());

        newGuest = makeGuest();
        newGuest.setPhone("(123)1234567");
        result = service.addUser(newGuest);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldAddHostWithValidState() throws DataAccessException {
        User guest = makeGuest();
        guest.setState("MN");
        Result<User> result = service.addUser(guest);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAddHostIfInvalidState() throws DataAccessException {
        User guest = makeGuest();
        guest.setState(",,");
        User user = guest;
        Result<User> result = service.addUser(user);
        assertFalse(result.isSuccess());

        guest.setState("Virginia");
        user = guest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddGuestIfNoName() throws DataAccessException {
        Guest guest = makeGuest();
        guest.setFirstName(" ");
        User user = guest;
        Result<User> result = service.addUser(user);
        assertFalse(result.isSuccess());

        Guest newGuest = makeGuest();
        newGuest.setLastName(" ");
        user = newGuest;
        result = service.addUser(user);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddGuestIfIdIsSet() throws DataAccessException {
        User newGuest = makeGuest();
        newGuest.setId("Test");
        Result<User> result = service.addUser(newGuest);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddHostIfDuplicate() throws DataAccessException {
        User newGuest = makeGuest();
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
        User guest = makeGuest();
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
    void shouldNotBeFoundByEmailIfDeleted() throws DataAccessException {
        Result<User> result = service.findByEmail(GuestRepositoryDouble.DELETED_GUEST.getEmail());
        assertNull(result.getPayload());
    }

    //support methods
    private Guest makeGuest() {
        Guest guest = new Guest();

        guest.setFirstName("Test");
        guest.setLastName("Test");
        guest.setEmail("Test@test.com");
        guest.setPhone("(111) 1111111");
        guest.setState("TT");
        guest.setDeleted(false);

        return guest;
    }
}
