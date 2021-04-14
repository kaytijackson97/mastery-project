package learn.repository;

import learn.models.Guest;
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
        Guest guest = repository.findByEmail("slomas0@mediafire.com");
        String expected = "Lomas";

        assertNotNull(guest);
        assertEquals(expected, guest.getLast_name());
    }

    @Test
    void shouldNotReturnHostIfInvalidEmail() throws DataAccessException {
        assertNull(repository.findByEmail("test"));
    }

    @Test
    void shouldNotReturnEmailIfNull() throws DataAccessException {
        assertNull(repository.findByEmail(null));
    }
}