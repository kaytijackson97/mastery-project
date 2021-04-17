package learn.repository.convertToJSON;

import learn.models.Host;
import learn.repository.DataAccessException;

import java.util.List;

public interface HostToJSONRepository {

    void writeToJSON(List<Host> hosts) throws DataAccessException;
}
