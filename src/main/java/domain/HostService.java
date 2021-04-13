package domain;

import models.Host;
import repository.DataAccessException;
import repository.HostRepository;

public class HostService implements UserService{

    private final HostRepository hostRepository;

    public HostService(HostRepository hostRepository) {
        this.hostRepository = hostRepository;
    }

    @Override
    public Host findByEmail(String email) throws DataAccessException {
        return hostRepository.findByEmail(email);
    }

}
