package learn.repository;

import learn.models.Host;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostFileRepository implements HostRepository{

    private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";
    private final String filePath;
    private static final String DELIMITER = ",";

    public HostFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Host findByEmail(String email) throws DataAccessException {
        List<Host> allHosts = findAll();
        return allHosts.stream()
                .filter(h -> h.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
    }

    @Override
    public List<Host> findAll() throws DataAccessException {
        ArrayList<Host> hosts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (!line.isBlank()) {
                    Host host = deserialized(line);
                    hosts.add(host);
                }
            }
        } catch (FileNotFoundException ex) {
            //do nothing
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return hosts;
    }

    private Host deserialized(String line) {
        String[] fields = line.split(DELIMITER);
        Host host = new Host();
        host.setId(fields[0]);
        host.setLast_name(fields[1]);
        host.setEmail(fields[2]);
        host.setPhone(fields[3]);
        host.setAddress(fields[4]);
        host.setCity(fields[5]);
        host.setState(fields[6]);
        host.setPostal_code(Integer.parseInt(fields[7]));
        host.setStandard_rate(BigDecimal.valueOf(Double.parseDouble(fields[8])));
        host.setWeekend_rate(BigDecimal.valueOf(Double.parseDouble(fields[9])));

        return host;
    }
}
