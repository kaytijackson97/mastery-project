package learn.repository;

import learn.models.Guest;
import learn.repository.convertToJSON.GuestToJSONRepository;

import java.util.List;

public class GuestToJSONRepositoryDouble implements GuestToJSONRepository {
    @Override
    public void writeToJSON(List<Guest> guests) throws DataAccessException {

    }
}
