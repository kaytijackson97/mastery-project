package learn.repository;

import learn.models.User;
import learn.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HostRepositoryDouble implements HostRepository {

    private ArrayList<Host> hosts = new ArrayList<>();
    public static final String HOST_ID = "2e72f86c-b8fe-4265-b4f1-304dea8762db";
    public static final Host HOST = makeHost();
    public static final Host DELETED_HOST = makeDeletedHost();

    public HostRepositoryDouble() {
        hosts.add(HOST);
        hosts.add(DELETED_HOST);
    }

    @Override
    public Host findByEmail(String email) throws DataAccessException {
        return findAllNotDeleted().stream()
                .filter(h -> h.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
    }

    @Override
    public List<Host> findAll() throws DataAccessException {
        return hosts;
    }

    @Override
    public List<Host> findAllNotDeleted() throws DataAccessException {
        return hosts.stream().filter(h -> !h.isDeleted()).collect(Collectors.toList());
    }

    @Override
    public Host add(User user) throws DataAccessException {
        Host host = createHostFromUser(user);
        return host;
    }

    @Override
    public boolean update(User user) throws DataAccessException {
        return findByEmail(user.getEmail()) != null;
    }

    @Override
    public boolean deleteByEmail(String email) throws DataAccessException {
        return findByEmail(email) != null;
    }

    private static Host makeHost() {
        Host host = new Host();
        host.setId(HOST_ID);
        host.setLastName("de Clerk");
        host.setEmail("kdeclerkdc@sitemeter.com");
        host.setPhone("(208) 9496329");
        host.setAddress("2 Debra Way");
        host.setCity("Boise");
        host.setState("ID");
        host.setPostalCode(83757);
        host.setStandardRate(new BigDecimal("200"));
        host.setWeekendRate(new BigDecimal("250"));
        host.setDeleted(false);
        return host;
    }

    private static Host makeDeletedHost() {
        Host host = new Host();
        host.setId("TestDeleteId");
        host.setLastName("Deleted Test");
        host.setEmail("TestDelete@test.com");
        host.setPhone("(208) 9496329");
        host.setAddress("2 Debra Way");
        host.setCity("Boise");
        host.setState("ID");
        host.setPostalCode(83757);
        host.setStandardRate(new BigDecimal("200"));
        host.setWeekendRate(new BigDecimal("250"));
        host.setDeleted(true);
        return host;
    }

    private Host createHostFromUser(User user) {
        Host host = new Host();

        host.setId(java.util.UUID.randomUUID().toString());
        host.setLastName(user.getLastName());
        host.setEmail(user.getEmail());
        host.setPhone(user.getPhone());

        String[] addressFields = user.getFullAddress().split(",", -1);
        host.setAddress(addressFields[0]);
        host.setCity(addressFields[1]);
        host.setState(user.getState());
        host.setPostalCode(Integer.parseInt(addressFields[3]));

        host.setStandardRate(user.getRates().get(0));
        host.setWeekendRate(user.getRates().get(1));

        return host;
    }
}
