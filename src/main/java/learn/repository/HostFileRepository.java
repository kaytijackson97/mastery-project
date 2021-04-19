package learn.repository;

import learn.models.Host;
import learn.models.User;
import learn.repository.convertToJSON.HostToJSONRepository;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HostFileRepository implements HostRepository {

    private static final String HEADER = "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";
    private final String filePath;
    private final HostToJSONRepository hostToJSONRepository;
    private static final String DELIMITER = ",";
    private static final String DELIMITER_REPLACEMENT = "@@@";

    public HostFileRepository(String filePath, HostToJSONRepository hostToJSONRepository) {
        this.filePath = filePath;
        this.hostToJSONRepository = hostToJSONRepository;
    }

    @Override
    public Host findByEmail(String email) throws DataAccessException {
        return findAllNotDeleted().stream()
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

    @Override
    public List<Host> findAllNotDeleted() throws DataAccessException {
        return findAll().stream()
                .filter(h -> !h.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public Host add(User user) throws DataAccessException {
        if (user == null) {
            return null;
        }

        Host host = createHostFromUser(user);
        host.setId(java.util.UUID.randomUUID().toString());

        List<Host> all = findAll();
        all.add(host);
        writeAll(all);
        return host;
    }

    @Override
    public boolean update(User user) throws DataAccessException {
        if (user == null) {
            return false;
        }

        Host host = createHostFromUser(user);

        List<Host> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getEmail().equalsIgnoreCase(host.getEmail())) {
                all.set(i, host);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteByEmail(String email) throws DataAccessException {
        List<Host> all = findAllNotDeleted();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getEmail().equalsIgnoreCase(email)) {
                Host host = all.get(i);
                host.setDeleted(true);
                all.set(i, host);
                writeAll(all);
                return true;
            }
        }
        return false;
    }

    public Host createHostFromUser(User user) {
        Host host = new Host();

        host.setLastName(user.getLastName());
        host.setEmail(user.getEmail());
        host.setPhone(user.getPhone());

        String[] addressFields = user.getFullAddress().split(DELIMITER, -1);
        host.setAddress(addressFields[0]);
        host.setCity(addressFields[1]);
        host.setState(user.getState());
        host.setPostalCode(addressFields[3]);

        host.setStandardRate(user.getRates().get(0));
        host.setWeekendRate(user.getRates().get(1));

        return host;
    }

    private Host deserialized(String line) {
        String[] fields = line.split(DELIMITER);
        Host host = new Host();

        host.setId(fields[0]);
        host.setLastName(fields[1].replace(DELIMITER_REPLACEMENT, DELIMITER));
        host.setEmail(fields[2]);
        host.setPhone(fields[3]);

        host.setAddress(fields[4].replace(DELIMITER_REPLACEMENT, DELIMITER));
        host.setCity(fields[5].replace(DELIMITER_REPLACEMENT, DELIMITER));
        host.setState(fields[6]);
        host.setPostalCode(fields[7]);

        host.setStandardRate(BigDecimal.valueOf(Double.parseDouble(fields[8])));
        host.setWeekendRate(BigDecimal.valueOf(Double.parseDouble(fields[9])));
        host.setDeleted(fields[10].equals("true"));

        return host;
    }

    private String serialized(Host host) {
        StringBuilder builder = new StringBuilder(100);

        builder.append(host.getId()).append(DELIMITER);
        builder.append(cleanField(host.getLastName())).append(DELIMITER);
        builder.append(host.getEmail()).append(DELIMITER);
        builder.append(host.getPhone()).append(DELIMITER);

        builder.append(cleanField(host.getAddress())).append(DELIMITER);
        builder.append(cleanField(host.getCity())).append(DELIMITER);
        builder.append(host.getState()).append(DELIMITER);
        builder.append(host.getPostalCode()).append(DELIMITER);

        builder.append(host.getStandardRate()).append(DELIMITER);
        builder.append(host.getWeekendRate()).append(DELIMITER);
        builder.append(host.isDeleted());

        return builder.toString();
    }

    private void writeAll(List<Host> hosts) throws DataAccessException {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println(HEADER);

            for (Host h : hosts) {
                writer.println(serialized(h));
            }
        } catch (FileNotFoundException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        hostToJSONRepository.writeToJSON(hosts);
    }

    private String cleanField(String field) {
        return field.replace(DELIMITER, DELIMITER_REPLACEMENT)
                .replace("/r", "")
                .replace("/n", "");
    }
}
