package learn.repository;

import learn.models.Host;
import learn.models.User;
import learn.repository.convertToJSON.HostToJSONRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    private static final String SEED_FILE = "./data/host-seed-file.csv";
    private static final String TEST_FILE = "./data/host-test-file.csv";

    HostToJSONRepository hostToJSONRepository = new HostToJSONRepositoryDouble();
    HostRepository repository = new HostFileRepository(TEST_FILE, hostToJSONRepository);

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
        assertEquals(expected, host.getLastName());
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
    void shouldAddValidHost() throws DataAccessException {
        User host = makeHost();
        Host actual = repository.add(host);

        assertNotNull(actual);
    }

    @Test
    void shouldNotAddNullHost() throws DataAccessException {
        Host actual = repository.add(null);
        assertNull(actual);
    }

    @Test
    void shouldUpdateValidHost() throws DataAccessException {
        Host host = repository.findByEmail("kdeclerkdc@sitemeter.com");
        host.setLastName("Test");

        User user = host;
        assertTrue(repository.update(user));
        user = repository.findByEmail(user.getEmail());
        assertEquals("Test", user.getLastName());
    }

    @Test
    void shouldNotUpdateInvalidHost() throws DataAccessException {
        User host = makeHost();
        assertFalse(repository.update(host));
    }

    @Test
    void shouldNotUpdateNullHost() throws DataAccessException {
        assertFalse(repository.update(null));
    }

    @Test
    void shouldDeleteIfValid() throws DataAccessException {
        assertTrue(repository.deleteByEmail("mfader2@amazon.co.jp"));
    }

    @Test
    void shouldNotDeleteIfInvalid() throws DataAccessException {
        assertFalse(repository.deleteByEmail("test"));
        assertFalse(repository.deleteByEmail(null));
    }

    private Host makeHost() {
        Host host = new Host();

        host.setLastName("Test");
        host.setEmail("Test@Test.com");
        host.setPhone("1234567890");

        host.setAddress("1 Test");
        host.setCity("Testville");
        host.setState("Test");
        host.setPostalCode("11111");

        host.setStandardRate(new BigDecimal("200.00"));
        host.setWeekendRate(new BigDecimal("250.00"));

        return host;
    }

}