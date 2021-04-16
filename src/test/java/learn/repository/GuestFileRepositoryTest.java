package learn.repository;

import learn.models.Guest;
import learn.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    private static final String SEED_FILE = "./data/guest-seed-file.csv";
    private static final String TEST_FILE = "./data/guest-test-file.csv";

    GuestFileRepository repository = new GuestFileRepository(TEST_FILE);

    @BeforeEach
    void setUp() throws IOException {
        Path seedPath = Paths.get(SEED_FILE);
        Path testPath = Paths.get(TEST_FILE);

        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldReturnGuestValidEmail() throws DataAccessException {
        Guest guest = repository.findByEmail("ndetoile3r@yahoo.co.jp");
        String expected = "Detoile";

        assertNotNull(guest);
        assertEquals(expected, guest.getLastName());
    }

    @Test
    void shouldNotReturnHostIfInvalidEmail() throws DataAccessException {
        assertNull(repository.findByEmail("test"));
    }

    @Test
    void shouldNotReturnEmailIfNull() throws DataAccessException {
        assertNull(repository.findByEmail(null));
    }

    @Test
    void shouldAddValidGuest() throws DataAccessException {
        Guest guest = new Guest();
        guest.setFirstName("Test");
        guest.setLastName("Test");
        guest.setEmail("Test@Test.com");
        guest.setPhone("1234567890");
        guest.setState("Test");

        User user = guest;
        Guest actual = repository.add(user);

        assertNotNull(actual);
    }

    @Test
    void shouldNotAddNullUser() throws DataAccessException {
        Guest actual = repository.add(null);
        assertNull(actual);
    }

    @Test
    void shouldUpdateValidGuest() throws DataAccessException {
        Guest guest = repository.findByEmail("ndetoile3r@yahoo.co.jp");
        guest.setLastName("Test");

        User user = guest;
        assertTrue(repository.update(user));
        user = repository.findByEmail(user.getEmail());
        assertEquals("Test", user.getLastName());
    }

    @Test
    void shouldNotUpdateInvalidGuest() throws DataAccessException {
        Guest guest = new Guest();
        guest.setFirstName("Test");
        guest.setLastName("Test");
        guest.setEmail("Test@Test.com");
        guest.setPhone("1234567890");
        guest.setState("Test");

        User user = guest;
        assertFalse(repository.update(user));
    }

    @Test
    void shouldNotUpdateNullHost() throws DataAccessException {
        assertFalse(repository.update(null));
    }

    @Test
    void shouldDeleteIfValid() throws DataAccessException {
        assertTrue(repository.deleteByEmail("ainmankh@example.com"));
    }

    @Test
    void shouldNotDeleteIfInvalid() throws DataAccessException {
        assertFalse(repository.deleteByEmail("test"));
        assertFalse(repository.deleteByEmail(null));
    }
}