package learn.repository;

import learn.repository.convertToJSON.ReservationToJSONFileRepository;
import learn.repository.convertToJSON.ReservationToJSONRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationToJSONRepositoryTest {

    private static final String SEED_JSON_FILE = "./data/JSON-files/reservation-seed-file.json";
    private static final String TEST_JSON_FILE = "./data/JSON-files/reservation-test-file.json";

    ReservationRepository repositoryDouble = new ReservationRepositoryDouble();
    ReservationToJSONRepository repository = new ReservationToJSONFileRepository(TEST_JSON_FILE, repositoryDouble);

    @BeforeEach
    void setUp() throws IOException {
        Path seedPath = Paths.get(SEED_JSON_FILE);
        Path testPath = Paths.get(TEST_JSON_FILE);

        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldWriteAllToJSONFile() throws DataAccessException {
        repository.writeToJSON(new File(ReservationFileRepositoryTest.TEST_DIRECTORY_FOLDER));
    }

    @Test
    void shouldNotWriteAllToJSONFileIfNullDirectory() throws DataAccessException {
        repository.writeToJSON(null);
    }

    @Test
    void shouldReturnFileNameWithoutExtension() throws DataAccessException {
        String actual = repository.getFileNameNoExtension("2e72f86c-b8fe-4265-b4f1-304dea8762db.csv");
        assertEquals("2e72f86c-b8fe-4265-b4f1-304dea8762db", actual);
    }
}
