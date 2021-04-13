package domain;

import models.Host;
import repository.DataAccessException;
import repository.GuestRepository;
import repository.HostRepository;
import repository.ReservationRepository;

public class HostService implements UserService{

    private final HostRepository hostRepository;

    public HostService(HostRepository hostRepository, GuestRepository guestRepository, ReservationRepository reservationRepository) {
        this.hostRepository = hostRepository;
    }

    @Override
    public Host findByEmail(String email) throws DataAccessException {
        return hostRepository.findByEmail(email);
    }

}
