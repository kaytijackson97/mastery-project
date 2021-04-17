package learn.repository.convertToJSON;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import learn.models.Host;
import learn.repository.DataAccessException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HostToJSONFileRepository implements HostToJSONRepository {

    private final String filePath;

    public HostToJSONFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void writeToJSON(List<Host> hosts) throws DataAccessException {
        String JsonId = "";
        Map<String, Host> map = new HashMap<>();
        for (int i = 1; i <= hosts.size(); i++) {
            JsonId = "host" + i;
            map.put(JsonId, hosts.get(i - 1));
        }

        //creates mapper instance
        ObjectMapper mapper = new ObjectMapper();

        //create an instance of DefaultPrettyPrinter()
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

        try {

            writer.writeValue(new File(filePath), map);

        } catch (IOException ex) {

            throw new DataAccessException(ex.getMessage());

        }
    }
}
