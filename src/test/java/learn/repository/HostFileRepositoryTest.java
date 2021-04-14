package learn.repository;

import learn.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    private static final String SEED_FILE = "./data/host-seed-file.csv";
    private static final String TEST_FILE = "./data/host-test-file.csv";

    HostRepository repository = new HostFileRepository(TEST_FILE);

    @BeforeEach
    void setUp() throws IOException {
        Path seedPath = Paths.get(SEED_FILE);
        Path testPath = Paths.get(TEST_FILE);

        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldReturnHostValidEmail() throws DataAccessException {
        Host host = repository.findByEmail("kdeclerkdc@sitemeter.com");
        String expected = "de Clerk";

        assertNotNull(host);
        assertEquals(expected, host.getLast_name());
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