package learn.repository;

import learn.models.Guest;
import learn.repository.convertToJSON.GuestToJSONFileRepository;
import learn.repository.convertToJSON.GuestToJSONRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class GuestToJSONRepositoryTest {

    private static final String SEED_JSON_FILE = "./data/JSON-files/guest-seed-file.json";
    private static final String TEST_JSON_FILE = "./data/JSON-files/guest-test-file.json";

    GuestToJSONRepository repository = new GuestToJSONFileRepository(TEST_JSON_FILE);

    @BeforeEach
    void setUp() throws IOException {
        Path seedPath = Paths.get(SEED_JSON_FILE);
        Path testPath = Paths.get(TEST_JSON_FILE);

        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldWriteAllToJSONFile() throws DataAccessException {
        Guest guest = GuestRepositoryDouble.GUEST;
        Guest deletedGuest = GuestRepositoryDouble.DELETED_GUEST;

        ArrayList<Guest> guests = new ArrayList<>();
        guests.add(guest);
        guests.add(deletedGuest);
        repository.writeToJSON(guests);

    }
}
