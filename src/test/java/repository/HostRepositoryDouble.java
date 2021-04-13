package repository;

import models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    private ArrayList<Host> hosts = new ArrayList<>();
    public static final String HOST_ID = "2e72f86c-b8fe-4265-b4f1-304dea8762db";
    public static final Host HOST = makeHost();

    public HostRepositoryDouble() {
        hosts.add(HOST);
    }

    @Override
    public Host findByEmail(String email) throws DataAccessException {
        return null;
    }

    @Override
    public List<Host> findAll() throws DataAccessException {
        return hosts;
    }

    private static Host makeHost() {
        Host host = new Host();
        host.setId(HOST_ID);
        host.setLast_name("de Clerk");
        host.setEmail("kdeclerkdc@sitemeter.com");
        host.setPhone("(208) 9496329");
        host.setAddress("2 Debra Way");
        host.setCity("Boise");
        host.setState("ID");
        host.setPostal_code(83757);
        host.setStandard_rate(new BigDecimal("200"));
        host.setWeekend_rate(new BigDecimal("250"));
        return host;
    }
}
