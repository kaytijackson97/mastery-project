package domain;

import models.Guest;
import models.Reservation;
import repository.DataAccessException;
import repository.GuestRepository;
import repository.HostRepository;
import repository.ReservationRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final HostRepository hostRepository;

    public ReservationService(ReservationRepository reservationRepository, GuestRepository guestRepository, HostRepository hostRepository) {
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
    }

    public List<Reservation> findById(String id) throws DataAccessException {

        Map<String, Guest> guestMap = guestRepository.findAll().stream()
                .collect(Collectors.toMap(Guest::getId, i -> i));

        List<Reservation> result = reservationRepository.findById(id);

        if (result == null || result.size() == 0) {
            return null;
        }

        for (Reservation r : result) {
            r.setGuest(guestMap.get(r.getGuest().getId()));
        }

        return result;
    }
}
