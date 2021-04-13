package repository;

import models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    private ArrayList<Host> hosts = new ArrayList<>();
    public static final String HOST_ID = "2e72f86c-b8fe-4265-b4f1-304dea8762db";
    public static final Host HOST = new Host(HOST_ID, "de Clerk", "kdeclerkdc@sitemeter.com",
            "(208) 9496329", "2 Debra Way", "Boise", "ID", 83757,
            new BigDecimal("200"), new BigDecimal("250"));

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
}
