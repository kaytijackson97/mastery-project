package learn.domain;

import learn.models.Guest;
import learn.models.Host;
import learn.repository.DataAccessException;
import learn.repository.GuestRepository;

public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository guestRepository) {
        this.repository = guestRepository;
    }

    public Result<Guest> findByEmail(String email) throws DataAccessException {
        Result<Guest> result = validateEmail(email);
        if (!result.isSuccess()) {
            return result;
        }

        Guest guest = repository.findByEmail(email);
        if(guest == null) {
            result.addErrorMessage("Guest could not be found.");
            return result;
        }

        result.setPayload(guest);
        return result;
    }

    public Result<Guest> validateEmail(String email) throws DataAccessException {
        Result<Guest> result = new Result<>();
        if (email == null || email.isBlank()) {
            result.addErrorMessage("Email cannot but empty");
            return result;
        }

        if (!email.contains("@")) {
            result.addErrorMessage("Not a valid email address");
            return result;
        }

        String[] emailParts = email.split("@");
        if (emailParts.length != 2 || !emailParts[emailParts.length - 1].contains(".")) {
            result.addErrorMessage("Not a valid email address");
        }
        return result;
    }
}
