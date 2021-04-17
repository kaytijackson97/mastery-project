package learn.repository;

import learn.models.Host;
import learn.repository.convertToJSON.HostToJSONFileRepository;
import learn.repository.convertToJSON.HostToJSONRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HostToJSONRepositoryTest {

    private static final String SEED_JSON_FILE = "./data/JSON-files/host-seed-file.json";
    private static final String TEST_JSON_FILE = "./data/JSON-files/host-test-file.json";

    HostToJSONRepository repository = new HostToJSONFileRepository(TEST_JSON_FILE);

    @BeforeEach
    void setUp() throws IOException {
        Path seedPath = Paths.get(SEED_JSON_FILE);
        Path testPath = Paths.get(TEST_JSON_FILE);

        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldWriteAllToJSONFile() throws DataAccessException {
        Host host = HostRepositoryDouble.HOST;
        Host deletedHost = HostRepositoryDouble.DELETED_HOST;

        ArrayList<Host> hosts = new ArrayList<>();
        hosts.add(host);
        hosts.add(deletedHost);
        repository.writeToJSON(hosts);

    }
}
