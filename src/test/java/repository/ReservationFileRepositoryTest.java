package repository;

import models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    private static final String SEED_FILE = "./data/reservations-seed-file.csv";
    private static final String TEST_FILE = "./data/reservations-test-folder/2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    private static final String TEST_DIRECTORY_PATH = "./data/reservations-test-folder";

    private final String testHostId = "2e72f86c-b8fe-4265-b4f1-304dea8762db";

    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIRECTORY_PATH);

    @BeforeEach
    void setUp() throws IOException {
        Path seedPath = Paths.get(SEED_FILE);
        Path testPath = Paths.get(TEST_FILE);

        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldReturnAllReservationIfValidId() throws DataAccessException {
        List<Reservation> actual = repository.findById(testHostId);
        assertEquals(3, actual.size());
    }

    @Test
    void shouldNotReturnAnyReservationsIfInvalid() throws DataAccessException {
        List<Reservation> actual = repository.findById("Test Id");
        assertEquals(0, actual.size());

    }

    @Test
    void shouldNotReturnAnyReservationIfIdIsNullOrBlank() throws DataAccessException {
        List<Reservation> actual = repository.findById(null);
        assertEquals(0, actual.size());

        actual = repository.findById(" ");
        assertEquals(0, actual.size());

        actual = repository.findById("");
        assertEquals(0, actual.size());
    }


}