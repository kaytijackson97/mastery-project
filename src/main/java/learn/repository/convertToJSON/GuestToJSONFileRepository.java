package learn.repository.convertToJSON;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import learn.models.Guest;
import learn.repository.DataAccessException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuestToJSONFileRepository implements GuestToJSONRepository {

    private final String filePath;

    public GuestToJSONFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void writeToJSON(List<Guest> guests) throws DataAccessException {
        Map<String, Guest> map = new HashMap<>();
        for (Guest g : guests) {
            map.put(g.getId(), g);
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        try {
            writer.writeValue(new File(filePath), map);
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}
