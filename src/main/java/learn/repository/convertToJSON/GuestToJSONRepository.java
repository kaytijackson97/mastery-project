package learn.repository.convertToJSON;

import learn.models.Guest;
import learn.repository.DataAccessException;

import java.util.List;

public interface GuestToJSONRepository {
    void writeToJSON(List<Guest> guests) throws DataAccessException;
}
